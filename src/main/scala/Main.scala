import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, ContextShift, ExitCode, IO, IOApp}
import connectors.{InternetArchiveConnector, InternetArchiveConnectorAlgebra}
import org.http4s.client.{Client, JavaNetClientBuilder}
import services._
import cats.syntax.all._

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val cs:  ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    val blockingPool: ExecutorService  = Executors.newFixedThreadPool(5)
    val blocker:      Blocker          = Blocker.liftExecutorService(blockingPool)
    val httpClient:   Client[IO]       = JavaNetClientBuilder[IO](blocker).create

    val internetArchiveConnector: InternetArchiveConnectorAlgebra[IO] = new InternetArchiveConnector[IO](httpClient)
    val internalArchiveService:   InternetArchiveServiceAlgebra[IO]   = new InternetArchiveService[IO](internetArchiveConnector)

    val ffmpegService: FFMpegServiceAlgebra[IO] = new FFMpegService[IO]

    val thumbnailCreatorService: ThumbnailCreatorServiceAlgebra[IO] = new ThumbnailCreatorService[IO](ffmpegService, internalArchiveService)

    val videoPath = args(0)
    val time      = args(1).toInt

    thumbnailCreatorService.createThumbnail(videoPath, time).flatMap {
      case Left(err) => IO(println(s"There has been an error: $err")).as(ExitCode.Error)
      case Right(()) => IO(println("The thumbnail has been successfully created")).as(ExitCode.Success)
    }
  }
}

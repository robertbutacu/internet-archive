package services

import cats.effect.{ContextShift, IO}
import models.InternetArchiveMetadata
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.ExecutionContext

class InternetArchiveServiceSpec extends WordSpec with MustMatchers {
  import cats.effect.Blocker
  import java.util.concurrent._

  implicit val cs:  ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val blockingPool: ExecutorService  = Executors.newFixedThreadPool(5)
  val blocker:      Blocker          = Blocker.liftExecutorService(blockingPool)
  val httpClient:   Client[IO]       = JavaNetClientBuilder[IO](blocker).create

  val service: InternetArchiveServiceAlgebra[IO] = new InternetArchiveService[IO](httpClient)

  "retrieveMetadata" should {
    "correctly retrieve metadata" in {
      service.retrieveMetadata("electricsheep-flock-244-32500-3").unsafeRunSync().get must matchPattern {
        case _: InternetArchiveMetadata =>
      }
    }

    "correctly fail to retrieve metadata" in {
      service.retrieveMetadata("some-unknown-metadata-name").unsafeRunSync() mustBe None
    }
  }
}

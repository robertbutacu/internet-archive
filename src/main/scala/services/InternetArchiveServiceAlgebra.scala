package services

import java.io.File

import cats.Functor
import connectors.InternetArchiveConnectorAlgebra
import models.{BusinessError, InternetArchiveMetadata, VideoNotFoundInArchive}
import cats.syntax.all._

trait InternetArchiveServiceAlgebra[F[_]] {
  def isVideoCorrupted(videoPath: String): F[Either[BusinessError, Unit]]
}

class InternetArchiveService[F[_]](internetArchiveConnector: InternetArchiveConnectorAlgebra[F])(implicit F: Functor[F]) extends InternetArchiveServiceAlgebra[F] {
  override def isVideoCorrupted(videoPath: String): F[Either[BusinessError, Unit]] = {
    // this feels rather unstable - there should be a better way of getting the identifier name
    val file = new File(videoPath)

    val identifier = file.getName.takeWhile(_ != '.')

    internetArchiveConnector.retrieveMetadata(identifier).map {
      case None           => Left(VideoNotFoundInArchive)
      case Some(metadata) =>
        checkMetadata(metadata, file)
    }
  }

  def checkMetadata(internetArchiveMetadata: InternetArchiveMetadata, file: File): Either[BusinessError, Unit] = {
    Right(())
  }
}

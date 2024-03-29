package services

import cats.Monad
import cats.data.EitherT
import models.BusinessError

trait ThumbnailCreatorServiceAlgebra[F[_]] {
  def createThumbnail(videoPath: String, time: Int): F[Either[BusinessError, Unit]]
}

class ThumbnailCreatorService[F[_]](ffmpegService:          FFMpegServiceAlgebra,
                                    internetArchiveService: InternetArchiveServiceAlgebra[F])
                                   (implicit M: Monad[F])
  extends ThumbnailCreatorServiceAlgebra[F] {
  override def createThumbnail(videoPath: String, time: Int): F[Either[BusinessError, Unit]] = {
    (
      for {
        fileMetadata <- EitherT(internetArchiveService.isVideoCorrupted(videoPath))
        // by default, duration is 0 - debatable as to what should be the value
        _            <- EitherT.fromEither(ffmpegService.createThumbnail(videoPath, time.toDouble, fileMetadata.length.getOrElse(0.0)))
      } yield ()
    ).value
  }
}
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
        _ <- EitherT(internetArchiveService.isVideoCorrupted(videoPath))
        _ <- EitherT.pure(ffmpegService.createThumbnail(videoPath, time, Int.MaxValue))
      } yield ()
    ).value
  }
}
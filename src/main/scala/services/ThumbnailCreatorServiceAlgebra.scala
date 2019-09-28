package services

import cats.Monad
import cats.syntax.all._
import models.BusinessError

trait ThumbnailCreatorServiceAlgebra[F[_]] {
  def createThumbnail(videoPath: String, time: Int): F[Either[BusinessError, Unit]]
}

class ThumbnailCreatorService[F[_]](ffmpegService:          FFMpegServiceAlgebra[F],
                                    internetArchiveService: InternetArchiveServiceAlgebra[F])
                                   (implicit M: Monad[F])
  extends ThumbnailCreatorServiceAlgebra[F] {
  override def createThumbnail(videoPath: String, time: Int): F[Either[BusinessError, Unit]] = {
    internetArchiveService.isVideoCorrupted(videoPath).flatMap {
      case Left(err) => M.pure(Left(err))
      case Right(_) => ffmpegService.createThumbnail(videoPath, time, Int.MaxValue)
    }
  }
}
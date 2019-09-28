package services

trait ThumbnailCreatorServiceAlgebra[F[_]] {
  def createThumbnail(filePath: String, time: String): F[Unit]
}

class ThumbnailCreatorService[F[_]](ffmpegService:          FFMpegServiceAlgebra[F],
                                    internetArchiveService: InternetArchiveServiceAlgebra[F]) extends ThumbnailCreatorServiceAlgebra[F] {
  override def createThumbnail(filePath: String, time: String): F[Unit] = ???
}
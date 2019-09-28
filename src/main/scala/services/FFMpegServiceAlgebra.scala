package services

trait FFMpegServiceAlgebra[F[_]] {
  def createThumbnail(filePath: String, time: String): F[Unit]
}

class FFMpegService[F[_]]() extends FFMpegServiceAlgebra[F] {
  override def createThumbnail(filePath: String, time: String): F[Unit] = ???
}
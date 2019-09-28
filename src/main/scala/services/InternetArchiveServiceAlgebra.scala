package services

import cats.MonadError
import cats.effect.Sync
import models.{BusinessError, InternetArchiveMetadata}
import cats.syntax.functor._
import org.http4s.InvalidMessageBodyFailure
import org.http4s.client.Client

trait InternetArchiveServiceAlgebra[F[_]] {
  def isFileCorrupted(filePath: String): F[Either[BusinessError, Unit]]
}

class InternetArchiveService[F[_]: Sync](httpClient: Client[F],

                                        )(implicit M: MonadError[F, Throwable]) extends InternetArchiveServiceAlgebra[F] {
  override def isFileCorrupted(filePath: String): F[Either[BusinessError, Unit]] = ???
}

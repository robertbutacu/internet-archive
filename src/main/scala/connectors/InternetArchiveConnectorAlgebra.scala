package connectors

import cats.MonadError
import cats.effect.Sync
import models.InternetArchiveMetadata
import org.http4s.InvalidMessageBodyFailure
import org.http4s.client.Client
import cats.syntax.functor._

trait InternetArchiveConnectorAlgebra[F[_]] {
  def retrieveMetadata(resourceName: String): F[Option[InternetArchiveMetadata]]
}

class InternetArchiveConnector[F[_]: Sync](httpClient: Client[F])(implicit M: MonadError[F, Throwable]) extends InternetArchiveConnectorAlgebra[F] {
  override def retrieveMetadata(resourceName: String): F[Option[InternetArchiveMetadata]] = {
    import org.http4s.circe.CirceEntityDecoder._

    val url = s"https://archive.org/metadata/$resourceName"

    M.recover(httpClient.expect[InternetArchiveMetadata](url).map(Option(_))) {
      case _: InvalidMessageBodyFailure => None
    }
  }
}

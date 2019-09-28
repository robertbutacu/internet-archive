package models

case class InternetArchiveMetadata(files: List[File])

object InternetArchiveMetadata {
  import io.circe._, io.circe.generic.semiauto._

  implicit val decoder: Decoder[InternetArchiveMetadata] = deriveDecoder[InternetArchiveMetadata]
}
package models

case class InternetArchiveMetadata(files: List[FileMetadata])

object InternetArchiveMetadata {
  import io.circe._, io.circe.generic.semiauto._

  implicit val decoder: Decoder[InternetArchiveMetadata] = deriveDecoder[InternetArchiveMetadata]
}
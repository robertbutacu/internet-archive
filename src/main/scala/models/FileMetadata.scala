package models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class FileMetadata(name:   String,
                        crc32:  Option[String],
                        length: Option[Double])

object FileMetadata {
  implicit val decoder: Decoder[FileMetadata] = deriveDecoder[FileMetadata]
}

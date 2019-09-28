package models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class File(name:   String,
                format: String,
                md5:    String,
                mtime:  Option[String],
                size:   Option[String])

object File {
  implicit val decoder: Decoder[File] = deriveDecoder[File]
}

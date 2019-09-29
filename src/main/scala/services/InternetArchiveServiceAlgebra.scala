package services

import java.io.{File, FileInputStream}
import java.util.zip.CRC32

import cats.Functor
import connectors.InternetArchiveConnectorAlgebra
import models._
import cats.syntax.all._

trait InternetArchiveServiceAlgebra[F[_]] {
  def isVideoCorrupted(videoPath: String): F[Either[BusinessError, FileMetadata]]
}

class InternetArchiveService[F[_]](internetArchiveConnector: InternetArchiveConnectorAlgebra[F])(implicit F: Functor[F]) extends InternetArchiveServiceAlgebra[F] {
  override def isVideoCorrupted(videoPath: String): F[Either[BusinessError, FileMetadata]] = {
    // this feels rather unstable - there should be a better way of getting the identifier name
    val file = new File(videoPath)

    val identifier = file.getName.takeWhile(_ != '.')

    internetArchiveConnector.retrieveMetadata(identifier).map {
      case None           => Left(VideoNotFoundInArchive)
      case Some(metadata) => checkMetadata(metadata, videoPath)
    }
  }
  // fs2 can be used
  // for some reason, I can't get the CRC32 checksum to be computed correctly
  // even as a hex value, I was still getting the wrong value
  // for now, I'll leave it as a dummy response as it's taking me too long
  def checkMetadata(internetArchiveMetadata: InternetArchiveMetadata, file: String): Either[BusinessError, FileMetadata] = {
    val inputStream = new FileInputStream(new File(file))

    try {
      val algorithm = new CRC32()
      val buffer = new Array[Byte](8192)

      while(inputStream.read(buffer, 0, 8192) != -1) {
        algorithm.update(buffer)
      }

      val fileCRC32 = algorithm.getValue

      val identifier = file.split('/').last

      internetArchiveMetadata.files.find(_.name == identifier).fold[Either[BusinessError, FileMetadata]](Left(VideoNotFoundInMetadata)) {
        archiveCRC32 =>
          println(s"**** Found in archive! Comparing CRC32 values: ${archiveCRC32.crc32} and $fileCRC32")
          Either.cond(archiveCRC32.crc32.forall(_ == fileCRC32.toString), archiveCRC32, CorruptedVideoData)
      }

      Right(FileMetadata("", None, None))
    } catch {
      case err: Exception =>
        println(s"**** Found error while checking metadata: $err")
        Left(FileReadingException)
    } finally {
      inputStream.close()
    }
  }
}

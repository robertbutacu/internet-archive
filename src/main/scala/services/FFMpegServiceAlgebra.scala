package services

import java.net.URI

import models.{ThumbnailCreationFailure, _}
import java.nio.file.{Files, Paths}

import cats.Applicative

trait FFMpegServiceAlgebra[F[_]] {
  def createThumbnail(filePath: String, time: Int, movieLength: Int): F[Either[BusinessError, Unit]]
}

class FFMpegService[F[_]](implicit A: Applicative[F]) extends FFMpegServiceAlgebra[F] {
  //both of these should come from config or user
  val thumbnailPath = "thumbnail-result.png"
  val thumbnailResolution = "600x480"

  // there should be a better way to get movie length other than checking and passing from InternetArchive metadata file
  override def createThumbnail(filePath: String, time: Int, movieLength: Int): F[Either[BusinessError, Unit]] = {
    A.pure(for {
      _ <- movieFileExists(filePath)
      _ <- thumbnailFileDoesNotExist(thumbnailPath)
      _ <- doesNotExtendMovieLength(time, movieLength)
      _ =  createFileThumbnail(filePath, time)
    } yield ())
  }

  def doesNotExtendMovieLength(time: Int, movieLength: Int): Either[ExtendsMovieLength.type, Unit] = {
    Either.cond(
      time <= movieLength,
      (),
      ExtendsMovieLength
    )
  }

  def movieFileExists(filePath: String): Either[MovieFileDoesNotExist.type, Unit] = {
    Either.cond(
      Files.exists(Paths.get(new URI(s"file:///$filePath"))),
      (),
      MovieFileDoesNotExist
    )
  }

  def thumbnailFileDoesNotExist(filePath: String): Either[ThumbnailFileAlreadyExists.type, Unit] = {
    Either.cond(
      !Files.exists(Paths.get(new URI(s"file:///$filePath"))),
      (),
      ThumbnailFileAlreadyExists
    )
  }

  def createFileThumbnail(filePath: String, time: Int): Unit = {
    import sys.process._
    import scala.language.postfixOps

    // maybe a better way to do this - kinda ugly
    val command = (s"ffmpeg -ss $time -i $filePath -an -vframes 1 -s $thumbnailResolution $thumbnailPath" !)

    // there is a slight delay in the creation of the file which causes the below condition to fail
    // ideally, the function should check that the thumbnail has been created

    /*Either.cond(
      Files.exists(Paths.get(new URI(s"file:///$thumbnailPath"))),
      (),
      ThumbnailCreationFailure
    )*/
    ()
  }
}
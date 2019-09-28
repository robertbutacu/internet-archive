package services

import java.net.URI

import models._
import java.nio.file.{Files, Paths}

trait FFMpegServiceAlgebra {
  def createThumbnail(videoPath: String, time: Int, videoLength: Int): Either[BusinessError, Unit]
}

class FFMpegService() extends FFMpegServiceAlgebra {
  //both of these should come from config or user
  val thumbnailPath = s"${System.getProperty("user.dir")}/thumbnail-result.png"
  val thumbnailResolution = "600x480"

  // there should be a better way to get movie length other than checking and passing from InternetArchive metadata file
  // also, Validated might be a better choice for the required preconditions
  override def createThumbnail(videoPath: String, time: Int, videoLength: Int): Either[BusinessError, Unit] = {
    for {
      _ <- videoExists(videoPath)
      _ <- thumbnailFileDoesNotExist(thumbnailPath) // there might be an overwrite parameter, but that might caused unwanted side effects
      _ <- doesNotExtendVideoLength(time, videoLength)
      _ =  createVideoThumbnail(videoPath, time)
    } yield ()
  }

  def doesNotExtendVideoLength(time: Int, movieLength: Int): Either[ExtendsVideoLength.type, Unit] = {
    Either.cond(
      time <= movieLength,
      (),
      ExtendsVideoLength
    )
  }

  def videoExists(filePath: String): Either[VideoFileDoesNotExist.type, Unit] = {
    Either.cond(
      Files.exists(Paths.get(new URI(s"file:///$filePath"))),
      (),
      VideoFileDoesNotExist
    )
  }

  def thumbnailFileDoesNotExist(filePath: String): Either[ThumbnailFileAlreadyExists.type, Unit] = {
    Either.cond(
      !Files.exists(Paths.get(new URI(s"file:///$filePath"))),
      (),
      ThumbnailFileAlreadyExists
    )
  }

  def createVideoThumbnail(filePath: String, time: Int): Unit = {
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
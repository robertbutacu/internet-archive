package services

import java.io.File

import base.TestData
import models.{VideoFileDoesNotExist, ThumbnailFileAlreadyExists}
import org.scalatest.{MustMatchers, WordSpec}

class FFMpegServiceAlgebraSpec extends WordSpec with MustMatchers with TestData {
  val service: FFMpegService = new FFMpegService

  "movieFileExists" should {
    "be successful if the movie file does exist" in {
      service.videoExists(s"${System.getProperty("user.dir")}/resources/some-file") mustBe Right(())
    }

    "fail with a MovieFileDoesNotExist error if the file does not exist" in {
      service.videoExists("some-invalid-path") mustBe Left(VideoFileDoesNotExist)
    }
  }

  "thumbnailFileDoesNotExist" should {
    "fail if the thumbnail does exist" in {
      service.thumbnailFileDoesNotExist(s"${System.getProperty("user.dir")}/resources/some-file") mustBe Left(ThumbnailFileAlreadyExists)
    }

    "be successful if the thumbnail file does not exist" in {
      service.thumbnailFileDoesNotExist("some-invalid-path") mustBe Right(())
    }
  }

  "createFileThumbnail" should {
    "be successful and create the thumbnail" in {
      new File(thumbnailPath).delete()

      service.createVideoThumbnail(videoPath, 60) mustBe ()
    }
  }
}

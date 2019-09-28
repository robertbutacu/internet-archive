package services

import cats.Id
import models.{MovieFileDoesNotExist, ThumbnailFileAlreadyExists}
import org.scalatest.{MustMatchers, WordSpec}

class FFMpegServiceAlgebraSpec extends WordSpec with MustMatchers {
  val service: FFMpegService[Id] = new FFMpegService[Id]

  "movieFileExists" should {
    "be successful if the movie file does exist" in {
      service.movieFileExists(s"${System.getProperty("user.dir")}/resources/some-file") mustBe Right(())
    }

    "fail with a MovieFileDoesNotExist error if the file does not exist" in {
      service.movieFileExists("some-invalid-path") mustBe Left(MovieFileDoesNotExist)
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
      val testMoviePath = s"${System.getProperty("user.dir")}/resources/avOEPKq_460svvp9.mp4"
      service.createFileThumbnail(testMoviePath, 60) mustBe ()
    }
  }
}

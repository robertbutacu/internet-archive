package services

import base.TestData
import cats.Id
import models.{BusinessError, ExtendsVideoLength, FileMetadata}
import org.scalatest.{MustMatchers, WordSpec}

class ThumbnailCreatorServiceSpec extends WordSpec with MustMatchers with TestData {
  val internetArchiveServiceWithNoDurationVideo = new InternetArchiveServiceAlgebra[Id] {
    override def isVideoCorrupted(videoPath: String): Id[Either[BusinessError, FileMetadata]] = Right(FileMetadata("some-name", None, Option(0.0)))
  }

  val ffmpegService = new FFMpegService()

  val thumbnailCreatorService = new ThumbnailCreatorService[Id](ffmpegService, internetArchiveServiceWithNoDurationVideo)

  "createThumbnail" should {
    "correctly pass the low duration video the the ffmpeg service and fail with a ExtendsVideoLength error" in {
      thumbnailCreatorService.createThumbnail(videoPath, 60) mustBe Left(ExtendsVideoLength)
    }
  }
}

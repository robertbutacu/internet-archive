package services

import base.TestData
import cats.Id
import connectors.InternetArchiveConnectorAlgebra
import models.{InternetArchiveMetadata, VideoNotFoundInArchive}
import org.scalatest.{MustMatchers, WordSpec}

class InternetArchiveServiceSpec extends WordSpec with MustMatchers with TestData {
  val connectorWithNoDataRetrieved: InternetArchiveConnectorAlgebra[Id] = new InternetArchiveConnectorAlgebra[Id]() {
    override def retrieveMetadata(resourceName: String): Id[Option[InternetArchiveMetadata]] = None
  }

  "isFileCorrupted" should {
    "fail when the video does not exist in the InternetArchive" in {
      val service = new InternetArchiveService[Id](connectorWithNoDataRetrieved)
      service.isVideoCorrupted(videoPath) mustBe Left(VideoNotFoundInArchive)
    }
  }
}

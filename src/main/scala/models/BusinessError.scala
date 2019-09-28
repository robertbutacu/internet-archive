package models

sealed trait BusinessError

case object VideoFileDoesNotExist      extends BusinessError
case object ExtendsVideoLength         extends BusinessError
case object ThumbnailFileAlreadyExists extends BusinessError
case object CorruptedVideoData         extends BusinessError
case object ThumbnailCreationFailure   extends BusinessError
case object VideoNotFoundInArchive     extends BusinessError

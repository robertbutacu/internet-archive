package models

sealed trait BusinessError

case object MovieFileDoesNotExist      extends BusinessError
case object ExtendsMovieLength         extends BusinessError
case object ThumbnailFileAlreadyExists extends BusinessError
case object CorruptedMovieData         extends BusinessError
case object ThumbnailCreationFailure   extends BusinessError

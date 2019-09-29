# internet-archive
A simple app exploring FFMpeg and some data corruption checks on a video.

An alternative would be to use meow-mtl to automatically derive a MonadError instance for some F and BusinessError.
This would cut through a level of abstraction over Either.

Also, given the nature of the service - command line with synchronous response - using the Http4s' Client might've been a bit of an overkill.

However, FS2 might've been used to stream everything as well - especially the file.

To run the service, simply use `sbt "run full-path-to-file second-at-which-to-take-thumbnail"`.
Example: `sbt "run /usr/test-usr/files/videos/some-video.mp4 60"`.

The thumbnail should be created in the directory from which the user ran the command as `thumbnail-result.png`.
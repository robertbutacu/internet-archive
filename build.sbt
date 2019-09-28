name := "internet-archive"

version := "0.1"

scalaVersion := "2.13.1"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

val http4sVersion = "0.21.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  // Optional for auto-derivation of JSON codecs
  "io.circe" %% "circe-generic" % "0.12.1"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"


name := "scalaj-http-poc"

version := "0.1"

import sbt._

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "io.spray" %% "spray-json" % "1.3.5",
  "net.liftweb" %% "lift-json" % "3.4.1"
)
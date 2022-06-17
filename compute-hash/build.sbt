name := "compute-hash"

version := "0.1"

import sbt._

libraryDependencies ++= Seq(
  "info.picocli" % "picocli" % "4.2.0",
  "info.picocli" % "picocli-codegen" % "4.2.0" % "provided",
  "com.typesafe.akka" %% "akka-actor" % "2.6.10",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.10" % Test,
  "com.github.pathikrit" %% "better-files" % "3.9.1",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "io.spray" %% "spray-json" % "1.3.5",
  "net.liftweb" %% "lift-json" % "3.4.1"
)

lazy val processAnnotations = taskKey[Unit]("Process annotations")

processAnnotations := {
  val log = streams.value.log

  log.info("Processing annotations ...")

  val classpath = ((products in Compile).value ++ ((dependencyClasspath in Compile).value.files)) mkString ":"
  val destinationDirectory = (classDirectory in Compile).value
  val processor = "picocli.codegen.aot.graalvm.processor.NativeImageConfigGeneratorProcessor"
  val classesToProcess = Seq("com.poc.bhagat.ComputeHash") mkString " "

  val command = s"javac -cp $classpath -proc:only -processor $processor -XprintRounds -d $destinationDirectory $classesToProcess"

  failIfNonZeroExitStatus(command, "Failed to process annotations.", log)

  log.info("Done processing annotations.")
}

def failIfNonZeroExitStatus(command: String, message: => String, log: Logger) = {
  import scala.sys.process._
  val result = command !

  if (result != 0) {
    log.error(message)
    sys.error("Failed running command: " + command)
  }
}

packageBin in Compile := (packageBin in Compile dependsOn (processAnnotations in Compile)).value

enablePlugins(GraalVMNativeImagePlugin)

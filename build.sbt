name := "markup"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" % "twirl-compiler_2.11" % "1.1.1",
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.yaml" % "snakeyaml" % "1.16"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

mainClass in (Compile, run) := Some("nl.fizzylogic.markup.Program")

assemblyJarName in assembly := "markup-0.0.1.jar"

mainClass in assembly := Some("nl.fizzylogic.markup.Program")
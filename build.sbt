import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "consumer"

val circeVersion = "0.14.1"

val myDependencies = Seq(
  "org.apache.kafka"        % "kafka-clients"                 % "3.3.1",
  "io.cloudevents"          % "cloudevents-kafka"             % "2.4.0",
  "io.circe"                %% "circe-core"                   % circeVersion,
  "io.circe"                %% "circe-generic"                % circeVersion,
  "io.circe"                %% "circe-parser"                 % circeVersion,
  "io.circe"                %% "circe-literal"                % circeVersion,
  scalaTest                 % Test
)

lazy val root = (project in file("."))
  .settings(
    name := "my-kafka-app",
    libraryDependencies ++= myDependencies
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

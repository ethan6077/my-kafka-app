import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "app"

val circeVersion = "0.14.1"
val specs2Version = "4.17.0"

val myDependencies = Seq(
  "org.apache.kafka"        % "kafka-clients"                 % "3.3.1",
  "io.cloudevents"          % "cloudevents-kafka"             % "2.4.0",
  "org.typelevel"           %% "cats-effect"                  % "2.5.3",
  "io.circe"                %% "circe-core"                   % circeVersion,
  "io.circe"                %% "circe-generic"                % circeVersion,
  "io.circe"                %% "circe-parser"                 % circeVersion,
  "io.circe"                %% "circe-literal"                % circeVersion,
  "org.specs2"              %% "specs2-core"                  % specs2Version             % "test",
  "org.specs2"              %% "specs2-matcher-extra"         % specs2Version             % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "my-producer",
    libraryDependencies ++= myDependencies
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

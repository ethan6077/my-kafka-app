import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "app"

val circeVersion = "0.14.1"
val specs2Version = "4.17.0"

val myDependencies = Seq(
  "org.apache.kafka"        % "kafka-clients"                 % "3.5.0",
  "org.apache.kafka"        %% "kafka-streams-scala"          % "3.5.0",
  "org.typelevel"           %% "cats-effect"                  % "3.5.0",
  "io.cloudevents"          % "cloudevents-kafka"             % "2.5.0",
  "org.slf4j"               % "slf4j-simple"                  % "2.0.7",
  "io.circe"                %% "circe-core"                   % circeVersion,
  "io.circe"                %% "circe-generic"                % circeVersion,
  "io.circe"                %% "circe-parser"                 % circeVersion,
  "io.circe"                %% "circe-literal"                % circeVersion,
  "org.tpolecat"            %% "doobie-core"                  % "1.0.0-RC1",
  "org.tpolecat"            %% "doobie-postgres"              % "1.0.0-RC1",
  "org.specs2"              %% "specs2-core"                  % specs2Version             % "test",
  "org.specs2"              %% "specs2-matcher-extra"         % specs2Version             % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "my-consumer",
    libraryDependencies ++= myDependencies
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

package app

import app.eventconsumer.EventConsumerClient
import cats.effect.{ExitCode, IO, IOApp, Resource}
import io.cloudevents.CloudEvent
import org.apache.kafka.clients.consumer.KafkaConsumer

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("starting consumer app ..."))
      flag <- parseArgs(args)
      _ <- startConsumer(flag)
      _ <- IO(println("closed producer ..."))
    } yield ExitCode.Success
  }

  private def parseArgs(args: List[String]): IO[String] = {
    if (args.length < 1)
      IO.raiseError(new IllegalArgumentException("Need Consumer Type: EVENT or STREAM"))
    else
      IO.pure(args(0))
  }

  private def startConsumer(flag: String): IO[Unit] = {
    if (flag == "EVENT") {
      startEventConsumer
    } else if (flag == "STREAM") {
      startStreamConsumer
    } else {
      IO.unit
    }
  }

  private def startEventConsumer: IO[Unit] = {
    val eventConsumerResource: Resource[IO, KafkaConsumer[String, CloudEvent]] = EventConsumerClient.buildResource

    eventConsumerResource.use {
      consumerClient => {
        IO {
          while (true) {
            println("starting polling ...")
            val receivedEvents = EventConsumerClient.receiveCloudEvents(consumerClient)
            db.saveEvents(receivedEvents)
            // commit to kafka
            consumerClient.commitSync()
          }
        }
      }
    }
  }

  // TODO: implement it
  private def startStreamConsumer: IO[Unit] = {
    //      val streams: KafkaStreams = StreamProcessor.buildStreams()
    //      streams.start()
    //      val myConsumer = EventProcessor.buildStringConsumer()

    IO.unit
  }

}

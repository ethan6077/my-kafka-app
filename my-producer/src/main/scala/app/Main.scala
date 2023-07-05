package app

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("starting producer ..."))
      _ <- startProducer
      _ <- IO(println("closed producer ..."))
    } yield ExitCode.Success
  }

  private def startProducer: IO[Unit] = {
    val producerResource = producer.buildKafkaProducerResource

    producerResource.use {
      producerClient => {
        val key = "TECH"
//        val key = "COMIC"
        val value = producer.consCloudEvent()
        IO(producer.send(producerClient, key, value))
      }
    }
  }

}

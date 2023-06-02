package app

import app.consumer.EventProcessor
import app.consumer.StreamProcessor
import org.apache.kafka.streams.KafkaStreams

import java.time.Duration
import scala.util.Try

object Main extends App {
  println("starting consumer app ...")

  // StreamProcessor
  val streams: KafkaStreams = StreamProcessor.buildStreams()
  streams.start()

  // EventProcessor
  val myConsumer = EventProcessor.buildStringConsumer()

  while (true) {
    println("starting polling ...")
    val receivedEvents = EventProcessor.receiveStringEvents(myConsumer)
    //      consumer.saveEvents(receivedEvents)
    // commit to kafka
    myConsumer.commitSync()
  }

  println("closing consumer ...")
  Try(myConsumer.close()).recover {
    case _ => println("Failed to close the kafka consumer")
  }

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }
}

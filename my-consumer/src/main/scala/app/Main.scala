package app

import app.consumer.EventProcessor

import scala.util.Try

object Main extends App {
  println("starting consumer ...")
  val myConsumer = consumer.build()

  while (true) {
    println("starting polling ...")
    val receivedEvents = EventProcessor.receive(myConsumer)
    consumer.saveEvents(receivedEvents)
    // commit to kafka
    myConsumer.commitSync()
  }

  println("closing consumer ...")
  Try(myConsumer.close()).recover {
    case _ => println("Failed to close the kafka consumer")
  }
}

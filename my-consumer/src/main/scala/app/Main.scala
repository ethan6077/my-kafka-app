package app

import scala.util.Try

object Main extends App {
  println("starting consumer ...")
  val myConsumer = consumer.build()

  while (true) {
    println("starting polling ...")
    val receivedEvents = consumer.receive(myConsumer)
    consumer.printEvents(receivedEvents)
  }

  println("closing consumer ...")
  Try(myConsumer.close()).recover {
    case _ => println("Failed to close the kafka consumer")
  }
}

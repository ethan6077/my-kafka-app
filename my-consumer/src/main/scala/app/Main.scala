package app

import cats.effect.unsafe.implicits.global

import scala.util.Try

object Main extends App {
  println("starting consumer ...")
  val myConsumer = consumer.build()

  while (true) {
    println("starting polling ...")
    val receivedEvents = consumer.receive(myConsumer)
    consumer.printEvents(receivedEvents)

    println("starting db ...")
    val firstBookTitle = db.findBookTitle(1).unsafeRunSync()
    println(s"First Book Title: ${firstBookTitle}")
  }

  println("closing consumer ...")
  Try(myConsumer.close()).recover {
    case _ => println("Failed to close the kafka consumer")
  }
}

package app

import java.util.UUID

object Main extends App {
  println("hello from consumer")
  val myConsumer = consumer.build()

  val consumerThread = new Thread {
    override def run: Unit = {
      while (true) {
        val receivedEvents = consumer.receive(myConsumer)
        consumer.printEvents(receivedEvents)
      }
    }
  }
  consumerThread.start()
  Thread.sleep(300)

  println("hello from producer")
  val myProducer = producer.build()

  val producerThread = new Thread {
    override def run: Unit = {
        val key = UUID.randomUUID().toString
        val value = producer.consCloudEvent()
        producer.send(myProducer, key, value)
    }
  }
  producerThread.start()
}

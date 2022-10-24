package app

import java.util.UUID

object Main extends App {
  println("starting producer ...")
  val myProducer = producer.build()
  val key = UUID.randomUUID().toString
  val value = producer.consCloudEvent()
  producer.send(myProducer, key, value)

  println("closing producer ...")
  myProducer.flush()
  myProducer.close()
}

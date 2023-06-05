package app

import java.util.UUID

object Main extends App {
  println("starting producer ...")
  val myProducer = producer.build()
//  val key = "COMIC"
  val key = "TECH"
  val value = producer.consCloudEvent()
  producer.send(myProducer, key, value)

  println("closing producer ...")
  myProducer.flush()
  myProducer.close()
}

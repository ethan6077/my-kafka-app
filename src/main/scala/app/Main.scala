package app

import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID
import io.circe.syntax.EncoderOps
import io.cloudevents.core.builder.CloudEventBuilder
import app.schema.{Book, Comic}

object Main extends App {
  println("hello from producer")

  val myProducer = producer.build()
  val key = UUID.randomUUID().toString
  val myBook = Book("ethan", "Magic Book", Comic, 100, java.time.LocalDate.now())
  val payload = myBook.asJson.noSpaces.map(_.toByte).toArray
  val value = CloudEventBuilder
    .v1()
    .withId(UUID.randomUUID().toString)
    .withType("au.com.rea.V1")
    .withSource(URI.create("urn:rea:events:v1:source:rea-source-1"))
    .withTime(OffsetDateTime.now())
    .withDataContentType("application/json")
    .withData(payload)
    .build();

  producer.send(myProducer, key, value)

//  println("hello from consumer")
}

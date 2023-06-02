package app.consumer

import app.schema.Book
import io.circe
import io.cloudevents.CloudEvent
import io.cloudevents.kafka.{CloudEventDeserializer, CloudEventSerializer}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

import java.util.Properties

object StreamProcessor {
  private implicit val stringSerde: Serde[String] = Serdes.String()
  private implicit val intSerde: Serde[Integer] = Serdes.Integer()
  private implicit val cloudEventSerde: Serde[CloudEvent] = Serdes.serdeFrom(new CloudEventSerializer, new CloudEventDeserializer)

  def initStreamProps(): Properties = {
    val consumerProps = new Properties()
    consumerProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processor-03")
    consumerProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    consumerProps
  }

  def joiner(a: String, b: String): String = {
    a + "+" + b
  }

  def buildStreams(): KafkaStreams = {
    val props: Properties = initStreamProps()
    val builder: StreamsBuilder = new StreamsBuilder()
    val sourceOfBooks: KStream[String, CloudEvent] = builder.stream[String, CloudEvent]("my-books-topic")
    val sourceOfFavoriteTypes: KTable[String, String] = builder.table[String, String]("my-favorite-types-topic")

    sourceOfBooks
      .map((_, v) => repartition(v))
      .peek((k, v) => peekStreamEvent(k, v))
      .join(sourceOfFavoriteTypes)(joiner)
      .to("my-books-output")

    val topology: Topology = builder.build()
//    println(s"Topology Description: ${topology.describe()}")

    val streams: KafkaStreams = new KafkaStreams(topology, props)

    streams
  }

  private def peekStreamEvent(key: String, value: String): Unit = {
    println("------------ peeking a stream event ------------")
    println(s"----------- key after map: $key ---------------")
    println(s"----------- value after map: $value -----------")
  }

  private def repartition(event: CloudEvent): (String, String) = {
    val maybeBook: Either[circe.Error, Book] = Book.buildBookFromEvent(event)
    maybeBook match {
      case Left(_) => ("UNKNOWN_TYPE", "UNKNOWN_TITLE")
      case Right(book) => (book.`type`.value, book.title)
    }
  }

}

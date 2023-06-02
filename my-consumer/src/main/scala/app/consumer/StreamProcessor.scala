package app.consumer

import app.schema.Book
import io.circe
import io.cloudevents.CloudEvent
import io.cloudevents.kafka.{CloudEventDeserializer, CloudEventSerializer}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

import java.time.Duration
import java.util.Properties

object StreamProcessor {
  private implicit val stringSerde: Serde[String] = Serdes.String()
  private implicit val cloudEventSerde: Serde[CloudEvent] = Serdes.serdeFrom(new CloudEventSerializer, new CloudEventDeserializer)

  def initStreamProps(): Properties = {
    val consumerProps = new Properties()
    consumerProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processor-02")
    consumerProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    consumerProps
  }

  def buildStreams(): KafkaStreams = {
    val props: Properties = initStreamProps()
    val builder: StreamsBuilder = new StreamsBuilder()
    val source: KStream[String, CloudEvent] = builder.stream[String, CloudEvent]("my-topic")

    source
      .peek((_, v) => printEvent(v))
      .to("my-topic-output")

    val topology: Topology = builder.build()
//    println(s"Topology Description: ${topology.describe()}")

    val streams: KafkaStreams = new KafkaStreams(topology, props)

    streams
  }

  private def printEvent(event: CloudEvent): Unit = {
    println("------------ peeking a stream event -----------")
    val maybeBook: Either[circe.Error, Book] = Book.buildBookFromEvent(event)
    maybeBook match {
      case Left(_) => println("decoding book json error!")
      case Right(book) => Book.printTitle(book)
    }
  }

}

package app

import java.util.Properties
import io.cloudevents.CloudEvent
import io.cloudevents.kafka.CloudEventSerializer
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

package object producer {
  def build(): KafkaProducer[String, CloudEvent] = {
    val producerProps = new Properties()
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT")
    producerProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 20000)

    val newProducer = new KafkaProducer[String, CloudEvent](producerProps, new StringSerializer, new CloudEventSerializer)

    newProducer
  }

  def send(kafkaProducer: KafkaProducer[String, CloudEvent], key: String, value: CloudEvent): Unit = {
    println("sending a new message ...")

    val record = new ProducerRecord("my-topic", key, value)

    kafkaProducer.send(record).get()
  }
}

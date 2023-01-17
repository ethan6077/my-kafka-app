# Tutorial

## Books

Book: https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/

Chapter 4 - Consumer: https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/ch04.html

Confluent Kafka Consumer Doc: https://docs.confluent.io/platform/current/clients/consumer.html#ak-consumer

## Diagram

Kafka Event Order: https://miro.com/app/board/uXjVPx1qe5o=/

## Commits and Offsets

- Automatic Commit

`enable.auto.commit=true`

Every five seconds the consumer will commit the largest offset your client received from `poll()`.

- Commit Current Offset

`enable.auto.commit=false`

`myConsumer.commitSync()`

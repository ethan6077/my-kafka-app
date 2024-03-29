# Tutorial

## Books

Book: https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/

Chapter 3- Writing message: [ref](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/ch03.html?_gl=1*177fool*_ga*OTA0MTg1Njk2LjE2NzM5MzI3Nzk.*_ga_092EL089CH*MTY3MzkzNzExOC4yLjEuMTY3MzkzNzk5MS42MC4wLjA.#writing_messages_to_kafka)

Chapter 4 - Consumer: [ref](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/ch04.html)

Confluent Kafka Consumer Doc: [ref](https://docs.confluent.io/platform/current/clients/consumer.html#ak-consumer)

## Diagram

Kafka Event Order: https://miro.com/app/board/uXjVPx1qe5o=/

## Commits and Offsets

- Automatic Commit

`enable.auto.commit=true`

Every five seconds the consumer will commit the largest offset your client received from `poll()`.

- Commit Current Offset

`enable.auto.commit=false`

`myConsumer.commitSync()`

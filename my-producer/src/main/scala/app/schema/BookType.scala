package app.schema

sealed trait BookType {
  val value: String
}

case object Tech extends BookType {
  val value: String = "TECH"
}

case object Comic extends BookType {
  val value: String = "COMIC"
}

case object Novel extends BookType {
  val value: String = "NOVEL"
}

case object Romance extends BookType {
  val value: String = "ROMANCE"
}

case object Other extends BookType {
  val value: String = "OTHER"
}

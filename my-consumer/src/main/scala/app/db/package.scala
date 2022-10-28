package app

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor

package object db {

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql://localhost/postgres", "postgres", "postgres"
  )

  def findBookTitle(id: Int): IO[String] = {
    val sql =
      sql"select title from books where id = $id"

    sql.query[String].unique.transact(xa)
  }
}

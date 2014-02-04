package model

import anorm.{ResultSetParser, RowParser, SqlQuery}
import play.api.db.DB
import play.api.Play.current

/**
 * User: joao.cunha
 */
case class Product (id: Long, ean: Long, name : String, description : String )

object Product {
  var products = Set[Product](
   /* Product(5010255079763L, "Paperclips Large",
      "Large Plain Pack of 1000"),
    Product(5018206244666L, "Giant Paperclips",
      "Giant Plain 51mm 100 pack"),
    Product(5018306332812L, "Paperclip Giant Plain",
      "Giant Plain Pack of 10000"),
    Product(5018306312913L, "No Tear Paper Clip",
      "No Tear Extra Large Pack of 1000"),
    Product(5018206244611L, "Zebra Paperclips",
      "Zebra Length 28mm Assorted 150 Pack")*/
  )

  val sql: SqlQuery = SqlQuery("select * from products order by name asc")

  def findAll = products.toList.sortBy(_.ean)
//  def findAll = DB.withConnection {
//    implicit connection =>
//    sql().map(
//        row => Product(row[Long]("id"), row[Long]("ean"), row[String]("name"), row[String]("description"))
//     ).toList
//  }


  def findByEan( ean : Long) = products.find( _.ean == ean)

  def add( prd : Product) = products += prd

//  def add(product: Product): Boolean = DB.withConnection { implicit connection =>
//    val addedRows =
//      SqlQuery("insert into products values ({id}, {ean}, '{name}', '{description}')").on(
//      "id" -> product.id,
//      "ean" -> product.ean,
//      "name" -> product.name,
//      "description" -> product.description
//    ).executeUpdate()
//      addedRows == 1
//  }

  def getAllWithPatterns: List[Product] = DB.withConnection {
    implicit connection =>
      import anorm.Row
      sql().collect {
        case Row(Some(id: Long), Some(ean: Long), Some(name: String), Some(description: String)) =>
          Product(id, ean, name, description)
      }.toList
  }

  def getAllWithParser: List[Product] = DB.withConnection {
    implicit connection =>
      sql.as(productsParser)
  }

  val productParser: RowParser[Product] = {
    import anorm.~
    import anorm.SqlParser._
    long("id") ~
    long("ean") ~
    str("name") ~
    str("description") map {
      case id ~ ean ~ name ~ description =>
        Product(id, ean, name, description)
    }
  }

  val productsParser: ResultSetParser[List[Product]] = {
    productParser *
  }
}


package heatcontrol

import com.mongodb.casbah._
	
object HelloWorld {
  def main(args: Array[String]) {
    println("Hallo, wereld!")
    val mongoClient = MongoClient("localhost", 27017)
    val db = mongoClient("test")
    db.collectionNames
  }
}

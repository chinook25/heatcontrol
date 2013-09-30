package heatcontrol

import com.mongodb._

object HelloWorld {
  def main(args: Array[String]) {
    println("Hallo, wereld1!")
    val mongoClient: MongoClient = new MongoClient("localhost", 27017)
    val db = mongoClient.getDB("test")
    val coll = db.getCollection("testCollection")
   println("Hallo, wereld2!")
  }
}

package heatcontrol.java;

import java.net.UnknownHostException;
import java.util.*;

import com.mongodb.*;

class DBController implements Runnable {
	DB db;

	public void run() {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			db = mongoClient.getDB("mydb");
			test();
			db.dropDatabase();
			mongoClient.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void insertDocument(String collectionName, BasicDBObject object) {
		DBCollection collection = db.getCollection(collectionName);
		collection.insert(object);
	}

	public DBObject getDocument(String collectionName, DBObject query) {
		DBCursor cursor = db.getCollection(collectionName).find(query);
		DBObject doc= null;
		try {
			while (cursor.hasNext()) {
				doc = cursor.next();
			}
		} finally {
			cursor.close();
		}
		return doc;
	}

	public void test() {
		BasicDBObject doc = new BasicDBObject("name", "MongoDB")
				.append("type", "database").append("count", 1)
				.append("info", new BasicDBObject("x", 203).append("y", 102));
		insertDocument("testCollection", doc);
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
			System.out.println(s);
		}
		DBObject myDoc = db.getCollection("testCollection").findOne();
		System.out.println(myDoc);

	}

	public static void main(String[] args) throws UnknownHostException {
		Thread t = new Thread(new DBController(), "My Thread");
		t.start();
	}
}

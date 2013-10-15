package heatcontrol.java;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;
import com.mongodb.*;

class DBController implements Runnable {
	DB db;
	MongoClient mongoClient;
	private final LinkedBlockingQueue<WeatherObject> weatherQueue;
	private final LinkedBlockingQueue<ExternalSensorObject> externalSensorQueue;
	private final LinkedBlockingQueue<CalendarObject> calendarQueue;

	DBController(LinkedBlockingQueue<WeatherObject> wq,
			LinkedBlockingQueue<ExternalSensorObject> eq,
			LinkedBlockingQueue<CalendarObject> cq) {
		weatherQueue = wq;
		externalSensorQueue = eq;
		calendarQueue = cq;
	}

	public void run() {
		synchronized (this) {
			try {
				mongoClient = new MongoClient("localhost", 27017);
				db = mongoClient.getDB("mydb");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			while (true) {
				WeatherObject forecast = weatherQueue.poll();
				if (forecast != null) {
					insertWeatherData(forecast);
				}
				ExternalSensorObject extern = externalSensorQueue.poll();
				if(extern != null){
					insertExternalSensorData(extern);
				}
				CalendarObject cal = calendarQueue.poll();
				if(cal != null){
					insertCalendarData(cal);
				}
			}
		}

	}

	public void dropDB() {
		db.dropDatabase();
		mongoClient.close();
	}

	public void insertWeatherData(WeatherObject w) {
		BasicDBObject object = new BasicDBObject("Type", w.getType())
				.append("Minimal Temperature", w.getTempMin())
				.append("Maximal Temperature", w.getTempMax())
				.append("Date", w.getDate());
		insertDocument(w.getType(), object);
	}

	public void insertExternalSensorData(ExternalSensorObject e) {
		BasicDBObject object = new BasicDBObject("Type", e.getType())
				.append("Temperature", e.getTemp()).append("Date", e.getDate())
				.append("Time", e.getTimestamp());
		insertDocument(e.getType(), object);
	}

	public void insertCalendarData(CalendarObject c) {
		BasicDBObject object = new BasicDBObject("Type", c.getType())
				.append("Room", c.getRoomID())
				.append("Start Time", c.getStartTime())
				.append("End Time", c.getEndTime()).append("Date", c.getDate());
		insertDocument(c.getType(), object);
	}

	private void insertDocument(String collectionName, BasicDBObject object) {
		DBCollection collection = db.getCollection(collectionName);
		collection.insert(object);
	}

	private DBObject getDocument(String collectionName, DBObject query) {
		DBCursor cursor = db.getCollection(collectionName).find(query);
		DBObject doc = null;
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
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
			System.out.println(s);
		}
		DBObject myDoc = db.getCollection("testCollection").findOne();
		System.out.println(myDoc);

	}

}

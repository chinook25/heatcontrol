package heatcontrol.web;

import heatcontrol.java.CalendarObject;
import heatcontrol.java.WeatherObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Controller
public class Webservice {

	DB database;


	public Webservice() {
		try {
			database = Mongo.connect(new DBAddress("localhost",27017,"mydb"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Async
	@RequestMapping(value = "/Rooms", method=RequestMethod.GET)
	public @ResponseBody Future<String[]> getRooms() {
		ArrayList<String> rooms = new ArrayList<String>();
		DBCollection temperatures = database.getCollection("Internal temperature");
		DBCursor roomCursor = temperatures.find();
		try {
			while (roomCursor.hasNext()) {
				rooms.add(roomCursor.next().get("roomID").toString());
			} 
		} finally {
			roomCursor.close();
		}
		return new AsyncResult<String[]>(rooms.toArray(new String[rooms.size()]));
	}

	@Async
	@RequestMapping(value = "/Internaltemperature", method = RequestMethod.GET)
	public @ResponseBody AsyncResult<TemperatureResponse[]> getInternalTemperature(@RequestParam(value="id", required=true) String roomID) {
		DBCollection col = database.getCollection("Internal temperature");
		DBCursor tempEntry = col.find();
		ArrayList<TemperatureResponse> temps = new ArrayList<TemperatureResponse>();
		DBObject intTempObj = null;
		try {
			if (tempEntry.hasNext())
				intTempObj = tempEntry.next();
				temps.add(new TemperatureResponse(intTempObj));
		} finally {
			tempEntry.close();
		}
		return new AsyncResult<TemperatureResponse[]>(temps.toArray(new TemperatureResponse[temps.size()]));
	}

	@Async
	@RequestMapping(value = "/Weather", method = RequestMethod.GET)
	public @ResponseBody Future<WeatherObject> getWeather()
	{
		DBCollection col = database.getCollection("weather");
		BasicDBObject query = new BasicDBObject("date", today());
		DBCursor cursor = col.find(query);
		DBObject weatherObj = null;
		try {
			if (cursor.hasNext()) {
				weatherObj = cursor.next();
			}
		} finally {
			cursor.close();
		}
		if (weatherObj != null)
			return new AsyncResult<WeatherObject>(new WeatherObject(weatherObj.get("date").toString(),Integer.parseInt(weatherObj.get("temp_min").toString()),Integer.parseInt(weatherObj.get("temp_max").toString())));
		else
			return null;
	}

	@Async
	@RequestMapping(value = "/Calendar", method = RequestMethod.GET)
	public @ResponseBody Future<CalendarObject[]> getCalendar() {
		DBCollection col = database.getCollection("calendar");
		BasicDBObject query = new BasicDBObject("date", today());
		DBCursor cursor = col.find(query);
		//DBCursor cursor = col.find( {date: today(), time_start: "", time_end: "" });
		ArrayList<CalendarObject> cal = new ArrayList<CalendarObject>();
		try {
			while (cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				CalendarObject calObj = new CalendarObject(dbObj.get("date").toString(), dbObj.get("roomID").toString(), dbObj.get("time_start").toString(), dbObj.get("time_end").toString());				
				cal.add(calObj);
			}
		} finally {
			cursor.close();
		}

		return new AsyncResult<CalendarObject[]>(cal.toArray(new CalendarObject[cal.size()]));
	}


	@Async
	@RequestMapping(value = "/StoreTemperature", method= RequestMethod.POST)
	public void storeTemperature(@RequestParam(value="roomID", required=true) String roomID, @RequestParam(value="temp", required=true) int temp) {
		Calendar now = Calendar.getInstance();
		BasicDBObject object = new BasicDBObject("type", "Internal temperature")
		.append("roomID", roomID)
		.append("Temperature", temp)
		.append("Date", Integer.toString(now.DAY_OF_YEAR))
		.append("Time", Integer.toString(now.HOUR_OF_DAY));
		database.getCollection("Internal temperature").insert(object);
	}


	@Async
	@RequestMapping(value = "/NewRoom", method=RequestMethod.POST)
	public void startController(@RequestParam(value="roomID", required=true) String roomID) {

	}

	@Async
	@RequestMapping(value = "/RemoveRoom", method=RequestMethod.DELETE)
	public void removeRoom(@RequestParam(value="roomID", required=true) String roomID) {
		// Stop room controller
		DBCollection roomCollection = database.getCollection(roomID);
		roomCollection.drop();
	}

	/*#*****************
	 *# Helper functions
	 *#*****************/
	private String today() {
		return Integer.toString(Calendar.getInstance().DAY_OF_YEAR);		
	}


}
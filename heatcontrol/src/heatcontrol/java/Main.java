package heatcontrol.java;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;

import com.mongodb.DBObject;

public class Main {

	
	
	public static void main(String[] args) {
		LinkedBlockingQueue<CalendarObject> calendarQueue = new LinkedBlockingQueue<CalendarObject>();
		LinkedBlockingQueue<ExternalSensorObject> externalSensorQueue = new LinkedBlockingQueue<ExternalSensorObject>();
		LinkedBlockingQueue<WeatherObject> weatherQueue = new LinkedBlockingQueue<WeatherObject>();
		LinkedBlockingQueue<QueryObject> queryQueue = new LinkedBlockingQueue<QueryObject>();
		LinkedBlockingQueue<DBObject> answerQueue = new LinkedBlockingQueue<DBObject>();
		
		Thread dbc = new Thread(new DBController(weatherQueue,externalSensorQueue, calendarQueue, queryQueue, answerQueue),"Database Controller");
		dbc.start();
		System.out.println("DBC running");
		Thread eis = new Thread(new ExternalInformationService(weatherQueue,externalSensorQueue, calendarQueue),"External Information Controller");
		eis.start();
		System.out.println("EIS running");
		
		
	}

}

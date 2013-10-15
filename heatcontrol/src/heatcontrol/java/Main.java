package heatcontrol.java;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;

public class Main {

	
	
	public static void main(String[] args) {
		LinkedBlockingQueue<CalendarObject> calendarQueue = new LinkedBlockingQueue<CalendarObject>();
		LinkedBlockingQueue<ExternalSensorObject> externalSensorQueue = new LinkedBlockingQueue<ExternalSensorObject>();
		LinkedBlockingQueue<WeatherObject> weatherQueue = new LinkedBlockingQueue<WeatherObject>();
		
		
		Thread dbc = new Thread(new DBController(weatherQueue,externalSensorQueue, calendarQueue),"Database Controller");
		dbc.start();
		System.out.println("DBC running");
		Thread eis = new Thread(new ExternalInformationService(weatherQueue,externalSensorQueue, calendarQueue),"External Information Controller");
		eis.start();
		System.out.println("EIS running");
		
		
	}

}

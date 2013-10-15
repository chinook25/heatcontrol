package heatcontrol.java;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;

public class Main {

	
	
	public static void main(String[] args) {
		ArrayList<CalendarObject> calendarQueue = new ArrayList<CalendarObject>();
		ArrayList<ExternalSensorObject> externalSensorQueue = new ArrayList<ExternalSensorObject>();
		LinkedBlockingQueue<WeatherObject> weatherQueue = new LinkedBlockingQueue<WeatherObject>();
		
		Thread dbc = new Thread(new DBController(weatherQueue),"Database Controller");
		dbc.start();
		System.out.println("jjj");
		//ExternalInformationService eis = new ExternalInformationService();
		//eis.run();
		while(true){
			try {
				dbc.notify();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
		
	}

}

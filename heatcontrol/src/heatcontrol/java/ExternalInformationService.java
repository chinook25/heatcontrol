package heatcontrol.java;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ExternalInformationService implements Runnable{
	
	private String weatherURL = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=Groningen&format=XML&num_of_days=1&key=8phfymv55kgpmynugrzaq84v";
	private String calendarsURL = "";
	
	private ExternalSensors sensors = null;
	
	private int numSensors = 4; // amount of sensors on the building containing the rooms
	
	public void run() {
		if (sensors == null) {
			sensors = new ExternalSensors(numSensors);
		}
		long startTime = System.currentTimeMillis();
		// Each hour get information from external sensors
		getExternalTemperature();
		
		// Each day at 12pm get new information from calendar and weather sources
		if (Calendar.getInstance().HOUR == 12) {
			getWeatherInformation();
			getCalendarInformation();
		}
		// sleep for the rest of the hour
		try {
			//Thread.sleep(3600000 - (System.currentTimeMillis() - startTime));
			Thread.sleep(6000); // for testing sleep 6 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * getExternalTemperature calls the external sensors to provide the current temperature measured at each sensor.
	 * The average of these temperatures is then saved as the temperature of the environment outside the building containing the rooms.
	 */
	private void getExternalTemperature() {
		int[] temperatures = sensors.simulate();
		int sum = 0;
		for (int i = 0; i < temperatures.length; i++) {
			sum += temperatures[i];
		}
		ExternalSensorObject externalTemp = new ExternalSensorObject(sum/temperatures.length);
		//DBListener.put(externalTemp);
	}
	
	
	private void getCalendarInformation() {
		Document calendarInformation = null;
		try {
			calendarInformation = restCall(calendarsURL);
		} catch (IOException e) {
			System.out.println("Calendar call failed");
			e.printStackTrace();
		}
		
		// TODO: parse list of calenders and get information from individual calendars
	}
	
	/**
	 * getWeatherInformation uses to predefined url to make a rest call to the worldWeatherOnline api. 
	 * It receives a Document object containing the xml returned by the server.
	 * The function then parses this Document and gets the temperature and date from the forecast. This information is then put into the db.
	 */
	private void getWeatherInformation() {
		Document weatherInformation = null;	
		try {
				weatherInformation = restCall(weatherURL);
			} catch (IOException e) {
				System.out.println("Weather call failed");
				e.printStackTrace();
			}
			
			NodeList weatherList = weatherInformation.getElementsByTagName("weather");
					
						Element weather = (Element)weatherList.item(0);
						
						int temp_max = Integer.parseInt(weather.getAttribute("tempMaxC"));
						int temp_min = Integer.parseInt(weather.getAttribute("tempMinC"));
						String date = weather.getAttribute("date");
						
						// TODO: Save information to database
						WeatherObject forecast = new WeatherObject(date,temp_max,temp_min);
						System.out.println("Type: " + forecast.getType());
						System.out.println("Date: " + forecast.getDate());
						System.out.println("Temp min: " + forecast.getTempMin());
						System.out.println("Temp max: " + forecast.getTempMax());
						//DBListener.put(forecast);
	}
			  
	/**
	 * restCall function handles the communication with rest/http api's of the weather forecast website and the google calendar.
	 * The function takes an url, which specifies all paramters for the call, and returns a Document object containing the xml repsonse of the web services.
	 * @param url
	 * @return Document
	 * @throws IOException
	 */
	private Document restCall(String url) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(url));
		
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Rest call failed");
		}
		
		HttpEntity entity = httpResponse.getEntity();
			
		Document content = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			content = builder.parse(entity.getContent());
        } catch (ParserConfigurationException e) {              
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
		
		httpResponse.close();

		return content;    
	}
	
	public static void Main(String[] args) {
		Thread thread = new Thread(new ExternalInformationService(), "ExternalInformationService");
		thread.start();
	}
		
}

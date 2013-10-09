/**
 * WeatherObject is an object used to transfer information from the ExternalInformationService to the Database.
 * It always is of the type ''weather'' and takes a date and min and max temperature.
 */

package heatcontrol.java;

public class WeatherObject {
	private String type = "weather";
	private int temp_max;
	private int temp_min;
	private String date; 
	
	public WeatherObject(String d, int max,int min) {
		this.temp_max = max;
		this.temp_min = min;
		this.date = d;
	}
	
	public String getType() {
		return type;
	}
	
	public int getTempMax() {
		return temp_max;
	}
	
	public int getTempMin() {
		return temp_min;
	}
	
	public String getDate() {
		return date;
	}
	
}

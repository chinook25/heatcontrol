/**
 * ExternalSensorObject is an object used to transfer information from the ExternalInformationService to the Database.
 * It always is of the type ''ExternalSensor''. It creates a data and timestamp of the time it is created and takes the the average temperature of the simulated external sensors.
 */
package heatcontrol.java;

import java.util.Calendar;

public class ExternalSensorObject {
	private String type = "External Sensors";
	private String date;
	private String timestamp;
	private double temp;
	
	/* constructor used when first creating the entry for the DB */
	public ExternalSensorObject(int t) {
		this.date = Calendar.YEAR + "-" + Calendar.MONTH + "-" + Calendar.DATE;
		this.timestamp = Calendar.HOUR + ":" + Calendar.MINUTE;
		this.temp = t;
	}
	
	/* constructor used when using this object to pass information over the web */
	public ExternalSensorObject(String date, String timestamp,
			double temp) {
		this.date = date;
		this.timestamp = timestamp;
		this.temp = temp;
	}

	public String getType() {
		return type;
	}
	
	public String getDate() {
		return date;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public double getTemp() {
		return temp;
	}
}

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
	private int temp;
	
	public ExternalSensorObject(int t) {
		this.date = Calendar.YEAR + "-" + Calendar.MONTH + "-" + Calendar.DATE;
		this.timestamp = Calendar.HOUR + ":" + Calendar.MINUTE;
		this.temp = t;
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
	
	public int getTemp() {
		return temp;
	}
}

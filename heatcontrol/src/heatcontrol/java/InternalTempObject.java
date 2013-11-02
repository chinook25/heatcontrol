package heatcontrol.java;

import java.util.Calendar;

public class InternalTempObject {
	private String type = "Internal temperature";
	private String date;
	private String timestamp;
	private double temp;
	private String roomID;
	
	public InternalTempObject(int t, String id) {
		this.date = Calendar.YEAR + "-" + Calendar.MONTH + "-" + Calendar.DATE;
		this.timestamp = Calendar.HOUR + ":" + Calendar.MINUTE;
		this.temp = t;
		this.roomID = id;
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
	
	public String getRoomID() {
		return roomID;
	}
}
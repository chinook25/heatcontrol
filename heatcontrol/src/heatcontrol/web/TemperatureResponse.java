package heatcontrol.web;

import com.mongodb.DBObject;

public class TemperatureResponse {
	private String type = "Internal temperature";
	private String roomID;
	private String date;
	private String timestamp;
	private int temp;
			
	public TemperatureResponse(DBObject obj) {
		this.roomID = obj.get("roomID").toString();
		this.date = obj.get("date").toString();
		this.timestamp = obj.get(timestamp).toString();
		this.temp = Integer.parseInt(obj.get("temp").toString());
		
	}
	
	public String getType() {
		return type;
	}
	
	public String getID() {
		return roomID;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getTime() {
		return timestamp;
	}
	
	public int getTemp() {
		return temp;
	}
}

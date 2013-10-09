/**
 * CalendarObject is an object used to transfer information from the ExternalInformationService to the Database.
 * It always is of the type ''calendar'' and takes a date, id and start and end times.
 */

package heatcontrol.java;

public class CalendarObject {
	private String type = "calendar";
	private String date;
	private String roomID;
	private String time_start;
	private String time_end;
	
	public CalendarObject(String d, String id, String ts, String te) {
		this.date = d;
		this.roomID = id;
		this.time_start = ts;
		this.time_end = te;	
	}
	
	public String getType() {
		return type;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getRoomID() {
		return roomID;
	}
	
	public String getStartTime() {
		return time_start;
	}
	
	public String getEndTime() {
		return time_end;
	}
}

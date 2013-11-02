package heatcontrol.java;

import java.net.UnknownHostException;

import com.mongodb.*;

public class RoomController implements Runnable {
	DB db;
	MongoClient mongoClient;
	double internalTemperature;
	int externalTemperature;
	String roomID;
	boolean isVentingOn;
	boolean isHeatingOn;
	boolean killed;

	public RoomController(String id) {
		isHeatingOn = false;
		isVentingOn = false;
		killed = false;
		internalTemperature = (int) Math.random() * 11 + 15;
		getExternalTemperature();
		roomID = id;
	}

	public void run() {
		try {
			mongoClient = new MongoClient("localhost", 27017);
			db = mongoClient.getDB("mydb");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		while (!killed) {
			// Update internal temperature every minute
			double delta = calculateTemperatureDelta();
			internalTemperature = internalTemperature + delta;

			// Adjust heating and venting
			if (internalTemperature < 19) {
				turnOnHeating();
			} else if (internalTemperature > 21) {
				turnOnVenting();
			} else {
				turnAllOff();
			}

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	double calculateTemperatureDelta() {
		// update the outside temperature
		getExternalTemperature();
			
		// calculate how much the temperature changes
		double delta = 0;
		if (Math.abs(internalTemperature - externalTemperature) < 2) {
			if (internalTemperature - externalTemperature < 0) {
				// it's warmer outside
				delta = delta + (1 / 60);
			} else {
				// it's colder outside
				delta = delta - (1 / 60);
			}
		}
		if (isHeatingOn) {
			delta = delta + (4 / 60);
		}
		if (isVentingOn) {
			delta = delta - (4 / 60);
		}
		return delta;
	}

	void getExternalTemperature() {
		// TODO
	}

	void getCalendarInformation() {
		// TODO
	}

	void turnOnHeating() {
		isVentingOn = false;
		isHeatingOn = true;
	}

	void turnOnVenting() {
		isHeatingOn = false;
		isVentingOn = true;
	}

	void turnAllOff() {
		isHeatingOn = false;
		isVentingOn = true;
	}

	public void killRoom() {
		killed = true;
	}

	public static void main(String[] args) {
		String id = args[0];
		Thread t = new Thread(new RoomController(id));
		t.start();
	}

}

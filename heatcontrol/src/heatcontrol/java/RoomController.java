package heatcontrol.java;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.*;

import org.apache.zookeeper.*;
import org.springframework.web.client.RestTemplate;

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
	RestTemplate rest = new RestTemplate();
    String url;

	public RoomController(String id, String url) {
		isHeatingOn = false;
		isVentingOn = false;
		killed = false;
		internalTemperature = (int) Math.random() * 11 + 15;
		getExternalTemperature();
		roomID = id;
		this.url = url;
	}

	public void run() {
		System.out.println("hallo");
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

//	public ZooKeeper connect(String hosts, int sessionTimeout)
//	        throws IOException, InterruptedException {
//	  final CountDownLatch connectedSignal = new CountDownLatch(1);
//	  ZooKeeper zk = new ZooKeeper(hosts, sessionTimeout, new Watcher() {
//	    @Override
//	    public void process(WatchedEvent event) {
//	      if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
//	        connectedSignal.countDown();
//	      }
//	    }
//	  });
//	  connectedSignal.await();
//	  return zk;
//	}	
//	
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
		ExternalSensorObject externalTemp = rest.getForObject(url+"/externaltemperature", ExternalSensorObject.class);
        externalTemperature = (int)Math.round(externalTemp.getTemp());
	}

	void getCalendarInformation() {
		// TODO
	}
	void storeInternalTemperature(){
		 InternalTempObject internalTemp = new InternalTempObject((int)internalTemperature, roomID);
	     rest.postForLocation(url+"/storeinternal?roomID={ID}&temp={temp}", internalTemp,roomID,internalTemperature);    
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
		String webserviceUrl = args[1];
		Thread t = new Thread(new RoomController(id, webserviceUrl));
		t.start();
	}

}

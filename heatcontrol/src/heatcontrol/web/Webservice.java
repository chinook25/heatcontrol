package heatcontrol.web;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Controller
public class Webservice {
 
    DB database;
    
    
    public Webservice() {
		try {
			database = Mongo.connect(new DBAddress("localhost",27017,"mydb"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    @Async
    @RequestMapping("/getinternaltemperature")
    public @ResponseBody Future<TemperatureResponse> temp(
            @RequestParam(value="id", required=true) String roomID) {
    		DBCollection col = database.getCollection("Internal temperature");
    		DBCursor tempEntry = col.find();
    		try {
    			if (tempEntry.hasNext())
    				return new AsyncResult<TemperatureResponse>(new TemperatureResponse(tempEntry.next()));
    		} finally {
    			tempEntry.close();
    		}
    		return null;
    }
}
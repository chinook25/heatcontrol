package heatcontrol.java;
import com.mongodb.*;
public class tmpQueryObject {
	private String type;
	private DBObject query;
	
	public void setQuery(String type, DBObject query){
		this.query = query;
		this.type = type;
	}
	
	public DBObject getQuery(){
		return query;
	}
	public String getType(){
		return type;
	}
	
}

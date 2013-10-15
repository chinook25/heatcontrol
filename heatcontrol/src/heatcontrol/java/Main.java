package heatcontrol.java;

import java.util.*;
public class Main {

	public static void main(String[] args) {
		DBController dbc = new DBController();
		dbc.run();
		ExternalInformationService eis = new ExternalInformationService();
		eis.run();
		
		
		
		
	}

}

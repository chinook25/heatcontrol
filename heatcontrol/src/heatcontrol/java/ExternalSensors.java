package heatcontrol.java;

import java.util.Random;

public class ExternalSensors {
	private int numSensors;
	
	public ExternalSensors(int num) {
		this.numSensors = num;
	}
	
	/**
	 * simulate generates, for each sensor, a temperature that is ''measured'' by the outside sensors.
	 * This temperatures lies within a range around the weather forecast min and max temperatures. The temperature of each sensor differs sligtly from the others.
	 * This is a very random and arbitrary process. Only use is showing outside temp can be used on controlling inside climate. 
	 * @return int[]
	 */
	public int[] simulate() {
		int[] temps = new int[numSensors];
		// TODO: Get min and max temp from datebase
		int min = 7;
		int max = 16;
		
		Random r = new Random(); // psuedo random number generator
		
		int currentTemp = (int)Math.round(min+(Math.random()*(min-max)));// generate random temp in range of forecast
		currentTemp += r.nextInt(10)-5;	// Change random temp so it can fall outside of forecast
		
		// for each sensor generate a random temp that lies around generated temp
		for (int i = 0; i < numSensors; i++) {
			temps[i] = currentTemp + (r.nextInt(5)-3); // measured temps lie in a range of [-2 2] around the same temperature
		}
		
		return temps;
	}
	
}
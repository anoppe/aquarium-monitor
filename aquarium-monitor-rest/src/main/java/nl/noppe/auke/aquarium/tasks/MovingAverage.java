package nl.noppe.auke.aquarium.tasks;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {

	private static final int MAX_QUEUE_SIZE = 10;
	
	private final Queue<Double> lastMeasurements;
	
	public MovingAverage() {	
		lastMeasurements = new LinkedList<>();
	}
	
	public void add(Double measurement) {
		
		if (measurement == null) {
			return;
		}
		
		if (lastMeasurements.size() >= MAX_QUEUE_SIZE) {
			lastMeasurements.poll();
		}
		
		lastMeasurements.add(measurement);
	}
	
	public Double getAverage() {
		
		Iterator<Double> iterator = lastMeasurements.iterator();
		
		Double total = 0d;
		
		while(iterator.hasNext()) {
			Double next = iterator.next();
			if (next != null) {
				total += next;
			}
		}

		return total / lastMeasurements.size();
	}
	
}

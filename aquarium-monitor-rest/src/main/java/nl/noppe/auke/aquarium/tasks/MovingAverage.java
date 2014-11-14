package nl.noppe.auke.aquarium.tasks;

public class MovingAverage {

	private volatile double average;
	private volatile int numberOfMeasurements;
	
	private static int window = 50;
	
	public MovingAverage() {	
		average = 0d;
		numberOfMeasurements = 0;
	}
	
	public void increment(double measurement) {
		numberOfMeasurements++;
		average += measurement;
	}
	
}

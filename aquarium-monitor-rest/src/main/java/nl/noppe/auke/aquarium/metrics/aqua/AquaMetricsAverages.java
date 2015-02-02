package nl.noppe.auke.aquarium.metrics.aqua;

import nl.noppe.auke.aquarium.tasks.MovingAverage;

public class AquaMetricsAverages {

	private volatile MovingAverage phMovingAverage = new MovingAverage();
	private volatile MovingAverage temperatureMovingAverage = new MovingAverage();
	private volatile MovingAverage currentMovingAverage = new MovingAverage();
	private final static Object LOCK = new Object();
	
	private Long lastSample = 0L;

	public AquaMetricsAverages() {	}
	
	public void update(AquaMetrics aquaMetrics) {
		synchronized(LOCK) {
			phMovingAverage.add(aquaMetrics.getPh());
			temperatureMovingAverage.add(aquaMetrics.getTemperature());
			currentMovingAverage.add(aquaMetrics.getCurrent());
		}
	}
	
	public AquaMetrics get() {
		synchronized(LOCK) {
			AquaMetrics aquaMetrics = new AquaMetrics();
			aquaMetrics.setPh(phMovingAverage.getAverage());
			aquaMetrics.setTemperature(temperatureMovingAverage.getAverage());
			aquaMetrics.setCurrent(currentMovingAverage.getAverage());
			
			lastSample = System.currentTimeMillis();
		
			return aquaMetrics;
		}
	}
	
	public Long getLastSample() {
		synchronized(LOCK) {
			return lastSample;
		}
	}
}

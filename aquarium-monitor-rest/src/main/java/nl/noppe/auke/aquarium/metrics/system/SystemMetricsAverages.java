package nl.noppe.auke.aquarium.metrics.system;

import nl.noppe.auke.aquarium.tasks.MovingAverage;

public class SystemMetricsAverages {

	private final MovingAverage availableSwapMovingAverage;
	private final MovingAverage cpuUtilizationMovingAverage;
	private final MovingAverage freeMemoryMovingAverage;
	private final MovingAverage usedMemoryMovingAverage;
	private final MovingAverage usedSwapMovingAverage;
	
	private Long lastSample = 0L;
	
	public SystemMetricsAverages() {
		availableSwapMovingAverage = new MovingAverage();
		cpuUtilizationMovingAverage = new MovingAverage();
		freeMemoryMovingAverage = new MovingAverage();
		usedMemoryMovingAverage = new MovingAverage();
		usedSwapMovingAverage = new MovingAverage();
	}
	
	public void update(SystemMetrics systemMetrics) {
		availableSwapMovingAverage.add(systemMetrics.getAvailableSwap().doubleValue());
		cpuUtilizationMovingAverage.add(systemMetrics.getCpuUtilization());
		freeMemoryMovingAverage.add(systemMetrics.getFreeMemory().doubleValue());
		usedMemoryMovingAverage.add(systemMetrics.getUsedMemory().doubleValue());
		usedSwapMovingAverage.add(systemMetrics.getUsedSwap().doubleValue());
		
	}
	
	public SystemMetrics get() {
		SystemMetrics systemMetrics = new SystemMetrics();
		systemMetrics.setAvailableSwap(availableSwapMovingAverage.getAverage().longValue());
		systemMetrics.setCpuUtilization(cpuUtilizationMovingAverage.getAverage());
		systemMetrics.setFreeMemory(freeMemoryMovingAverage.getAverage().longValue());
		systemMetrics.setUsedMemory(usedMemoryMovingAverage.getAverage().longValue());
		systemMetrics.setUsedSwap(usedSwapMovingAverage.getAverage().longValue());
		
		lastSample = System.currentTimeMillis();
		
		return systemMetrics;
	}

	public Long getLastSample() {
		return lastSample;
	}
}

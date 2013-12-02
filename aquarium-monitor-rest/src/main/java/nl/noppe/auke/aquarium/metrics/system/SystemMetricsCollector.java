package nl.noppe.auke.aquarium.metrics.system;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.MemoryStats;

public class SystemMetricsCollector {

	private static final Logger logger = LoggerFactory.getLogger(SystemMetricsCollector.class);
	
	private JavaSysMon monitor;
	
	@Autowired
	public void setMonitor(JavaSysMon monitor) {
		this.monitor = monitor;
	}
	
	public SystemMetrics getSystemMetrics() {
		CpuTimes initialTimes = monitor.cpuTimes();
		MemoryStats memoryStats = monitor.physical();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float cpuUsage = monitor.cpuTimes().getCpuUsage(initialTimes);
		
		logger.debug("cpu usage: {}", cpuUsage);
		logger.debug("RAM usage: {}", (memoryStats.getTotalBytes() - memoryStats.getFreeBytes()) / (1024 * 1024));
		
		SystemMetrics systemMetrics = new SystemMetrics();
		systemMetrics.setCpuUtilization(Double.parseDouble("" + cpuUsage));
		systemMetrics.setFreeMemory((memoryStats.getFreeBytes() / (1024 * 1024)));
		systemMetrics.setUsedMemory(((memoryStats.getTotalBytes() - memoryStats.getFreeBytes()) / (1024 * 1024)));
		systemMetrics.setUsedSwap(((monitor.swap().getTotalBytes() - monitor.swap().getFreeBytes()) / (1024 * 1024)));
		systemMetrics.setOccuredDatetime(new Date());
		return 	systemMetrics;
	}
}

package nl.noppe.auke.aquarium.tasks;

import nl.noppe.auke.aquarium.metrics.Metrics;
import nl.noppe.auke.aquarium.metrics.service.MetricsProcessor;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class TaskScheduler {

	private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
	
	private SystemMetricsCollector systemMetricsCollector;
	private MetricsProcessor metricsProcessor;
	
	@Autowired
	public void setSystemMetricsCollector(SystemMetricsCollector systemMetricsCollector) {
		this.systemMetricsCollector = systemMetricsCollector;
	}
	
	@Autowired
	public void setMetricsProcessor(MetricsProcessor metricsProcessor) {
		this.metricsProcessor = metricsProcessor;
	}
	
	@Scheduled(cron="*/5 * * * * ?")
	public void getSystemMetrics() {
		logger.debug("starting system metrics collector");
		Metrics metrics = systemMetricsCollector.getSystemMetrics();
		
		metricsProcessor.processSystemMetrics(metrics);
	}
}

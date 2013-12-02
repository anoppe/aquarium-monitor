package nl.noppe.auke.aquarium.tasks;

import nl.noppe.auke.aquarium.metrics.Metrics;
import nl.noppe.auke.aquarium.metrics.system.SystemMetrics;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

public class MetricsCollectScheduler {

	private static final Logger logger = LoggerFactory.getLogger(MetricsCollectScheduler.class);
	
	private SystemMetricsCollector systemMetricsCollector;
	
	SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	@Autowired
	public void setSystemMetricsCollector(SystemMetricsCollector systemMetricsCollector) {
		this.systemMetricsCollector = systemMetricsCollector;
	}
	
	
	@Scheduled(cron="*/5 * * * * ?")
	public void getSystemMetrics() {
		logger.debug("starting system metrics collector");
		Metrics metrics = systemMetricsCollector.getSystemMetrics();
		if (metrics == null) {
			metrics = new SystemMetrics();
		}
		logger.debug("Sending message to broker: " + metrics);
		messagingTemplate.convertAndSend("/queue/systemMetrics", metrics);
		
	}
	
	@Scheduled(cron="*/5 * * * * ?")
	public void printSystemMetrics() {
		
	}
}

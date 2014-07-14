package nl.noppe.auke.aquarium.tasks;

import java.util.Date;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;
import nl.noppe.auke.aquarium.metrics.aqua.AquaMetricsCollector;
import nl.noppe.auke.aquarium.metrics.system.SystemMetrics;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;
import nl.noppe.auke.aquarium.persistence.SystemMetricsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

public class MetricsCollectScheduler {

	private static final Logger logger = LoggerFactory.getLogger(MetricsCollectScheduler.class);
	
	private SystemMetricsCollector systemMetricsCollector;
	private AquaMetricsCollector aquaMetricsCollector;
	private SimpMessageSendingOperations messagingTemplate;
	private SystemMetricsRepository systemMetricsRepository;
	
	@Autowired
	public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	@Autowired
	public void setSystemMetricsRepository(SystemMetricsRepository systemMetricsRepository) {
		this.systemMetricsRepository = systemMetricsRepository;
	}
	
	@Autowired
	public void setSystemMetricsCollector(SystemMetricsCollector systemMetricsCollector) {
		this.systemMetricsCollector = systemMetricsCollector;
	}
	
	@Autowired
	public void setAquaMetricsCollector(AquaMetricsCollector aquaMetricsCollector) {
		this.aquaMetricsCollector = aquaMetricsCollector;
	}
	
	@Scheduled(cron="*/10 * * * * ?")
	public void getSystemMetrics() {
		logger.debug("starting system metrics collector");
		SystemMetrics metrics = systemMetricsCollector.getSystemMetrics();
		if (metrics == null) {
			metrics = new SystemMetrics();
		}
		logger.debug("Sending message to broker: {}", metrics);
		
		try {
			systemMetricsRepository.save(metrics);
		} catch(Throwable t) {
			logger.error("Unable to store metrics: {}", t.getMessage());
		}
		
		messagingTemplate.convertAndSend("/queue/systemMetrics", metrics);
		
	}
	
//	@Scheduled(cron="*/5 * * * * ?")
	public void getAquaMetrics() {
		logger.debug("Requesting PH from Arduino");
		Double ph = aquaMetricsCollector.getPh();
		
		AquaMetrics aquaMetrics = new AquaMetrics();
		aquaMetrics.setPh(ph);
		aquaMetrics.setOccuredDatetime(new Date());
		
		logger.debug("Sending message to broker: " + aquaMetrics);
		
		messagingTemplate.convertAndSend("/queue/aquaMetrics", aquaMetrics);
	}
	
}

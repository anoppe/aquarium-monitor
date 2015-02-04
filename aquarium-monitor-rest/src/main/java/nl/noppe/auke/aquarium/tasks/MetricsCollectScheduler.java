package nl.noppe.auke.aquarium.tasks;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;
import nl.noppe.auke.aquarium.metrics.aqua.AquaMetricsAverages;
import nl.noppe.auke.aquarium.metrics.aqua.AquaMetricsCollector;
import nl.noppe.auke.aquarium.metrics.system.SystemMetrics;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsAverages;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;
import nl.noppe.auke.aquarium.persistence.AquaMetricsRepository;
import nl.noppe.auke.aquarium.persistence.SystemMetricsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MetricsCollectScheduler {

	private static final Logger logger = LoggerFactory.getLogger(MetricsCollectScheduler.class);
	private static final Object LOCK = new Object();
	
	private static final long SAMPLE_INTERVAL = 600000L; 
	
	private SystemMetricsCollector systemMetricsCollector;
	private AquaMetricsCollector aquaMetricsCollector;
	private SimpMessageSendingOperations messagingTemplate;
	private SystemMetricsRepository systemMetricsRepository;
	private AquaMetricsRepository aquaMetricsRepository;
	
	private volatile Integer currentMoonLightState;
	private boolean isBusy = false;
	
	private final AquaMetricsAverages aquaMetricsAverages;
	private final SystemMetricsAverages systemMetricsMovingAverages;
	
	public MetricsCollectScheduler() {	
		aquaMetricsAverages = new AquaMetricsAverages();
		systemMetricsMovingAverages = new SystemMetricsAverages();
	}
	
//	@Scheduled(cron="* * * * * ?")
	public void getSystemMetrics() {
		logger.debug("starting system metrics collector");
		
		SystemMetrics metrics = systemMetricsCollector.getSystemMetrics();
		if (metrics == null) {
			metrics = new SystemMetrics();
		}
		
		systemMetricsMovingAverages.update(metrics);

		messagingTemplate.convertAndSend("/queue/systemMetrics", metrics);
		logger.debug("Sending message to broker: {}", metrics);
		if ((System.currentTimeMillis() - systemMetricsMovingAverages.getLastSample()) >= SAMPLE_INTERVAL) {
			
			SystemMetrics systemMetrics = systemMetricsMovingAverages.get();
			logger.debug("average: {}", systemMetrics);
			
			try {
				systemMetricsRepository.save(systemMetrics);
			} catch(Throwable t) {
				logger.error("Unable to store metrics: {}", t.getMessage());
			}
		}
		
		
	}
	
	@Autowired
	ObjectMapper objectMapper;
	
	Random random = new Random();
	
	@Scheduled(cron="*/2 * * * * ?")
	public void getAquaMetrics() {
		logger.debug("Requesting PH from Arduino");

		synchronized(LOCK) {
			isBusy = true;
			
//		AquaMetrics aquaMetrics = new AquaMetrics();
//		aquaMetrics.setCurrent(random.nextDouble() * 10);
//		aquaMetrics.setOccuredDatetime(new Date());
//		aquaMetrics.setPh(random.nextDouble() * 10);
//		aquaMetrics.setTemperature(random.nextDouble() * 10);
			aquaMetricsCollector.getReading(new SerialResponseCallback() {
				
				@Override
				public void callback(String data) {
					AquaMetrics aquaMetrics = null;
					try {
						aquaMetrics = objectMapper.readValue(data, AquaMetrics.class);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
					
					if (aquaMetrics == null) {
						logger.warn("No metrics data received!");
						return;
					}
					
					aquaMetrics.setOccuredDatetime(new Date());
					aquaMetrics.setCurrent(10.0d);
					currentMoonLightState = aquaMetrics.getMoonLightState();
					
					logger.debug("Sending message to broker: {}", aquaMetrics);
					
					aquaMetricsAverages.update(aquaMetrics);
					messagingTemplate.convertAndSend("/queue/aquaMetrics", aquaMetrics);
					
					if ((System.currentTimeMillis() - aquaMetricsAverages.getLastSample()) >= SAMPLE_INTERVAL) {
						
						AquaMetrics averageAquaMetrics = aquaMetricsAverages.get();
						averageAquaMetrics.setOccuredDatetime(new Date());
						
						logger.info("average {}", averageAquaMetrics);
						
						try {
							aquaMetricsRepository.save(averageAquaMetrics);
						} catch (Throwable t) {
							logger.error(t.getMessage(), t);
						}
					}
					isBusy = false;
				}
			});
		}
	}
	
	public void toggleMoonLight() {
		logger.debug("toggling' moonlight {}", currentMoonLightState);
		synchronized (LOCK) {
			while(isBusy) {
			}
			aquaMetricsCollector.toggleMoonLight();
		}
	}
	
	@Scheduled(cron="0 0 22 * * ?")
	public void turnOnMoonLight() {
		logger.debug("toggling' moonlight {}", currentMoonLightState);
		
		if (currentMoonLightState == 1) {
			logger.info("Moonlight already On.");
			return;
		}
		
		synchronized (LOCK) {
			while(isBusy) {
			}
			aquaMetricsCollector.toggleMoonLight();
		}
	}

	@Scheduled(cron="0 0 7 * * ?")
	public void turnOffMoonLight() {
		logger.debug("toggling' moonlight {}", currentMoonLightState);
		
		if (currentMoonLightState == 0) {
			logger.info("Moonlight already Off.");
			return;
		}
		
		synchronized (LOCK) {
			while(isBusy) {
			}
			aquaMetricsCollector.toggleMoonLight();
		}
	}
	
	public interface SerialResponseCallback {
		public void callback(String data);
	}
	
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
	
	@Autowired
	public void setAquaMetricsRepository(AquaMetricsRepository aquaMetricsRepository) {
		this.aquaMetricsRepository = aquaMetricsRepository;
	}
}

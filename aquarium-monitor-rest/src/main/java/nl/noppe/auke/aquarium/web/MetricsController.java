package nl.noppe.auke.aquarium.web;

import java.util.Calendar;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.system.SystemMetrics;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;
import nl.noppe.auke.aquarium.persistence.SystemMetricsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/systemMetrics")
public class MetricsController {

	private SimpMessageSendingOperations messageTemplate;
	private SystemMetricsRepository systemMetricsRepository;
	private SystemMetricsCollector systemMetricsCollector;
	
	@Autowired
	public void setMessageTemplate(SimpMessageSendingOperations messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
	
	@Autowired
	public void setSystemMetricsRepository(SystemMetricsRepository systemMetricsRepository) {
		this.systemMetricsRepository = systemMetricsRepository;
	}
	
	@Autowired
	public void setSystemMetricsCollector(SystemMetricsCollector systemMetricsCollector) {
		this.systemMetricsCollector = systemMetricsCollector;
	}
	
	@MessageMapping("/hello")
    public void greeting(String message) throws Exception {
        Thread.sleep(3000); // simulated delay
        messageTemplate.convertAndSend("/queue/greetings", message);
    }
	
	@RequestMapping(value = "/maxmemory", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Long getMaxMemory() {
		return systemMetricsCollector.getMaxMemory();
	}
	
	@RequestMapping(value="/pastHour", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SystemMetrics> getPastHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		
		return systemMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
	}
	
	@RequestMapping(value="/pastDay", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SystemMetrics> getPastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		
		return systemMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
	}
}

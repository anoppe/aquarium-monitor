package nl.noppe.auke.aquarium.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.MaxMemory;
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
	public MaxMemory getMaxMemory() {
		long maxMemory = systemMetricsCollector.getMaxMemory();
		return new MaxMemory(maxMemory);
	}
	
	@RequestMapping(value="/pastHour", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SystemMetrics> getPastHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		
		List<SystemMetrics> results = systemMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
		return results;
//		return aggregateSystemMetrics(results); 
	}
	
	@RequestMapping(value="/pastDay", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SystemMetrics> getPastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		
		List<SystemMetrics> results = systemMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
		
		return aggregateSystemMetrics(results);
	}
	
	@RequestMapping(value="/pastWeek", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SystemMetrics> getPastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		
		List<SystemMetrics> results = systemMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
		
		return aggregateSystemMetrics(results);
	}

	@RequestMapping(value="/pastMonth", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SystemMetrics> getPasMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		
		List<SystemMetrics> results = systemMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
		
		return aggregateSystemMetrics(results);
	}
	
	private List<SystemMetrics> aggregateSystemMetrics(List<SystemMetrics> results) {
		List<SystemMetrics> aggregatedResults = new ArrayList<>();
		if (results == null || results.isEmpty()) {
			return aggregatedResults;
		}
		
		Date date = results.get(0).getOccuredDatetime();
		Double cpuUtilization = 0d;
		Long freeMemory = 0l;
		Long usedMemory = 0l;
		Long usedSwap = 0l;
		long count = 0;
		
		for (SystemMetrics systemMetrics : results) {
			cpuUtilization += systemMetrics.getCpuUtilization();
			freeMemory += systemMetrics.getFreeMemory();
			usedMemory += systemMetrics.getUsedMemory();
			usedSwap += systemMetrics.getUsedSwap();
			count++;

			if ((systemMetrics.getOccuredDatetime().getTime() - date.getTime()) >= 300000) {
				SystemMetrics aggregatedSystemMetrics = new SystemMetrics();
				aggregatedSystemMetrics.setCpuUtilization((cpuUtilization / count));
				aggregatedSystemMetrics.setFreeMemory((freeMemory / count));
				aggregatedSystemMetrics.setOccuredDatetime(date);
				aggregatedSystemMetrics.setUsedMemory((usedMemory / count));
				aggregatedSystemMetrics.setUsedSwap((usedSwap / count));

				aggregatedResults.add(aggregatedSystemMetrics);
				
				date = systemMetrics.getOccuredDatetime();
				cpuUtilization = 0d;
				freeMemory = 0l;
				usedMemory = 0l;
				usedSwap = 0l;
				count = 0;
				
				continue;
			}
			
		}
		
		return aggregatedResults;
	}
}

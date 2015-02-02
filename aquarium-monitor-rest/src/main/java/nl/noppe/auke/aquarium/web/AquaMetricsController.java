package nl.noppe.auke.aquarium.web;

import java.util.Calendar;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;
import nl.noppe.auke.aquarium.persistence.AquaMetricsRepository;
import nl.noppe.auke.aquarium.tasks.MetricsCollectScheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/aquametrics")
public class AquaMetricsController {

	private static final Long HOUR_AGGREGATION = 300000l;
	private static final Long DAY_AGGREGATION = 3600000l;
	private static final Long WEEK_AGGREGATION = 21600000l;
	private static final Long MONTH_AGGREGATION = 43200000l;
	
	private MetricsCollectScheduler metricsCollectScheduler;
	private AquaMetricsRepository aquaMetricsRepository;
	
	@Autowired
	public void setAquaMetricsRepository(AquaMetricsRepository aquaMetricsRepository) {
		this.aquaMetricsRepository = aquaMetricsRepository;
	}
	
	@Autowired
	public void setMetricsCollectScheduler(MetricsCollectScheduler metricsCollectScheduler) {
		this.metricsCollectScheduler = metricsCollectScheduler;
	}
	
	@RequestMapping(value="/pastHour", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPastHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		
		return aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
	}
	
	@RequestMapping(value="/pastDay", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		
		return aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
	}
	
	@RequestMapping(value="/pastWeek", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		
		return aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
	}

	@RequestMapping(value="/pastMonth", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPasMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		
		return aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime());
	}
	
	@RequestMapping(value="/toggleMoonLight", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	@ResponseBody
	public void toggleMoonLight() {
		metricsCollectScheduler.toggleMoonLight();
	}
	
}

package nl.noppe.auke.aquarium.web;

import java.util.Calendar;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;
import nl.noppe.auke.aquarium.persistence.AquaMetricsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/aquametrics")
public class AquaMetricsController {

	private AquaMetricsRepository aquaMetricsRepository;
	
	@Autowired
	public void setAquaMetricsRepository(AquaMetricsRepository aquaMetricsRepository) {
		this.aquaMetricsRepository = aquaMetricsRepository;
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
}

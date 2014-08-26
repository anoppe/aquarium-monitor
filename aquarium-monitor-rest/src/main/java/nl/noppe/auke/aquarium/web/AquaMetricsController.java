package nl.noppe.auke.aquarium.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

	private static final Long HOUR_AGGREGATION = 300000l;
	private static final Long DAY_AGGREGATION = 3600000l;
	private static final Long WEEK_AGGREGATION = 21600000l;
	private static final Long MONTH_AGGREGATION = 43200000l;
	
	
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
		
		return aggregate(aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime()), HOUR_AGGREGATION);
	}
	
	@RequestMapping(value="/pastDay", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		
		return aggregate(aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime()), DAY_AGGREGATION);
	}
	
	@RequestMapping(value="/pastWeek", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		
		return aggregate(aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime()), WEEK_AGGREGATION);
	}

	@RequestMapping(value="/pastMonth", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AquaMetrics> getPasMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		
		return aggregate(aquaMetricsRepository.findByDateTimeGreaterThan(calendar.getTime()), MONTH_AGGREGATION);
	}
	
	private List<AquaMetrics> aggregate(List<AquaMetrics> results, Long aggregationPeriod) {
		List<AquaMetrics> aggregatedMetrics = new ArrayList<>();
		
		if (results == null || results.isEmpty()) {
			return aggregatedMetrics;
		}
		
		Date date = results.get(0).getOccuredDatetime();
		Double ph = 0d;
		Double temperature = 0d;
		long count = 0;
		
		for (AquaMetrics aquaMetric : results) {
			ph += aquaMetric.getPh();
			temperature += aquaMetric.getTemperature();
			
			count++;
			if ((aquaMetric.getOccuredDatetime().getTime() - date.getTime()) >= aggregationPeriod) {
				AquaMetrics aggregatedAquaMetrics = new AquaMetrics();
				aggregatedAquaMetrics.setOccuredDatetime(date);
				aggregatedAquaMetrics.setTemperature(temperature / count);
				aggregatedAquaMetrics.setPh(ph / count);
				aggregatedMetrics.add(aggregatedAquaMetrics);
				
				date = aquaMetric.getOccuredDatetime();
				ph = 0d;
				temperature = 0d;
				count = 0;
				
				continue;
			}
			
		}
		
		return aggregatedMetrics;
	}
}

package nl.noppe.auke.aquarium.metrics.service;

import nl.noppe.auke.aquarium.metrics.Metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;

public class MetricsProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MetricsProcessor.class);
	
	@CachePut(value="systemMetrics", key="#metrics.occuredDatetime")
	public Metrics processSystemMetrics(Metrics metrics) {
		
		logger.debug("TODO: add received metrics object to objectsToPersistQueue");

		return metrics;
	}
}

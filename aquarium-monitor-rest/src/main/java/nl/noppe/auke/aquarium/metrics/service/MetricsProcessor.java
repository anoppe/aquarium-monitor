package nl.noppe.auke.aquarium.metrics.service;

import nl.noppe.auke.aquarium.metrics.Metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MetricsProcessor.class);
	
	public Metrics processSystemMetrics(Metrics metrics) {
		
		logger.debug("TODO: add received metrics object to objectsToPersistQueue");

		return metrics;
	}
	
}

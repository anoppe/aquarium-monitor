package nl.noppe.auke.aquarium.config;

import nl.noppe.auke.aquarium.metrics.service.MetricsProcessor;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AquariumServiceConfiguration {
	
	@Bean
	public MetricsProcessor metricsProcessor() {
		return new MetricsProcessor();
	}
	
}

package nl.noppe.auke.aquarium;

import java.util.Arrays;

import nl.noppe.auke.aquarium.metrics.service.MetricsProcessor;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AquariumServiceConfiguration {
	
	@Bean
	public MetricsProcessor metricsProcessor() {
		return new MetricsProcessor();
	}
	
	@Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("systemMetrics")));
        return cacheManager;
    }
}

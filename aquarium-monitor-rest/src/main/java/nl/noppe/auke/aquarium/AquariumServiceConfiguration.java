package nl.noppe.auke.aquarium;

import nl.noppe.auke.aquarium.metrics.service.MetricsProcessor;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
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
    public CacheManager cacheManager() throws Exception {
		SpringEmbeddedCacheManagerFactoryBean cacheManagerFactoryBean = new SpringEmbeddedCacheManagerFactoryBean();

		SpringEmbeddedCacheManager embeddedCacheManager = cacheManagerFactoryBean.getObject();
		EmbeddedCacheManager cacheManager = embeddedCacheManager.getNativeCacheManager();
		org.infinispan.configuration.cache.Configuration dcc = cacheManager.getDefaultCacheConfiguration();
		
		org.infinispan.configuration.cache.Configuration c = new ConfigurationBuilder().read(dcc).build();
		 
		cacheManager.defineConfiguration("systemMetrics", c);
		
		return embeddedCacheManager; 
    }
}

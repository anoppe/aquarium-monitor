package nl.noppe.auke.aquarium.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages="nl.noppe.auke.aquarium.persistence")
public class MongoConfiguration extends AbstractMongoConfiguration {
	
	@Override
	protected String getMappingBasePackage() {
		return "nl.noppe.auke.aquarium.metrics";
	}
	
	@Bean(name="mongoTemplate")
	public MongoTemplate systemMetricsMongoTemplate(Mongo mongo) throws UnknownHostException {
		return new MongoTemplate(mongo, "system_metrics");
	}
	
	@Bean
	public Mongo mongo() throws UnknownHostException {
		return new MongoClient("192.168.1.23");
	}

	@Override
	protected String getDatabaseName() {
		return "sysem_metrics";
	}
}

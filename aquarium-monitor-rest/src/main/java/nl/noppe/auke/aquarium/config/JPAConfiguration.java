package nl.noppe.auke.aquarium.config;


import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;
import nl.noppe.auke.aquarium.metrics.system.SystemMetrics;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages="nl.noppe.auke.aquarium.persistence")
@EntityScan(basePackageClasses={AquaMetrics.class, SystemMetrics.class})
public class JPAConfiguration {
	
}

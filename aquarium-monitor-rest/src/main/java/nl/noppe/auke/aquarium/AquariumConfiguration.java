package nl.noppe.auke.aquarium;

import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;
import nl.noppe.auke.aquarium.tasks.TaskScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jezhumble.javasysmon.JavaSysMon;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages={"nl.noppe.auke.aquarium.tasks", "nl.noppe.auke.aquarium.metrics"})
public class AquariumConfiguration {

	public static void main(String[] args) {
		
		SpringApplication springApplication = new SpringApplication(AquariumConfiguration.class, AquariumServiceConfiguration.class);
		springApplication.run();
		
//		SpringApplication.run(new Object[] {AquariumConfiguration.class, AquariumServiceConfiguration.class});
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new TaskScheduler();
	}
	
	@Bean
	public SystemMetricsCollector systemMetricsCollector() {
		return new SystemMetricsCollector();
	}
	
	@Bean
	public JavaSysMon monitor() {
		return new JavaSysMon();
	}
	
}

package nl.noppe.auke.aquarium.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetricsCollector;
import nl.noppe.auke.aquarium.metrics.service.MetricsProcessor;
import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;
import nl.noppe.auke.aquarium.tasks.MetricsCollectScheduler;
import nl.noppe.auke.aquarium.web.MetricsController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jezhumble.javasysmon.JavaSysMon;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackageClasses={MetricsCollectScheduler.class, MetricsController.class, AquariumConfiguration.class})
public class AquariumConfiguration implements SchedulingConfigurer {

	public static void main(String[] args) {
		
		SpringApplication springApplication = new SpringApplication(AquariumConfiguration.class);
		springApplication.run();
		
//		SpringApplication.run(new Object[] {AquariumConfiguration.class, AquariumServiceConfiguration.class});
	}
	
	@Bean
	public MetricsProcessor metricsProcessor() {
		return new MetricsProcessor();
	}
	
	@Bean
	public MetricsCollectScheduler metricsCollectScheduler() {
		return new MetricsCollectScheduler();
	}
	
	@Bean
	public SystemMetricsCollector systemMetricsCollector() {
		return new SystemMetricsCollector();
	}
	
	@Bean
	public AquaMetricsCollector aquaMetricsCollector() {
		AquaMetricsCollector aquaMetricsCollector = new AquaMetricsCollector();
		return aquaMetricsCollector;
	}
	
	@PostConstruct
	public void initializeSerial() {
		aquaMetricsCollector().initialize();
	}
	
	@Bean
	public JavaSysMon monitor() {
		return new JavaSysMon();
	}
	
	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(5);
    }
    
//    @Bean
//    public EmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
//    	TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//    	factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[] {
//    			tomcatContextCustomizer()
//    	}));
//    	return factory;
//    }
//
//    @Bean 
//    public TomcatContextCustomizer tomcatContextCustomizer() {
//    	return new TomcatContextCustomizer() {
//    		@Override
//    		public void customize(Context context) {
//    			context.addServletContainerInitializer(new WsSci(), null);
//    		}
//    	};
//    }
	
}

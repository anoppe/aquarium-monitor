package nl.noppe.auke.aquarium.config;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import nl.noppe.auke.aquarium.metrics.system.SystemMetricsCollector;
import nl.noppe.auke.aquarium.tasks.MetricsCollectScheduler;
import nl.noppe.auke.aquarium.web.MetricsController;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.jezhumble.javasysmon.JavaSysMon;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackageClasses={MetricsCollectScheduler.class, MetricsController.class, AquariumConfiguration.class})
public class AquariumConfiguration implements SchedulingConfigurer {

	public static void main(String[] args) {
		
		SpringApplication springApplication = new SpringApplication(AquariumConfiguration.class, AquariumServiceConfiguration.class);
		springApplication.run();
		
//		SpringApplication.run(new Object[] {AquariumConfiguration.class, AquariumServiceConfiguration.class});
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
    
    @Bean
    public EmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
    	TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
    	factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[] {
    			tomcatContextCustomizer()
    	}));
    	return factory;
    }

    @Bean 
    public TomcatContextCustomizer tomcatContextCustomizer() {
    	return new TomcatContextCustomizer() {
    		@Override
    		public void customize(Context context) {
    			context.addServletContainerInitializer(new WsSci(), null);
    		}
    	};
    }
	
}
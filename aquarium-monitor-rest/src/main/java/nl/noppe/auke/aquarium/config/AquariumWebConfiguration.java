package nl.noppe.auke.aquarium.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
public class AquariumWebConfiguration extends WebMvcConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AquariumWebConfiguration.class);
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
//		registry.addResourceHandler("/scripts/**", "/fonts/**").addResourceLocations(DummyClass.class.getClassLoader().getResource("classpath:META-INF/resources/").toExternalForm());
	}
	
	
}

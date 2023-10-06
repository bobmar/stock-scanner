package org.rhm.stock;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class StockConfig implements WebMvcConfigurer {
/**
 * THIS IS NEEDED ONLY FOR TESTING! SHOULD NOT BE DEPLOYED IN ANY PRODUCTION ENVIRONMENT.
 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**");
	}

}

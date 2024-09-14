package com.algaworks.algafood.core.security;
// Fonte: https://spring.io/blog/2015/06/08/cors-support-in-spring-framework#filter-based-cors-support
// https://gist.github.com/alexaugustobr/3222a12dfa34c22cd7bc7b6d089e3ced#file-corsconfig-java

import java.util.Collections;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Filtro para configurar CORS no Autorization Server,
 * para que OPTIONS possa ser chamado em /oauth/token.
 */
@Configuration
public class CorsConfig {

	/**
	 * 
	 * "org.springframework.beans.factory.BeanCreationException: 
	 * Error creating bean with name 'springSecurityFilterChain' 
	 * defined in class path resource 
	 * [org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.class]: 
	 * Bean instantiation via factory method failed; 
	 * nested exception is org.springframework.beans.BeanInstantiationException: 
	 * Failed to instantiate [javax.servlet.Filter]: 
	 * Factory method 'springSecurityFilterChain' threw exception; 
	 * nested exception is org.springframework.beans.factory.BeanNotOfRequiredTypeException: 
	 * Bean named 'corsFilter' is expected to be of type 'org.springframework.web.filter.CorsFilter' 
	 * but was actually of type 'org.springframework.boot.web.servlet.FilterRegistrationBean'"
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false);
		config.setAllowedOrigins(Collections.singletonList("*"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return bean;
	}

}
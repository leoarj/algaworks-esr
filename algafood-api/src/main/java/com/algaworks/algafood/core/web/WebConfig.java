package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe para configurar globlalmente o CORS na aplicação.<br>
 * Deve implementar {@link WebMvcConfigurer} para configurar métodos de callback do Spring MVC.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private ApiDeprecationHandler apiDeprecationHandler;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // habilita para qualquer caminho
			.allowedMethods("*"); // habilita para todos os verbos HTTP
//			.allowedOrigins("*") // habilita para qualquer origem (padrão)
//			.maxAge(30); // define o tempo de vida (em segundos) do cache preflight para os clients
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiDeprecationHandler);
	}
	
	/**
	 * Bean para Shallow ETag (ETag simples), utilizando implementação do Spring.
	 */
	@Bean
	public Filter shallowEtagFilter() {
		return new ShallowEtagHeaderFilter();
	}
}

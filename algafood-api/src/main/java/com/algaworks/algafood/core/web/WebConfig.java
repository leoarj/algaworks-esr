package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe para configurar globlalmente o CORS na aplicação.<br>
 * Deve implementar {@link WebMvcConfigurer} para configurar métodos de callback do Spring MVC.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // habilita para qualquer caminho
			.allowedMethods("*"); // habilita para todos os verbos HTTP
//			.allowedOrigins("*") // habilita para qualquer origem (padrão)
//			.maxAge(30); // define o tempo de vida (em segundos) do cache preflight para os clients
	}
	
	/**
	 * Para definir a versão padrão caso não seja especificadoo Accept na requisição.
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(AlgaMediaTypes.V2_APPLICATION_JSON);
	}
	
	/**
	 * Bean para Shallow ETag (ETag simples), utilizando implementação do Spring.
	 */
	@Bean
	public Filter shallowEtagFilter() {
		return new ShallowEtagHeaderFilter();
	}
}

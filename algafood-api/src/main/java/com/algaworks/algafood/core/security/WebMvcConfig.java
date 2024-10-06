package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Customiza métodos de callback para configurações do Spring MVC.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Registra para o controlador padrão "/login" qual resposta será renderizada
		// no caso a página login.html (resources/templates/pages).
		registry.addViewController("/login").setViewName("pages/login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	
}

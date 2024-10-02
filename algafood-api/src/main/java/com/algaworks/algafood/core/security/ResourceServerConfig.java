package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configurações de segurança do Resource Server.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig {

	/**
	 * Configura cadeia de filtros de segurança para o Resource Server.
	 * Como autenticações de endpoints e tipo/verificação de token.
	 */
	@Bean
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/oauth2/**").authenticated()
			.and()
			.csrf().disable()
			.cors().and()
			.oauth2ResourceServer().jwt();
		
		return http
				.formLogin(Customizer.withDefaults())
				.build();
	}
	
}

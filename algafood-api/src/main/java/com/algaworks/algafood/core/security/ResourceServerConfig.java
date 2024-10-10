package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configurações de segurança do Resource Server.
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig {

	/**
	 * Configura cadeia de filtros de segurança para o Resource Server.
	 * Como autenticações de endpoints e tipo/verificação de token.
	 */
	@Bean
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(Customizer.withDefaults())
			.csrf().disable()
			.cors().and()
			.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
		
		return http.build();
	}
	
	/**
	 * Configura um conversor de token JWT para ter acesso as authorities
	 * (claims personalizadas) para o Resourcer Server saber como ter acesso
	 * as Granted Authorities presentes no token, para conseguir verificar
	 * a autorização de acesso aos recursos protegidos.
	 */
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		
		/*
		 * Configura lógica de conversão.
		 * No caso mantendo a mesma estrutura do token,
		 * porém recuperando as authorities personalizadas e
		 * combinando com os escopos presentes. 
		 */
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			// Recupera scopes
			JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);
			
			// Recupera authorities
			List<String> authorities = jwt.getClaimAsStringList("authorities");
			
			if (authorities == null) {
				// Se não houverem authorities,
				// retorna apenas os scopes (sufuciente para Client Credentials)
				return grantedAuthorities;
			}
			
			grantedAuthorities.addAll(authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
			
			return grantedAuthorities;
		});
		
		return converter;
	}
	
}

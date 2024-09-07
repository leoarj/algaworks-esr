package com.algaworks.algafood.core.security;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * Componente para configurar a segurança na API<br>
 * Obs.: Utilizando classes depreciadas, porém será migrado para versões atualizadas do Spring.
 * 
 * */
@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				//.anyRequest().authenticated() // restringe qualquer outro endpoint a ter somente acesso com autenticação
				.antMatchers(HttpMethod.POST, "/v1/cozinhas/**").hasAnyAuthority("EDITAR_COZINHAS")
				.antMatchers(HttpMethod.PUT, "/v1/cozinhas/**").hasAnyAuthority("EDITAR_COZINHAS")
				.antMatchers(HttpMethod.GET, "/v1/cozinhas/**").hasAnyAuthority("CONSULTAR_COZINHAS")
				.anyRequest().denyAll()
			.and()
			.cors().and() // Configura CORS (para que chamada com OPTIONS não seja impedida por navegadores)
			//.oauth2ResourceServer().opaqueToken();
			.oauth2ResourceServer().jwt() // configura para usar tokens transparentes JWT
			.jwtAuthenticationConverter(jwtAuthenticationConverter());
	}
	
	/**
	 * Retorna uma conversor de autenticação JWT, dizendo como extrair as authorities<br>
	 * da autenticação para a configuração de segurança do resource server.
	 */
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			var authorities = jwt.getClaimAsStringList("authorities");
			
			if (authorities == null) {
				authorities = Collections.emptyList();
			}
			
			return authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		});
		
		return jwtAuthenticationConverter;
	}
	
	/**
	 * Expôe um bean referente ao decodificador de token JWT,<br>
	 * especificando a chave secreta e o algoritmo.<br>
	 * Obs.: Deve ser a mesma chave do Authorization Server.
	 */
//	@Bean
//	public JwtDecoder jwtDecoder() {
//		var secretKey = new SecretKeySpec("iHrwgwjuSgBNwIhT5vl7Syfxtr1GsKAR".getBytes(), "HmacSHA256");
//		
//		return NimbusJwtDecoder.withSecretKey(secretKey).build();
//	}
}

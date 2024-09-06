package com.algaworks.algafood.core.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

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
				.anyRequest().authenticated() // restringe qualquer outro endpoint a ter somente acesso com autenticação
				
			.and()
			.cors().and() // Configura CORS (para que chamada com OPTIONS não seja impedida por navegadores)
			//.oauth2ResourceServer().opaqueToken();
			.oauth2ResourceServer().jwt(); // configura para usar tokens transparentes JWT
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

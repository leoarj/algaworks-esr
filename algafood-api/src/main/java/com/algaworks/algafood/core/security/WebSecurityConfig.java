package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Componente para configurar a segurança na API<br>
 * Obs.: Utilizando classes depreciadas, porém será migrado para versões atualizadas do Spring.
 * 
 * */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication() // configurando autenticação em memória (testes)
			.withUser("leandro")
				.password(passwordEncoder().encode("123"))
				.roles("ADMNIN")
			.and()
			.withUser("joao")
				.password(passwordEncoder().encode("123"))
				.roles("ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
			
			.and()
			
			.authorizeRequests()
				.antMatchers("/v1/cozinhas/**").permitAll() // torna recurso de cozinhas público
				.anyRequest().authenticated() // restringe qualquer outro endpoint a ter somente acesso com autenticação
				
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // para não guardar estado de sessão
				
			.and()
				.csrf().disable(); // desabilitar o Cross-site Request Forgery (evitar transporte de sessão)
	}
	
	/**
	 * Para gerar o bean referente a codificação da senha.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

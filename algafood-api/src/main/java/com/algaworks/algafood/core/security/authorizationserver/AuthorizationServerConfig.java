package com.algaworks.algafood.core.security.authorizationserver;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração para beans referentes ao Authorization Server.
 */
@Configuration
public class AuthorizationServerConfig {

	/**
	 * Registra bean para configuração de filtros de segurança para requisições HTTP.
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE) // para ter precedência antes de filters do Resource Server
	public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
		// Configura cadeia de filtros de segurança padrão
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.build();
	}
	
	/**
	 * Registra um bean para indicar o url do provedor de configurações,
	 * provida via configuração definida via propriedades.
	 */
	@Bean
	public ProviderSettings providerSettings(AlgaFoodSecurityProperties properties) {
		return ProviderSettings.builder()
				.issuer(properties.getProviderUrl())
				.build();
	}
	
	/**
	 * Registra um bean para indicar o repositório de clients que interagem com o servidor.
	 * Expôe os clients registrados e configurações de token (opaco, transparente).
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
		RegisteredClient algafoodbackend = RegisteredClient
				.withId("1")
				.clientId("algafood-backend")
				.clientSecret(passwordEncoder.encode("backend123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope("READ")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
						.accessTokenTimeToLive(Duration.ofMinutes(30))
						.build())
				.build();
		
		return new InMemoryRegisteredClientRepository(Arrays.asList(algafoodbackend)); 
	}
	
}

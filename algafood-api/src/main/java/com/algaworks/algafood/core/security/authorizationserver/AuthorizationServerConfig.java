package com.algaworks.algafood.core.security.authorizationserver;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

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
		return http
				.formLogin(Customizer.withDefaults())
				.build();
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
		
		// Clients com fluxo Client Credentials
		RegisteredClient algafoodbackend = RegisteredClient
				.withId("1")
				.clientId("algafood-backend")
				.clientSecret(passwordEncoder.encode("backend123"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope("READ")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.accessTokenTimeToLive(Duration.ofMinutes(30))
						.build())
				.build();
		
		// Clients com fluxo Authorization Code
		RegisteredClient algafoodWeb = RegisteredClient
                .withId("2")
                .clientId("algafood-web")
                .clientSecret(passwordEncoder.encode("web123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("READ")
                .scope("WRITE")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .reuseRefreshTokens(false)
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .build())
                .redirectUri("http://127.0.0.1:8080/authorized")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();
		
		RegisteredClient foodanalytics = RegisteredClient
                .withId("3")
                .clientId("foodanalytics")
                .clientSecret(passwordEncoder.encode("web123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("READ")
                .scope("WRITE")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .build())
                .redirectUri("http://www.foodanalytics.local:8082")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();
		
		return new InMemoryRegisteredClientRepository(
				Arrays.asList(
						algafoodbackend, algafoodWeb, foodanalytics)
				); 
	}
	
	/**
	 * Registra um bean responsavel por gerenciar as autorizações.
	 * Recebe injeção de um JdbcOperations para repassar a implementação
	 * JdbcOAuth2AuthorizationService, para manipular os dados na tabela de autorizações,
	 * além da injeção do repositório de clients da API registrados.
	 */
	@Bean
	public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations,
			RegisteredClientRepository registeredClientRepository) {
		
		return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
	}
	
	/**
	 * Registra um bean para obtenção de JWK (JSON Web Keys),
	 * configurando um JWKSet a partir de um JKS (Java Key Store),
	 * obtendo as respectivas chaves privada e pública.
	 */
	@Bean
	public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception {
		char[] keyStorePass = properties.getPassword().toCharArray();
		String keyPairAlias = properties.getKeypairAlias();
		
		// Localizaçã do recurso JSK (No caso encodado em Base64 e tratado por protocol resolver)
		Resource jksLocation = properties.getJksLocation();
		// Obtém o InputStream do JKS (necessário para carregar em um objeto KeyStore)
		InputStream inputStream = jksLocation.getInputStream();
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, keyStorePass);
		
		// Uma vez obtido o objeto KeyStore, criar a chave RSA
		RSAKey rsaKey = RSAKey.load(keyStore, keyPairAlias, keyStorePass);
		
		// Com a chave RSA criada, retornar um objeto JWKSet
		return new ImmutableJWKSet<>(new JWKSet(rsaKey));
	}
	
	/**
	 * Registra bean para personalizar contexto de encode do token JWT,
	 * permitindo dessa forma adicionar propriedades customizadas.
	 * Deve buscar o usuário do banco de dados, devido alteração realizada na class {@link JpaUserDetailsService}
	 * para evitar problemas de serialização.
	 * Adiciona claims (afirmações/reinvindicações) customizadas como id do usuário por exemplo.
	 */
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UsuarioRepository usuarioRepository) {
		return context -> {
			// Retorna autenticação representando o resource-owner ou client
			Authentication authentication = context.getPrincipal();
			
			// Se a identificação a autenticação é uma instância de User do Spring Security
			if (authentication.getPrincipal() instanceof User) {
				User user = (User) authentication.getPrincipal();
				
				// Recupera usuário do banco de dados de acordo com a identidade da autenticação
				Usuario usuario = usuarioRepository.findByEmail(user.getUsername()).orElseThrow();
				
				// Recupera as authorities transformadas no JpaUserDetailsService
				// e repassa para um Set de String para incluir nas claims
				Set<String> authorities = new HashSet<>();
				for (GrantedAuthority authority : user.getAuthorities()) {
					authorities.add(authority.getAuthority());
				}
				
				context.getClaims().claim("usuario_id", usuario.getId().toString());
				context.getClaims().claim("authorities", authorities);
			}
		};
	}
}

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
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
//		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		applyDefaultSecurityWithCustomConsentPage(http);
		
		return http
				.formLogin(customizer -> customizer.loginPage("/login"))
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
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder,
			JdbcOperations jdbcOperations) {
		
		return new JdbcRegisteredClientRepository(jdbcOperations);
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
	
	/**
	 * Configura um bean para o serviço de autorização de consentimentos,
	 * para armazenamento dos consentimentos já concedidos.
	 * @implNote Armazenando no banco de dados via {@link JdbcOAuth2AuthorizationConsentService}
	 */
	@Bean
	public OAuth2AuthorizationConsentService consentService(JdbcOperations jdbcOperations,
			RegisteredClientRepository clientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, clientRepository);
	}
	
	/**
	 * Expôe o bean referente a consulta de autorizações concedidas a clients,
	 * que estão armazenadas no banco de dados.
	 * A consulta fornecida por esse serviço será utilizada para exibir os clients
	 * com autorizações concedidas em uma página personalizada.
	 */
	@Bean
	public OAuth2AuthorizationQueryService oAuth2AuthorizationQueryService(JdbcOperations jdbcOperations) {
		return new JdbcOAuth2AuthorizationQueryService(jdbcOperations);
	}
	
	/**
	 * Customiza configuração padrão de segurança {@code applyDefaultSecurity(HttpSecurity http)}
	 * de {@link OAuth2AuthorizationServerConfiguration}
	 * definindo uma página personalizada de consentimento.
	 * @see {@link OAuth2AuthorizationServerConfiguration}
	 */
	private void applyDefaultSecurityWithCustomConsentPage(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer<>();

        authorizationServerConfigurer.authorizationEndpoint(
                customizer -> customizer.consentPage("/oauth2/consent"));

        RequestMatcher endpointsMatcher = authorizationServerConfigurer
                .getEndpointsMatcher();

        http.requestMatcher(endpointsMatcher)
            .authorizeRequests(authorizeRequests ->
                    authorizeRequests.anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
            .apply(authorizationServerConfigurer);
	}
}

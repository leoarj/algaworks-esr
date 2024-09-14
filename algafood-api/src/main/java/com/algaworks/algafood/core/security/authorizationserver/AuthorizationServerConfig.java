package com.algaworks.algafood.core.security.authorizationserver;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// Agora injetando de JpaUserDetailsService
	@Autowired
	private UserDetailsService userDetailsService;
	
	// Para ter acesso as configurações de conexão do Redis (no application.properties).
//	@Autowired
//	private RedisConnectionFactory redisConnectionFactory;
	
	@Autowired
	private JwtKeyStoreProperties jwtKeyStoreProperties;
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource);
//			.inMemory()
//				.withClient("algafood-web")
//				.secret(passwordEncoder.encode("web123"))
//				.authorizedGrantTypes("password", "refresh_token")
//				.scopes("WRITE", "READ")
//				.accessTokenValiditySeconds(60 * 60 * 6) // 6 horas (padrão é 12 horas)
//				//.accessTokenValiditySeconds(15) // 15s para teste
//				.refreshTokenValiditySeconds(60 * 24 * 60 * 60) // 60 dias
//			.and()
//				.withClient("faturamento")
//				.secret(passwordEncoder.encode("faturamento123"))
//				.authorizedGrantTypes("client_credentials")
//				.scopes("WRITE", "READ")
//			.and()
//				.withClient("foodanalytics")
//				.secret(passwordEncoder.encode("food123"))
//				.authorizedGrantTypes("authorization_code")
//				.scopes("WRITE", "READ")
//				.redirectUris("http://www.foodanalytics.local:8082")
//			.and()
//				.withClient("foodanalytics-pkce")
//				.secret(passwordEncoder.encode(""))
//				.authorizedGrantTypes("authorization_code")
//				.scopes("WRITE", "READ")
//				.redirectUris("http://www.foodanalytics.local:8082")
//			.and()
//				.withClient("webadmin")
//				.authorizedGrantTypes("implicit")
//				.scopes("WRITE", "READ")
//				.redirectUris("http://aplicacao-cliente")
//			.and()
//				.withClient("checktoken")
//				.secret(passwordEncoder.encode("check123"));
	}
	
	/**
	 * Para configurar quem pode verificar a validade de um token.
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//security.checkTokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()")
		.tokenKeyAccess("permitAll()") // habilita acesso para endpoint de obtenção da chave pública
		.allowFormAuthenticationForClients(); // para permitir autenticação por url-encoded
	}
	
	/**
	 * Para repassar o Authentication Manager para a configuração de autorização dos endpoints<br>
	 * para o Authorization Server validar o usuário/senha finais.<br>
	 * Necessário apenas no fluxo "password credentials".
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// Passa cadeia de modificadores (melhoradores) de token
		var enhancerChaim = new TokenEnhancerChain();
		enhancerChaim.setTokenEnhancers(
				Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter()));
		
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.reuseRefreshTokens(false)
//			.tokenStore(redisTokenStore()) // configura armazenamento de tokens para o Redis
			.accessTokenConverter(jwtAccessTokenConverter()) // configura conversor de Token JWT
			.tokenEnhancer(enhancerChaim) // cadeia de modificadores de token
			.approvalStore(approvalStore(endpoints.getTokenStore()))
			.tokenGranter(tokenGranter(endpoints));
	}
	
	/**
	 * Substitui handler padrão após ter configurado JWT no authorization server<br>
	 * para voltar opções de poder escolher os escopos a aprovar/desaprovar na autenticação.
	 */
	private ApprovalStore approvalStore(TokenStore tokenStore) {
		var approvalStore = new TokenApprovalStore();
		approvalStore.setTokenStore(tokenStore);
		
		return approvalStore;
	}
	
	/**
	 * Retorna um Token Store com implementação para Redis.
	 */
//	private TokenStore redisTokenStore() {
//		return new RedisTokenStore(redisConnectionFactory);
//	}
	
	/**
	 * Registra um bean que representa o conjunto de chaves criptográficas disponíveis
	 * e suas propriedades, conforme a especificação JWKS.
	 */
	@Bean
	public JWKSet jwkSet() {
		RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic())
				.keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.RS256)
				.keyID("algafood-key-id"); // Configurando apenas uma chave no nomento
		
		return new JWKSet(builder.build());
	}
	
	/**
	 * Registra um bean para um conversor de token JWT, usando chave simétrica.
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		// Configuração simétrica com tokens opacos.
//		jwtAccessTokenConverter.setSigningKey("iHrwgwjuSgBNwIhT5vl7Syfxtr1GsKAR");
		
		// Configura o conversor de token JWT para usar um par de chaves RSA assimétrico.
		jwtAccessTokenConverter.setKeyPair(keyPair());
		
		return jwtAccessTokenConverter;
	}
	
	/**
	 * Para padronizar código de acesso ao par de chaves criptográficas.
	 */
	private KeyPair keyPair() {
		// Obtém recurso do keystore a partir dos resources
//		var jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());
		var jksResource = jwtKeyStoreProperties.getJkLocation();
		// Senha utilizada para criptografia do arquivo/chaves
		var keyStorePass = jwtKeyStoreProperties.getPassword();
		// Nome do conjunto das chaves no keystore
		var keyPairAlias = jwtKeyStoreProperties.getKeypairAlias();
		
		// Cria a fábrica para pares de chaves, a partir de um keytore JKS.
		// Para obter os pares contidos nele.
		var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, keyStorePass.toCharArray());
		// Obtém o par de acordo com o alias correspondente.
		return keyStoreKeyFactory.getKeyPair(keyPairAlias);
	}
	
	/**
	 * Para adicionar suporte a PKCE no Authorization Server.
	 */
	private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
				endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory());
		
		var granters = Arrays.asList(
				pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
		
		return new CompositeTokenGranter(granters);
	}
}

package com.algaworks.algafood.core.security.authorizationserver;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe para propriedades de configuração do Authorization Server.
 */
@Component
@Getter
@Setter
@Validated
@ConfigurationProperties("algafood.auth")
public class AlgaFoodSecurityProperties {

	@NotBlank
	private String providerUrl;
	
}

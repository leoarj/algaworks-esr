package com.algaworks.algafood.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.http.MediaType;

/**
 * Componente para que no HAL sejam aceitos media types (vnd.empresa.versao) customizados<br>
 * e a representação dos recursos continuem na especificação.
 */
@Configuration
public class AlgaHalConfiguration {

	@Bean
	public HalConfiguration globalPolicy() {
		return new HalConfiguration()
				.withMediaType(MediaType.APPLICATION_JSON)
				.withMediaType(AlgaMediaTypes.V1_APPLICATION_JSON);
	}
}

package com.algaworks.algafood.core.springdoc;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

/**
 * Componente para configurações customizadas para o SpringDoc.
 */
@Validated
@Component
@ConfigurationProperties("springdoc.custom")
public class SpringDocCustomProperties {
	
	@NotNull
	private List<String> resourceServersUrls;

	/**
	 * @return Lista dos servidores configurados, que serão exibidos na interface do Swagger UI, drop-dow "servers".
	 */
	public List<String> getResourceServersUrls() {
		return resourceServersUrls;
	}

	public void setResourceServersUrls(List<String> resourceServersUrls) {
		this.resourceServersUrls = resourceServersUrls;
	}
}

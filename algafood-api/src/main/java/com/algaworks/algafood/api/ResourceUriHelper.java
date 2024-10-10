package com.algaworks.algafood.api;

import java.net.URI;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.experimental.UtilityClass;

/**
 * Classe utilitária para incluir o header {@code location} na resposta
 * com a URI do recurso que acabou de ser criado.<br>
 * Esse componente utilitário deve ser chamado nos endpoints de criação de recursos.
 */
@UtilityClass // marca a classe como utilitária
public class ResourceUriHelper {

	/**
	 * - Constrói a URI a partir da requisição corrente.<br>
	 * - Obtém o servlet da resposta.<br>
	 * - Injeta o header {@code location} com a URI do recurso na resposta. 
	 */
	public static void addUriInResponseHeader(Object resourceId) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(resourceId).toUri();
		
		HttpServletResponse response = ((ServletRequestAttributes)
				RequestContextHolder.getRequestAttributes()).getResponse();
		
		response.setHeader(HttpHeaders.LOCATION, uri.toString());
	}
}

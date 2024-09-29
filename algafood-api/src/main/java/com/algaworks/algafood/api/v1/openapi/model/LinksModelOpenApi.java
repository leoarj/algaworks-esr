package com.algaworks.algafood.api.v1.openapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinksModelOpenApi {

	private LinkModel rel;
	
	@Getter
	@Setter
	private class LinkModel {
		@Schema(example = "http://api.algafood.com.br/v1/recurso/id")
		private String href;
		@Schema(description = "Se é um link que aceita parâmetros adicionais de requisição")
		private boolean templated;
	}	
}
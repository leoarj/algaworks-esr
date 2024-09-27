package com.algaworks.algafood.api.v2.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v2.model.CidadeModelV2;

import lombok.Data;

/**
 * Esta classe é um substituto para o objeto _embedded na documentação OpenApi.<br>
 * Corrige a geração dos objetos e arrays de objetos que estavam de forma indevida na documentação.<br>
 * Modela retorno de coleção de cidades na documentação de acordo com o formato real retornado pela API.
 */
//@ApiModel("CidadesModel")
@Data
public class CidadesModelV2OpenApi {

	private CidadeEmbeddedModelOpenApi _embedded;
	private Links _links;
	
//	@ApiModel("CidadesEmbeddedModel")
	@Data
	public class CidadeEmbeddedModelOpenApi {
		private List<CidadeModelV2> cidades;
	}
	
}

package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.EstadoModel;

import lombok.Data;

//@ApiModel("EstadosModel")
@Data
public class EstadosModelOpenApi {

	private EstadoEmbeddedModelOpenApi _embedded;
	private Links _links;
	
//	@ApiModel("EstadosEmbeddedModel")
	@Data
	public class EstadoEmbeddedModelOpenApi {
		private List<EstadoModel> estados;
	}
	
}

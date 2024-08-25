package com.algaworks.algafood.api.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.model.RestauranteApenasNomeModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;

//@ApiModel("RestaurantesApenasNomeModel")
@Data
public class RestaurantesApenasNomeModelOpenApi {

	private RestaurantesApenasNomeEmbeddedModelOpenApi _embedded;
	private Links _links;
	
	//@ApiModel("RestaurantesApenasNomeEmbeddedModel")
	@Data
	public class RestaurantesApenasNomeEmbeddedModelOpenApi {
		private List<RestauranteApenasNomeModel> restaurantes;
	}
}

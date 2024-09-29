package com.algaworks.algafood.api.v2.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//@Relation(collectionRelation = "cozinhas")
@Setter
@Getter
public class CozinhaModelV2 extends RepresentationModel<CozinhaModelV2> {

	/*
	 * Classe que está contida em outro modelo de representação,
	 * também precisa ser anotada.
	 */
	@Schema(example = "1")
	private Long idCozinha;
	
	@Schema(example = "Brasileira")
	private String nomeCozinha;
	
}

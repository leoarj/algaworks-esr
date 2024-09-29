package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "cidades")
@Setter
@Getter
public class CidadeResumoModel extends RepresentationModel<CidadeResumoModel> {

	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Uberlândia")
	private String nome;
	
	@Schema(example = "Minas Gerais")
	private String estado;
	
}
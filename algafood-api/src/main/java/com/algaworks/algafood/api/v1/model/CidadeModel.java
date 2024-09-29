package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "cidades")
//@ApiModel(value = "Cidade", description = "Representa uma cidade")
@Setter
@Getter
public class CidadeModel extends RepresentationModel<CidadeModel> {

	@Schema(description = "ID da cidade", example = "1")
	private Long id;
	
	@Schema(example = "Uberlândia")
	private String nome;
	private EstadoModel estado;
}

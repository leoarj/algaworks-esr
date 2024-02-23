package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.algaworks.algafood.core.validation.Groups;

public class EstadoInput {

	@NotNull(groups = Groups.EstadoId.class)
	private Long id;
	
	@NotBlank
	private String nome;
}

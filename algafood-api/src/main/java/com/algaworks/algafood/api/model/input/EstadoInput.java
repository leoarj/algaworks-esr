package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotBlank;

public class EstadoInput {
	
	@NotBlank
	private String nome;
}

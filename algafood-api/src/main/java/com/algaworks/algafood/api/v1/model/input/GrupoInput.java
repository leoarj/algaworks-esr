package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoInput {

//	@ApiModelProperty(example = "Gerente", required = true)
	@NotBlank
	private String nome;
	
}

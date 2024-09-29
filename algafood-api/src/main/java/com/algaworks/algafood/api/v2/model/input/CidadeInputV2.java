package com.algaworks.algafood.api.v2.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeInputV2 {

	@Schema(example = "Uberlândia")
	@NotBlank
	private String nomeCidade;
	
	@Schema(example = "1")
	@NotNull
	private Long idEstado;
}

package com.algaworks.algafood.api.v2.model.input;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//@ApiModel("CozinhaInput")
@Setter
@Getter
public class CozinhaInputV2 {

	@Schema(example = "Brasileira")
	@NotBlank
	private String nomeCozinha;
}

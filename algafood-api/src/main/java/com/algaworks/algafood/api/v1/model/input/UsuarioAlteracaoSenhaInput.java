package com.algaworks.algafood.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioAlteracaoSenhaInput {

	@Schema(example = "123")
	@NotBlank
	private String senhaAtual;
	
	@Schema(example = "123")
	@NotBlank
	private String novaSenha;
	
}

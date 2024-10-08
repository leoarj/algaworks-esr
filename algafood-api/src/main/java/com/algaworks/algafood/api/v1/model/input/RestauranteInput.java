package com.algaworks.algafood.api.v1.model.input;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;

//import com.algaworks.algafood.core.validation.ValorZeroIncluirDescricao;

import lombok.Getter;
import lombok.Setter;

//Anotação customizada a nível de classe para validar mais de uma propriedade conforme condições impostas.
//@ValorZeroIncluirDescricao(valorField = "taxaFrete",
//		descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Setter
@Getter
public class RestauranteInput {

	@Schema(example = "Thai Gourmet")
	@NotBlank
	private String nome;
	
	@Schema(example = "12.00")
	@NotNull
	@PositiveOrZero
	private BigDecimal taxaFrete;
	
	@Valid
	@NotNull
	private CozinhaIdInput cozinha;
	
	@Valid
	@NotNull
	private EnderecoInput endereco;
	
}
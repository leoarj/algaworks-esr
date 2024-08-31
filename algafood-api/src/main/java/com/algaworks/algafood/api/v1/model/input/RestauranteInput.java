package com.algaworks.algafood.api.v1.model.input;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModelProperty;

//import com.algaworks.algafood.core.validation.ValorZeroIncluirDescricao;

import lombok.Getter;
import lombok.Setter;

//Anotação customizada a nível de classe para validar mais de uma propriedade conforme condições impostas.
//@ValorZeroIncluirDescricao(valorField = "taxaFrete",
//		descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Setter
@Getter
public class RestauranteInput {

	@ApiModelProperty(example = "Thai Gourmet", required = true)
	@NotBlank
	private String nome;
	
	@ApiModelProperty(example = "12.00", required = true)
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
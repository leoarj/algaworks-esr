package com.algaworks.algafood.client.input;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RestauranteInput {

	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaIdInput cozinha;
	private EnderecoInput endereco;	
}

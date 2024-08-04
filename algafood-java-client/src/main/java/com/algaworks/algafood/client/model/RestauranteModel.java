package com.algaworks.algafood.client.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RestauranteModel {

	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaModel cozinha;
	private Boolean ativo;
	private Boolean aberto;
	private EnderecoModel endereco;
}

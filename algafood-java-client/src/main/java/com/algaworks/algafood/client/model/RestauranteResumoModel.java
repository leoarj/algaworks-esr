package com.algaworks.algafood.client.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Modelo de representação para consumo da API.<br>
 * Consumidor deve criar modelos/dto conforme sua linguagem de programação.
 */
@Data
public class RestauranteResumoModel {

	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaModel cozinha;
}

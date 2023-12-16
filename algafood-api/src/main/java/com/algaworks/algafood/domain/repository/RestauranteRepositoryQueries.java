package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

/**
 * Interface customizada para queries customizadas.
 * Deve ser extendida pelo repositório default
 * e uma implementação deve ser provida para esta interface.
 */
public interface RestauranteRepositoryQueries {
	
	List<Restaurante> find(String nome,
			BigDecimal taxaInicial, BigDecimal taxaFinal);

}

package com.algaworks.algafood.infrastructure.repository.spec;

import java.util.ArrayList;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;

/**
 * Classe de especificação para fornecer lógica de filtros para
 * a camada de API, usando o Criteria API do JPA.
 */
public class PedidoSpecs {

	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
		return (root, query, builder) -> {
			
			/*
			 * Condição para verificar o tipo de resutado da query.
			 * Só vai realizar o fetch com as outras entidades para Pedido se for uma query de Pedido.
			 * 
			 * O objetivo é evitar a exception abaixo:
			 * 	"org.hibernate.QueryException:
			 *  query specified join fetching, but the owner of the fetched association was not present in the select list"
			 *  
			 *  Ocorre por causa da execução da query count, onde a mesma não está inclusa no fetch para dados dp Pedido.
			 *  
			 *  "select count(pedido0_.id) as col_0_0_ from pedido pedido0_ where pedido0_.restaurante_id=1 and pedido0_.data_criacao>=?"
			 */
			if (Pedido.class.equals(query.getResultType())) {
				// Para evitar N+1
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
			}
			
			var predicates = new ArrayList<Predicate>();
			
			if (filtro.getClienteId() != null) {
				predicates.add(builder.equal(root.get("cliente").get("id"), filtro.getClienteId()));
			}
			
			if (filtro.getRestauranteId() != null) {
				predicates.add(builder.equal(root.get("restaurante").get("id"), filtro.getRestauranteId()));
			}
			
			if (filtro.getDataCriacaoInicio() != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"),
						filtro.getDataCriacaoInicio()));
			}
			
			if (filtro.getDataCriacaoFim() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"),
						filtro.getDataCriacaoFim()));
			}
			
			return builder.and(predicates.toArray(size -> new Predicate[size]));
		};
	}
	
}

package com.algaworks.algafood.infrastructure.service.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;

// TODO verificar depois como fazer com views e query nativa

@Repository // Para marcar como componente e tradução de exceptions relacionadas a persistência
public class VendaQueryServiceImpl implements VendaQueryService {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);
		
		var predicates = new ArrayList<Predicate>();
		
		var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class,
				root.get("dataCriacao"),
				builder.literal("+00:00"),
				builder.literal(timeOffset));
		
		var functionDateDataCriacao = builder.function(
				"date", Date.class, functionConvertTzDataCriacao);
		
		var selection = builder.construct(VendaDiaria.class,
				functionDateDataCriacao,
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		if (Objects.nonNull(filtro.getRestauranteId())) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
		
		if (Objects.nonNull(filtro.getDataCriacaoInicio())) {
			predicates.add(builder.greaterThanOrEqualTo(
					root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
		}
		
		if (Objects.nonNull(filtro.getDataCriacaoFim())) {
			predicates.add(builder.lessThanOrEqualTo(
					root.get("dataCriacao"), filtro.getDataCriacaoFim()));
		}
		
		// Implementação com "or"
//		predicates.add(builder.or(
//				builder.equal(root.get("status"), StatusPedido.CONFIRMADO),
//				builder.equal(root.get("status"), StatusPedido.ENTREGUE)));
		predicates.add(root.get("status").in(
				StatusPedido.CONFIRMADO,
				StatusPedido.ENTREGUE));
		
		query.select(selection);
		query.where(predicates.toArray(size -> new Predicate[size]));
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}
}

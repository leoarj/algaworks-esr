package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

/**
 * Fornece implementação para interface customizada
 * que será extendida por repositório padrão,
 * dessa forma permitindo implementação de consultas mais complexas e específicas.
 * Deve seguir o mesmo nome do repositório padrão para que o Spring consiga entender
 * que é uma implementação customizada.
 */
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {
//		var jpql = "from Restaurante where nome like :nome " +
//				"and taxaFrete between :taxaInicial and :taxaFinal";
		
		var jpql = new StringBuilder();
		jpql.append("from Restaurante where 0 = 0 ");
		
		var parametros = new HashMap<String, Object>();
		
		if (StringUtils.hasLength(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}
		
		if (taxaInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaInicial", taxaInicial);
		}
		
		if (taxaFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal");
			parametros.put("taxaFinal", taxaFinal);
		}
		
//		return manager.createQuery(jpql.toString(), Restaurante.class)
//				.setParameter("nome", "%" + nome + "%")
//				.setParameter("taxaInicial", taxaInicial)
//				.setParameter("taxaFinal", taxaFinal)
//				.getResultList();
		
		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
	
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
		
		return query.getResultList();
	}

}

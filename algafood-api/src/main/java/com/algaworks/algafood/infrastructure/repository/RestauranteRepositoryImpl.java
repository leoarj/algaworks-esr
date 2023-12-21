package com.algaworks.algafood.infrastructure.repository;

import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
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
	
	@Autowired
	@Lazy // Para inicializar somente quando necessário e evitar erros com dependência circular.
	private RestauranteRepository restauranteRepository;
	
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {
////		var jpql = "from Restaurante where nome like :nome " +
////				"and taxaFrete between :taxaInicial and :taxaFinal";
//		
//		var jpql = new StringBuilder();
//		jpql.append("from Restaurante where 0 = 0 ");
//		
//		var parametros = new HashMap<String, Object>();
//		
//		if (StringUtils.hasLength(nome)) {
//			jpql.append("and nome like :nome ");
//			parametros.put("nome", "%" + nome + "%");
//		}
//		
//		if (taxaInicial != null) {
//			jpql.append("and taxaFrete >= :taxaInicial ");
//			parametros.put("taxaInicial", taxaInicial);
//		}
//		
//		if (taxaFinal != null) {
//			jpql.append("and taxaFrete <= :taxaFinal");
//			parametros.put("taxaFinal", taxaFinal);
//		}
//		
////		return manager.createQuery(jpql.toString(), Restaurante.class)
////				.setParameter("nome", "%" + nome + "%")
////				.setParameter("taxaInicial", taxaInicial)
////				.setParameter("taxaFinal", taxaFinal)
////				.getResultList();
//		
//		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
//	
//		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
//		
//		return query.getResultList();
		
		/**
		 * Exemplo com Criteria API do JPA.
		 * Com criteria, é possível estabelecer consultas programaticamente.
		 * Geralmente é mais utilizada com consultas mais complexas,
		 * devido a quantidade de código necessária para fazer funcionar.
		 * 
		 * Para consultas mais simples JPQL e métodos de conveniência do Spring
		 * ainda são uma boa opção.
		 */
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		Root<Restaurante> root = criteria.from(Restaurante.class); // from Restaurante
		
		var predicates = new ArrayList<>();
		
		if (StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		
		if (taxaInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaInicial));
		}
		
		if (taxaFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFinal));
		}
		
		criteria.where(predicates.toArray(size -> new Predicate[size]));
		
		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		// Utilizando próprio repositório padrão para implementação.
		return restauranteRepository.findAll(comFreteGratis()
				.and(comNomeSemelhante(nome)));
	}

}

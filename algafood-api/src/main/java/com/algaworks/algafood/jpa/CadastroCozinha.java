package com.algaworks.algafood.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CadastroCozinha {

	// Anotação @PersistenceContext para injetar entity manager.
	@PersistenceContext
	private EntityManager manager;
	
	public List<Cozinha> listar() {
		// Utilizando JPQL para obter entidades de cozinha.
		return manager.createQuery("from Cozinha", Cozinha.class)
				.getResultList();
	}
	
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}
	
	// Anotação @Transactional para que as alterações seja efetuadas dentro de uma transação.
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);
	}
	
	@Transactional
	public void remover(Cozinha cozinha) {
		cozinha = buscar(cozinha.getId());
		manager.remove(cozinha);
	}
	
}

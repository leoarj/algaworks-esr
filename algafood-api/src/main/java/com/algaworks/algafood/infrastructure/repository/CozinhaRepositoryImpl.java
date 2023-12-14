package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {

	// Anotação @PersistenceContext para injetar entity manager.
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Cozinha> listar() {
		// Utilizando JPQL para obter entidades de cozinha.
		return manager.createQuery("from Cozinha", Cozinha.class)
				.getResultList();
	}
	
	@Override
	public List<Cozinha> consultarPorNome(String nome) {
		return manager.createQuery("from Cozinha where nome like :nome", Cozinha.class)
				.setParameter("nome", "%" + nome + "%")
				.getResultList();
	}
	
	@Override
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}
	
	// Anotação @Transactional para que as alterações seja efetuadas dentro de uma transação.
	@Transactional
	@Override
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);
	}
	
	@Transactional
	@Override
	public void remover(Long id) {
		Cozinha cozinha = buscar(id);
		
		if (cozinha == null) {
			/*
			 * Exception específica do Spring para dizer que
			 * um número esperado de resultados não foi encontrado.
			 */
			throw new EmptyResultDataAccessException(1);
		}
		
		manager.remove(cozinha);
	}

}

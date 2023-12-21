package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {

	/**
	 * Documentação do Spring sobre query methods:
	 * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
	 */
	
	List<Cozinha> findTodasByNomeContaining(String nome);
	
	Optional<Cozinha> findByNome(String nome);
	
	boolean existsByNome(String nome);
	
}


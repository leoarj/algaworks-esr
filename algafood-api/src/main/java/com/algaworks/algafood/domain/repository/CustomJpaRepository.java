package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface base para repositório customizado, deve estender JpaRepository.
 * 
 * Para não ser considerado um bean de repositório pois é uma interface base para repositório base.
 */
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

	Optional<T> buscarPrimeiro();
	
	void detach(T entity);
	
}

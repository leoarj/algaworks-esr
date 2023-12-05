package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaReposity;

@Service
public class CadastroCozinhaService {

	@Autowired
	private CozinhaReposity cozinhaReposity;
	
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaReposity.salvar(cozinha);
	}
	
}

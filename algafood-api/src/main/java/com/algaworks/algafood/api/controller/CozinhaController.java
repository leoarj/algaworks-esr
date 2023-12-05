package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaReposity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	private final CozinhaReposity cozinhaReposity;
	
	/*
	 * Apenas para teste de negociação de conteúdo
	 * com o header Accept.
	 * */
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar() {
		return cozinhaReposity.listar();
	}
	
//	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
//	public List<Cozinha> listar2() {
//		return cozinhaReposity.listar();
//	}
	
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml() {
		return new CozinhasXmlWrapper(cozinhaReposity.listar());
	}
	
	@GetMapping("/{cozinhaId}")
	public Cozinha buscar(@PathVariable Long cozinhaId) {
		return cozinhaReposity.buscar(cozinhaId);
	}
	
}

package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teste")
public class TesteController {

	private final CozinhaRepository cozinhaRepository;

//	// Utilizando @RequestParam para injetar parâmetro da requisição no argumento
//	@GetMapping("/cozinhas/por-nome")
//	public List<Cozinha> cozinhasPorNome(@RequestParam("nome") String nome) {
//		return cozinhaRepository.consultarPorNome(nome);
//	}
}

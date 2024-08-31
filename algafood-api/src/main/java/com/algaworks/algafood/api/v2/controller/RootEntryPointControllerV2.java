package com.algaworks.algafood.api.v2.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v2.AlgaLinksV2;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Controlador base que representa o RootEntryPoint da API,<br>
 * a partir de onde vão ser listados todos os recursos da API.
 */
@RequiredArgsConstructor
@ApiIgnore
@RestController
@RequestMapping(path = "/v2", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointControllerV2 {

	private final AlgaLinksV2 algaLinks;
	
	/**
	 * Constrói modelo de representação do root entry point da API e adiciona links dos recursos.
	 */
	@GetMapping
	public RootEntryPointModel root() {
		var rootEntryPointModel = new RootEntryPointModel();
		
		rootEntryPointModel.add(algaLinks.linkToCidades("cidades"));
		rootEntryPointModel.add(algaLinks.linkToCozinhas("cozinhas"));
		
		return rootEntryPointModel;
	}
	
	private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
	}
	
}

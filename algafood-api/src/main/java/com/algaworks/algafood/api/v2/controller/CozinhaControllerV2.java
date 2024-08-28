package com.algaworks.algafood.api.v2.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.disassembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v2/cozinhas")
public class CozinhaControllerV2 {

	private final CozinhaRepository cozinhaReposity;
	
	private final CadastroCozinhaService cadastroCozinhaService;
	
	private final CozinhaModelAssemblerV2 cozinhaModelAssembler;
	private final CozinhaInputDisassemblerV2 cozinhaInputDisassembler;
	
	private final PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cozinhaReposity.findAll(pageable);
				
		PagedModel<CozinhaModelV2> cozinhasPagedModel  = pagedResourcesAssembler
				.toModel(cozinhasPage, cozinhaModelAssembler);
		
		return cozinhasPagedModel;
	}
	
	@GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModelV2 buscar(@PathVariable Long cozinhaId) {
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.buscarOuFalhar(cozinhaId));
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModelV2 adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
	}
	
	@PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModelV2 atualizar(@PathVariable Long cozinhaId, 
			@RequestBody @Valid CozinhaInputV2 cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
				
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
	}
		
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}
}

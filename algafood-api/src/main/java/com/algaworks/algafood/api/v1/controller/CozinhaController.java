package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.disassembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/cozinhas")
public class CozinhaController implements CozinhaControllerOpenApi {

	private final CozinhaRepository cozinhaReposity;
	
	private final CadastroCozinhaService cadastroCozinhaService;
	
	private final CozinhaModelAssembler cozinhaModelAssembler;
	private final CozinhaInputDisassembler cozinhaInputDisassembler;
	
	private final PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cozinhaReposity.findAll(pageable);
		
		log.info("Consultando cozinhas com páginas de {} registro...", pageable.getPageSize());
		
//		if (true) {
//			throw new RuntimeException("Exception de teste");
//		}
		
//		List<CozinhaModel> cozinhasModel = cozinhaModelAssembler
//				.toCollectionModel(cozinhasPage.getContent());
//		
//		Page<CozinhaModel> cozinhasModelPage = new PageImpl<>(cozinhasModel, pageable,
//				cozinhasPage.getTotalElements());
		
		PagedModel<CozinhaModel> cozinhasPagedModel  = pagedResourcesAssembler
				.toModel(cozinhasPage, cozinhaModelAssembler);
		
		return cozinhasPagedModel;
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.buscarOuFalhar(cozinhaId));
	}
	
	@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
	}
	
	@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
	@PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModel atualizar(@PathVariable Long cozinhaId, 
			@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
				
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
	}
		
	@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}
}

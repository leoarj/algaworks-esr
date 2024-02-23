package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.disassembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	private final CozinhaRepository cozinhaReposity;
	
	private final CadastroCozinhaService cadastroCozinhaService;
	
	private final CozinhaModelAssembler cozinhaModelAssembler;
	private final CozinhaInputDisassembler cozinhaInputDisassembler;
	
	@GetMapping
	public List<CozinhaModel> listar() {
		return cozinhaModelAssembler.toCollectionModel(cozinhaReposity.findAll());
	}
	
	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		// Código anterior mais complexo, está no histórico do repositório Git
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.buscarOuFalhar(cozinhaId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
	}
	
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId, 
			@RequestBody @Valid CozinhaInput cozinhaInput) {
		// Código anterior mais complexo, está no histórico do repositório Git
		Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		
//		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
	}
	
//	@DeleteMapping("/{cozinhaId}")
//	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
//		try {
//			cadastroCozinhaService.excluir(cozinhaId);
//			return ResponseEntity.noContent().build();
//			
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//			
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//					.body(e.getMessage());
//		}
//	}
	
	// Removidos try/catch para testar a definição do status code diretamente
	// a partir das exceptions.
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
//		try {
//			cadastroCozinhaService.excluir(cozinhaId);
//		} catch (EntidadeNaoEncontradaException e) {
//			// Teste com exceções próprias do Spring
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//			//throw new ServerWebInputException(e.getMessage()); // Já retorna 409 - CONFLICT
//		}
	}
}

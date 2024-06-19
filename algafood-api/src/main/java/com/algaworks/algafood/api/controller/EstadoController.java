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

import com.algaworks.algafood.api.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.disassembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/estados")
public class EstadoController {

	private final EstadoRepository estadoRepository;
	private final CadastroEstadoService cadastroEstadoService;
	
	private final EstadoModelAssembler estadoModelAssembler;
	private final EstadoInputDisassembler estadoInputDisassembler;
	
	@GetMapping
	public List<EstadoModel> listar() {
		return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
	}
	
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
		return estadoModelAssembler.toModel(cadastroEstadoService.buscarOuFalhar(estadoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
		
		return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estado));
	}
	
	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId,
			@RequestBody @Valid EstadoInput estadoInput) {
		Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);
		
//		BeanUtils.copyProperties(estado, estadoAtual, "id");
		
		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
		
		return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estadoAtual));
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		cadastroEstadoService.remover(estadoId);
	}
	
}

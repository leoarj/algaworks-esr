package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.disassembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
	
	private final FormaPagamentoRepository formaPagamentoRepository;
	
	private final CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	private final FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	private final FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;

	@GetMapping
	public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request) {
		if (verificarSeEtagNaoFoiModificada(request,
				formaPagamentoRepository.getDataUltimaAtualizacao())) {
			return null;
		}
		
		List<FormaPagamentoModel> formasPagamentoModel = formaPagamentoModelAssembler
				.toCollectionModel(formaPagamentoRepository.findAll());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
				.body(formasPagamentoModel);
	}
	
	@GetMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId, ServletWebRequest request) {
		if (verificarSeEtagNaoFoiModificada(request,
				formaPagamentoRepository.getDataAtualizacaoById(formaPagamentoId))) {
			return null;
		}
		
		FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler
				.toModel(cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId));
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
				.body(formaPagamentoModel);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
		
		return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamento));
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
		
		return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamentoAtual));
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long formaPagamentoId) {
		cadastroFormaPagamentoService.excluir(formaPagamentoId);
	}
	
	/**
	 * Desabilita controle de cache do ShallowEtagHeaderFilter.
	 * Verifica se a etag da requisição corresponde com a etag atual,
	 * com base na última data de atualização.
	 */
	private boolean verificarSeEtagNaoFoiModificada(ServletWebRequest request,
			OffsetDateTime dataUltimaAtualizacao) {
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		return request.checkNotModified(eTag);
	}
}

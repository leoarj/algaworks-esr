package com.algaworks.algafood.api.v1.controller;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.disassembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/formas-pagamento")
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {
	
	private final FormaPagamentoRepository formaPagamentoRepository;
	
	private final CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	private final FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	private final FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;

	@CheckSecurity.FormasPagamento.PodeConsultar
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {
		Optional<String> optEtag = calcularEtag(request,
				formaPagamentoRepository.getDataUltimaAtualizacao());
		
		if (optEtag.isPresent()) {
			CollectionModel<FormaPagamentoModel> formasPagamentoModel = formaPagamentoModelAssembler
					.toCollectionModel(formaPagamentoRepository.findAll());
			
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
					.eTag(optEtag.get())
					.body(formasPagamentoModel);
		}
		
		return null;
	}
	
	@CheckSecurity.FormasPagamento.PodeConsultar
	@GetMapping(path = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId, ServletWebRequest request) {
		Optional<String> optEtag = calcularEtag(request,
				formaPagamentoRepository.getDataAtualizacaoById(formaPagamentoId));
		
		if (optEtag.isPresent()) {
			FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler
					.toModel(cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId));
			
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
					.eTag(optEtag.get())
					.body(formaPagamentoModel);
		}
		
		return null;
	}
	
	@CheckSecurity.FormasPagamento.PodeEditar
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
		
		return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamento));
	}
	
	@CheckSecurity.FormasPagamento.PodeEditar
	@PutMapping(path = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
		
		return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamentoAtual));
	}
	
	@CheckSecurity.FormasPagamento.PodeEditar
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
	private Optional<String> calcularEtag(ServletWebRequest request,
			OffsetDateTime dataUltimaAtualizacao) {
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		eTag = request.checkNotModified(eTag) ? null : eTag;
		
		return Optional.ofNullable(eTag);
	}
}

package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.v1.disassembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos",
	produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {
	
	private final ProdutoModelAssembler produtoModelAssembler;
	private final ProdutoInputDisassembler produtoInputDisassembler;
	
	private final CadastroProdutoService cadastroProdutoService;
	
	private final AlgaLinks algaLinks;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId,
			@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
		
		List<Produto> todosProdutos = null;
		
		if (incluirInativos) {
			todosProdutos = cadastroProdutoService.listarOuFalharTodosPorCodigoRestaurante(restauranteId);
		} else {
			todosProdutos = cadastroProdutoService.listarOuFalharAtivosPorCodigoRestaurante(restauranteId);
		}
		
		return produtoModelAssembler.toCollectionModel(todosProdutos)
				.add(algaLinks.linkToProdutos(restauranteId));
	}
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		return produtoModelAssembler.toModel(cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId));
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFunctionamento
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@PathVariable Long restauranteId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
		
		return produtoModelAssembler.toModel(cadastroProdutoService.salvarNovo(produto, restauranteId));
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFunctionamento
	@PutMapping("/{produtoId}")
	public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produtoAtual = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		
		return produtoModelAssembler.toModel(cadastroProdutoService.salvar(produtoAtual));
	} 
}

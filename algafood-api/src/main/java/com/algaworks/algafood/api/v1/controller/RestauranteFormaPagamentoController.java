package com.algaworks.algafood.api.v1.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/formas-pagamento",
	produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements
	RestauranteFormaPagamentoControllerOpenApi {
	
	private final CadastroRestauranteService cadastroRestauranteService;
	
	private final FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	private final AlgaLinks algaLinks;
	
	private final AlgaSecurity algaSecurity;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		CollectionModel<FormaPagamentoModel> formasPagamentoModel =
				formaPagamentoModelAssembler.toCollectionModel(
						restaurante.getFormasPagamento())
				.removeLinks()
				//.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId, "formas-pagamento"))
				.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId));
		
		if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restauranteId)) {
			formasPagamentoModel.add(algaLinks
					.linkTolinkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar"));
			
			formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
				formaPagamentoModel.add(algaLinks
						.linkTolinkToRestauranteFormaPagamentoDesassociacao(
								restauranteId, formaPagamentoModel.getId(), "desassociar"));
			});
		}
		
		return formasPagamentoModel;
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
		
		return ResponseEntity.noContent().build();
	}
}

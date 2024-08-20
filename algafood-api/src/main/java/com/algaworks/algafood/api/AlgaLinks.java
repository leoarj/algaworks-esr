package com.algaworks.algafood.api;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.controller.FluxoPedidoController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.RestauranteFormaPagamentoController;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.controller.RestauranteUsuarioResponsavelController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.controller.UsuarioGrupoController;

@Component
public class AlgaLinks {
	
	public static final TemplateVariables PAGE_VARIABLES = new TemplateVariables(
			new TemplateVariable("page", VariableType.REQUEST_PARAM),
			new TemplateVariable("size", VariableType.REQUEST_PARAM),
			new TemplateVariable("sort", VariableType.REQUEST_PARAM));

	public Link linkToPedidos(String rel) {
		TemplateVariables filterVariables = new TemplateVariables(
				new TemplateVariable("clienteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM));
		
		String pedidosUrl = WebMvcLinkBuilder.linkTo(PedidoController.class)
				.toUri()
				.toString();
		
		return Link.of(UriTemplate.of(pedidosUrl,
				PAGE_VARIABLES.concat(filterVariables)), rel);
	}
	
	public Link linkToConfirmacaoPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(FluxoPedidoController.class)
				.confirmar(codigoPedido))
				.withRel(rel);
	}
	
	public Link linkToEntregaPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(FluxoPedidoController.class)
				.entregar(codigoPedido))
				.withRel(rel);
	}
	
	public Link linkToCancelamentoPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(FluxoPedidoController.class)
				.cancelar(codigoPedido))
				.withRel(rel);
	}
	
	public Link linkToRestaurante(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteController.class)
				.buscar(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestaurante(Long restauranteId) {
		return linkToRestaurante(restauranteId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToRestaurantes(String rel) {
		TemplateVariables projectionVariables = new TemplateVariables(
				new TemplateVariable("projecao", VariableType.REQUEST_PARAM));
				
		String restaurantesUrl = WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(RestauranteController.class)
					.listar())
				.toUri()
				.toString();
		
		return Link.of(UriTemplate.of(restaurantesUrl, projectionVariables), rel);
	}
	
	public Link linkToRestaurantes() {
		return linkToRestaurantes(IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToRestauranteAtivacao(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteController.class)
				.ativar(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteInativacao(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteController.class)
				.inativar(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteAbertura(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteController.class)
				.abrir(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteFechamento(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteController.class)
				.fechar(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteFormasPagamento(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteFormaPagamentoController.class)
				.listar(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteFormasPagamento(Long restauranteId) {
		return linkToRestauranteFormasPagamento(restauranteId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkTolinkToRestauranteFormaPagamentoDesassociacao(
			Long restauranteId, Long formaPagamentoId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteFormaPagamentoController.class)
				.desassociar(restauranteId, formaPagamentoId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteResponsaveis(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteUsuarioResponsavelController.class)
				.listar(restauranteId))
				.withRel(rel);
	}
	
	public Link linkToRestauranteResponsaveis(Long restauranteId) {
		return linkToRestauranteResponsaveis(restauranteId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToUsuario(Long usuarioId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(UsuarioController.class)
				.buscar(usuarioId))
				.withRel(rel);
	}
	
	public Link linkToUsuario(Long usuarioId) {
		return linkToUsuario(usuarioId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToUsuarios(String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(UsuarioController.class)
				.listar())
				.withRel(rel);
	}
	
	public Link linkToUsuarios() {
		return linkToUsuarios(IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToGruposUsuario(Long usuarioId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(UsuarioGrupoController.class)
				.listar(usuarioId))
				.withRel(rel);
	}
	
	public Link linkToGruposUsuario(Long usuarioId) {
		return linkToGruposUsuario(usuarioId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToFormaPagamento(Long formaPagamentoId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(FormaPagamentoController.class)
				.buscar(formaPagamentoId, null)) // <- Passando null porque é indiferente para construir a url
				.withRel(rel);
	}
	
	public Link linkToFormaPagamento(Long formaPagamentoId) {
		return linkToFormaPagamento(formaPagamentoId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToFormasPagamento(String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(FormaPagamentoController.class)
				.listar(null))
				.withRel(rel);
	}
	
	public Link linkToFormasPagamento() {
		return linkToFormasPagamento(IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToCidade(Long cidadeId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(CidadeController.class)
				.buscar(cidadeId))
				.withRel(rel);
	}
	
	public Link linkToCidade(Long cidadeId) {
		return linkToCidade(cidadeId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToCidades(String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(CidadeController.class)
				.listar())
				.withRel(rel);
	}
	
	public Link linkToCidades() {
		return linkToCidades(IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToProduto(Long restauranteId, Long produtoId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class)
				.buscar(restauranteId, produtoId))
				.withRel(rel);
	}
	
	public Link linkToProduto(Long restauranteId, Long produtoId) {
		return linkToProduto(restauranteId, produtoId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToEstado(Long estadoId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EstadoController.class)
				.buscar(estadoId))
				.withRel(rel);
	}
	
	public Link linkToEstado(Long estadoId) {
		return linkToEstado(estadoId, IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToEstados(String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EstadoController.class)
				.listar())
				.withRel(rel);
	}
	
	public Link linkToEstados() {
		return linkToEstados(IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToCozinhas(String rel) {
		return WebMvcLinkBuilder.linkTo(CozinhaController.class)
				.withRel(rel);
	}
	
	public Link linkToCozinhas() {
		return linkToCozinhas(IanaLinkRelations.SELF_VALUE);
	}
	
	public Link linkToCozinha(Long cozinhaId, String rel) {
		return WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(CozinhaController.class)
				.buscar(cozinhaId))
				.withRel(rel);
	}
	
	public Link linkToCozinha(Long cozinhaId) {
		return linkToCozinha(cozinhaId, IanaLinkRelations.SELF_VALUE);
	}
	
}

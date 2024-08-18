package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;

	public PedidoModelAssembler() {
		super(PedidoController.class, PedidoModel.class);
	}
	
	@Override
	public PedidoModel toModel(Pedido pedido) {
		PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
		
		modelMapper.map(pedido, pedidoModel);
		
		pedidoModel.add(algaLinks.linkToPedidos());
		
//		pedidoModel.add(WebMvcLinkBuilder.linkTo(PedidoController.class)
//				.withRel("pedidos"));
		
		if (pedido.podeSerConfirmado()) {
			pedidoModel.add(algaLinks.linkToConfirmacaoPedido(pedidoModel.getCodigo(), "confirmar"));
		}
		
		if (pedido.podeSerCancelado()) {
			pedidoModel.add(algaLinks.linkToCancelamentoPedido(pedidoModel.getCodigo(), "cancelar"));
		}
		
		if (pedido.podeSerEntrege()) {
			pedidoModel.add(algaLinks.linkToEntregaPedido(pedidoModel.getCodigo(), "entregar"));
		}
		
		pedidoModel.getRestaurante().add(algaLinks
				.linkToRestaurante(pedidoModel
						.getRestaurante().getId()));
//		pedidoModel.getRestaurante().add(WebMvcLinkBuilder.linkTo(
//				WebMvcLinkBuilder.methodOn(RestauranteController.class)
//				.buscar(pedidoModel.getRestaurante().getId()))
//				.withSelfRel());
		
		pedidoModel.getCliente().add(algaLinks
				.linkToUsuario(pedidoModel
						.getCliente().getId()));
//		pedidoModel.getCliente().add(WebMvcLinkBuilder.linkTo(
//				WebMvcLinkBuilder.methodOn(UsuarioController.class)
//				.buscar(pedidoModel.getCliente().getId()))
//				.withSelfRel());
		
		pedidoModel.getFormaPagamento().add(algaLinks
				.linkToFormaPagamento(pedidoModel
						.getFormaPagamento().getId()));
//		pedidoModel.getFormaPagamento().add(WebMvcLinkBuilder.linkTo(
//				WebMvcLinkBuilder.methodOn(FormaPagamentoController.class)
//				.buscar(pedidoModel.getFormaPagamento().getId(), null)) // <- Passando null porque é indiferente para construir a url
//				.withSelfRel());
		
		pedidoModel.getEnderecoEntrega().getCidade().add(algaLinks
				.linkToCidade(pedidoModel
						.getEnderecoEntrega()
							.getCidade().getId()));
//		pedidoModel.getEnderecoEntrega().getCidade().add(WebMvcLinkBuilder.linkTo(
//				WebMvcLinkBuilder.methodOn(CidadeController.class)
//				.buscar(pedidoModel.getEnderecoEntrega().getCidade().getId()))
//				.withSelfRel());
		
		pedidoModel.getItens().forEach(item -> {
			item.add(algaLinks.linkToProduto(pedidoModel
					.getRestaurante().getId(), item.getProdutoId(), "produto"));
//			item.add(WebMvcLinkBuilder.linkTo(
//					WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class)
//					.buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
//					.withRel("produto"));
		});
		
		return pedidoModel;
	}
	
//	public List<PedidoModel> toCollectionModel(List<Pedido> pedidos) {
//		return pedidos.stream()
//				.map(this::toModel)
//				.toList();
//	}
}

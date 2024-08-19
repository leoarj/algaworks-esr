package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;
	
	public PedidoResumoModelAssembler() {
		super(PedidoController.class, PedidoResumoModel.class);
	}
	
	@Override
	public PedidoResumoModel toModel(Pedido pedido) {
		PedidoResumoModel pedidoResumoModel = createModelWithId(pedido.getCodigo(), pedido);
		
		modelMapper.map(pedido, pedidoResumoModel);
		
		pedidoResumoModel.add(algaLinks.linkToPedidos("pedidos"));
//		pedidoResumoModel.add(WebMvcLinkBuilder.linkTo(PedidoController.class)
//				.withRel("pedidos"));
		
		pedidoResumoModel.getRestaurante().add(algaLinks
				.linkToRestaurante(pedidoResumoModel
						.getRestaurante().getId()));
//		pedidoResumoModel.getRestaurante().add(WebMvcLinkBuilder.linkTo(
//				WebMvcLinkBuilder.methodOn(RestauranteController.class)
//				.buscar(pedidoResumoModel.getRestaurante().getId()))
//				.withSelfRel());
		
		pedidoResumoModel.getCliente().add(algaLinks.linkToUsuario(
				pedidoResumoModel
				.getCliente().getId()));
//		pedidoResumoModel.getCliente().add(WebMvcLinkBuilder.linkTo(
//				WebMvcLinkBuilder.methodOn(UsuarioController.class)
//				.buscar(pedidoResumoModel.getCliente().getId()))
//				.withSelfRel());
	
		return pedidoResumoModel;
	}
	
//	public List<PedidoResumoModel> toCollectionModel(List<Pedido> pedidos) {
//		return pedidos.stream()
//				.map(this::toModel)
//				.toList();
//	}
}

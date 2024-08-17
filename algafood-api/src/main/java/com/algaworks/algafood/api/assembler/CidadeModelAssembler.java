package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.domain.model.Cidade;

/**
 * Estendendo {@link RepresentationModelAssemblerSupport} para adicionar automação de links no modelo de representação.
 */
@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

	@Autowired
	private ModelMapper modelMapper;

	public CidadeModelAssembler() {
		// construtor da superclasse, deve informar a class do controlador e do modelo de representação
		super(CidadeController.class, CidadeModel.class);
	}
	
	// toModel já existe na classe de suporte, bastando adicionar a anotação de sobreescrita
	// sobreescrevendo para adicionar os links
	@Override
	public CidadeModel toModel(Cidade cidade) {
		CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);
		
		modelMapper.map(cidade, cidadeModel);
		
		cidadeModel.add(WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(CidadeController.class)
				.listar())
				.withRel("cidades"));
		
		
		cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(EstadoController.class)
				.buscar(cidadeModel.getEstado().getId()))
				.withSelfRel());
		
		return cidadeModel;
	}
	
	// sobreescrevendo para adicionar os links referentes ao _self da coleção
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
		return super.toCollectionModel(entities)
				.add(WebMvcLinkBuilder.linkTo(CidadeController.class)
				.withSelfRel());
	}
	
//	public List<CidadeModel> toCollectionModel(List<Cidade> cidades) {
//		return cidades.stream()
//				.map(this::toModel)
//				.toList();
//	}
	
}

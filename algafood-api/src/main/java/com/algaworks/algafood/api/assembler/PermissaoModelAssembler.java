package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Permissao;

/**
 * Implementando {@link RepresentationModelAssembler} porque não precisa personalizar os links.
 */
@Component
public class PermissaoModelAssembler
	implements RepresentationModelAssembler<Permissao, PermissaoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Override
	public PermissaoModel toModel(Permissao permissao) {
		return modelMapper.map(permissao, PermissaoModel.class);
	}
	
	@Override
	public CollectionModel<PermissaoModel> toCollectionModel(Iterable<? extends Permissao> entities) {
		// Chamando método default
		return RepresentationModelAssembler.super.toCollectionModel(entities)
				.add(algaLinks.linkToPermissoes());
	}
	
//	public List<PermissaoModel> toCollectionModel(Collection<Permissao> permissoes) {
//		return permissoes.stream()
//				.map(this::toModel)
//				.toList();
//	}
	
}

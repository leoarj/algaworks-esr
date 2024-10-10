package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;

/**
 * Estendendo {@link RepresentationModelAssemblerSupport} para adicionar automação<br>
 * de links no modelo de representação.
 */
@Component
public class CidadeModelAssembler
	extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
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
		
		if (algaSecurity.podeConsultarCidades()) {
			cidadeModel.add(algaLinks.linkToCidades("cidades"));
		}
		
		if (algaSecurity.podeConsultarEstados()) {
			cidadeModel.getEstado().add(algaLinks
					.linkToEstado(cidadeModel
							.getEstado().getId()));
		}
		
		return cidadeModel;
	}
	
	// sobreescrevendo para adicionar os links referentes ao _self da coleção
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
		CollectionModel<CidadeModel> collectionModel = super.toCollectionModel(entities);
		
		if (algaSecurity.podeConsultarCidades() ) {
			collectionModel.add(algaLinks.linkToCidades());
		}
		
		return collectionModel;
	}	
}

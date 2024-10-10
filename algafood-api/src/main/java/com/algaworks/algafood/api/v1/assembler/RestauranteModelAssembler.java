package com.algaworks.algafood.api.v1.assembler;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler
	extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	public RestauranteModelAssembler() {
		super(RestauranteController.class, RestauranteModel.class);
	}
	
	@Override
	public RestauranteModel toModel(Restaurante restaurante) {
		RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		
		modelMapper.map(restaurante, restauranteModel);
		
		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
		}
		
		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			if (restaurante.podeSerAtivado()) {
				restauranteModel.add(
						algaLinks.linkToRestauranteAtivacao(
								restauranteModel.getId(), "ativar"));
			}
			
			if (restaurante.podeSerInativado()) {
				restauranteModel.add(
						algaLinks.linkToRestauranteInativacao(
								restauranteModel.getId(), "inativar"));
			}
		}
		
		if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
			if (restaurante.podeSerAberto()) {
				restauranteModel.add(
						algaLinks.linkToRestauranteAbertura(
								restauranteModel.getId(), "abrir"));
			}
			
			if (restaurante.podeSerFechado()) {
				restauranteModel.add(
						algaLinks.linkToRestauranteFechamento(
								restauranteModel.getId(), "fechar"));
			}
		}
		
		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteModel.add(
					algaLinks.linkToProdutos(
							restauranteModel.getId(), "produtos"));
		}
		
		if (algaSecurity.podeConsultarCozinhas()) {
			restauranteModel.getCozinha().add(
					algaLinks.linkToCozinha(
							restauranteModel.getCozinha().getId()));
		}
		
		if (algaSecurity.podeConsultarCidades()) {
			if (Objects.nonNull(restauranteModel.getEndereco()) &&
					Objects.nonNull(restauranteModel.getEndereco().getCidade())) {
				restauranteModel.getEndereco().getCidade().add(
						algaLinks.linkToCidade(
								restauranteModel.getEndereco().getCidade().getId()));
			}
		}
		
		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteModel.add(
					algaLinks.linkToRestauranteFormasPagamento(
							restauranteModel.getId(), "formas-pagamento"));
		}
		
		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			restauranteModel.add(
					algaLinks.linkToRestauranteResponsaveis(
							restauranteModel.getId(), "responsaveis"));
		}
		
		return restauranteModel;
	}
	
	@Override
	public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
		CollectionModel<RestauranteModel> collectionModel = super.toCollectionModel(entities);
		
		if (algaSecurity.podeConsultarRestaurantes()) {
			collectionModel.add(algaLinks.linkToRestaurantes());
		}
		
		return collectionModel;
	}
}

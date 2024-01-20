package com.algaworks.algafood.domain.service;

import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CadastroRestauranteService {

	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe um cadastro de restaurante com código %d";
	
	private final RestauranteRepository restauranteRepository;
	private final CozinhaRepository cozinhaRepository;
	private final CadastroCozinhaService cadastroCozinhaService;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		
		restaurante.setCozinha(cozinha); // Atualiza restaurante com cozinha vinda do respositório (managed).
		
		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId)));
	}
	
}

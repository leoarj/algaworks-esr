package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	private final RestauranteRepository restauranteRepository;
	private final CadastroRestauranteService cadastroRestauranteService;
	private final CozinhaRepository cozinhaRepository;
	
	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.listar();
	}
	
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteRepository.buscar(restauranteId);
		
		if (restaurante != null) {
			return ResponseEntity.ok(restaurante);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestauranteService.salvar(restaurante);
		
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(restaurante);
		} catch(EntidadeNaoEncontradaException e) {
			/**
			 * Está tratando entidade náo encontrada (Cozinha)
			 * como bad-request e não como not-found devido ao fato
			 * de que é uma entidade relacionada ao restaurante
			 * no momento da persistência, sendo assim, um restaurante
			 * não pode ser incluído com uma cozinha inexistente (má-requisição),
			 * e passar o status 404-NOT-FOUND traria um mau entendimento
			 * de que é o recurso de /restaurentes que não existe.
			 */
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}
	
	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
			@RequestBody Restaurante restaurante) {
		try {
			Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
			
			if (restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				
				restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
				return ResponseEntity.ok(restauranteAtual);
			}
			
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}
	
	/**
	 * Mapeamento para verbo PATCH (atualização parcial de recurso).
	 * 
	 * O verbo PATCH é utilizado quando queremos atualizar um recurso parcialmente,
	 * ou seja, não alterando todas as propriedades na requisição.
	 * 
	 * Não é uma atualização convencional como no verbo PUT, onde o recurso inteiro é atualizado,
	 * mas necessita de um tratamento específico para identificar as propriedades que foram alteradas.
	 * 
	 * No caso, é passado um Map de String/Object onde o corpo da requisição será serializado,
	 * e posteriormente aplicado na atualização do recurso.
	 */
	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos) {
		Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
		
		if (restauranteAtual == null) {
			return ResponseEntity.notFound().build();
		}
		
		//merge(campos, restauranteAtual);
		restauranteAtual = merge2(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);
	}

	/**
	 * Método para tratar a atualização parcial do recurso com base nas propriedades recebidas.
	 * 
	 * Reflection pode ser utilizada para manipular as propriedades alteradas.
	 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		// Mapeador para transformar propriedades de um corpo de requição para um objeto de uma classe especificada.
		ObjectMapper objectMapper = new ObjectMapper();
		// Converte o corpo JSON preenchendo em um objeto as propriedades correspondentes de mesmo nome definidos na classe.
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			// Obtém um representação referente a um campo da classe
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			// Para permitir acessar o campo privado
			field.setAccessible(true);
			
			// Obtém valor contido no campo e altera no objeto convertido inicialmente
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			// Altera no objeto de destino o valor obtido do objeto convertido (origem)
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
	
	// Sugestão de implementação sem usar reflection
	private Restaurante merge2(Map<String, Object> dadosOrigem, Restaurante restauranteAtual) {
		// Mapeador para transformar propriedades de um corpo de requição para um objeto de uma classe especificada.
		ObjectMapper objectMapper = new ObjectMapper();
		// Converte o corpo JSON preenchendo em um objeto as propriedades correspondentes de mesmo nome definidos na classe.
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		// Extrai quais propriedades atualizadas, para que ao repassar dados do restaurante atual
		// as propriedades atualizadas do restaurante convertido do corpo JSON não sejam alteradas de volta ao valor atual.
		String[] propriedadesIgnorar = dadosOrigem.keySet().toArray(String[]::new); //size -> new String[size]
		BeanUtils.copyProperties(restauranteAtual, restauranteOrigem, propriedadesIgnorar);
		return restauranteOrigem;
		
	}

}

package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.CozinhaIdInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	private final RestauranteRepository restauranteRepository;
	private final CadastroRestauranteService cadastroRestauranteService;
	
	/**
	 * Para poder realizar a validação diretamente e não a cargo do framework.
	 */
	@Autowired
	private SmartValidator validator;
	
	private final RestauranteModelAssembler restauranteModelAssembler;
	
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		return restauranteModelAssembler.toModel(restaurante);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = toDomainObject(restauranteInput);
			
			return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		
			/**
			 * Está tratando entidade náo encontrada (Cozinha)
			 * como bad-request e não como not-found devido ao fato
			 * de que é uma entidade relacionada ao restaurante
			 * no momento da persistência, sendo assim, um restaurante
			 * não pode ser incluído com uma cozinha inexistente (má-requisição),
			 * e passar o status 404-NOT-FOUND traria um mau entendimento
			 * de que é o recurso de /restaurentes que não existe.
			 */
		
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = toDomainObject(restauranteInput);
			
			Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
			
			BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
			
			return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
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
	public RestauranteModel atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos, HttpServletRequest request) {		
		Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");
		
		return atualizar(restauranteId, toInput(restauranteAtual));
	}

	private void validate(Restaurante restaurante, String objectName) {
		/*
		 * Objeto de validação precisa de um registrador dos erros de validação
		 * e suas respectivas propriedades de um determinado objeto.
		 */
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult); // Exceção para ser tratada no Exception Handler
		}
	}

	/**
	 * Método para tratar a atualização parcial do recurso com base nas propriedades recebidas.
	 * 
	 * Reflection pode ser utilizada para manipular as propriedades alteradas.
	 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			// Mapeador para transformar propriedades de um corpo de requição para um objeto de uma classe especificada.
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
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
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
	// Sugestão de implementação sem usar reflection
	private Restaurante merge2(Map<String, Object> dadosOrigem, Restaurante restauranteAtual, HttpServletRequest request) {
		
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			// Mapeador para transformar propriedades de um corpo de requição para um objeto de uma classe especificada.
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			// Converte o corpo JSON preenchendo em um objeto as propriedades correspondentes de mesmo nome definidos na classe.
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			// Extrai quais propriedades atualizadas, para que ao repassar dados do restaurante atual
			// as propriedades atualizadas do restaurante convertido do corpo JSON não sejam alteradas de volta ao valor atual.
			String[] propriedadesIgnorar = dadosOrigem.keySet().toArray(String[]::new); //size -> new String[size]
			BeanUtils.copyProperties(restauranteAtual, restauranteOrigem, propriedadesIgnorar);
			return restauranteOrigem;
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
		
	}
	
	private Restaurante toDomainObject(RestauranteInput restauranteInput) {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());
		
		restaurante.setCozinha(cozinha);
		
		return restaurante;
	}
	
	// Temporário, por causa do atualizar parcial com PATCH
	private RestauranteInput toInput(Restaurante restaurante) {
		CozinhaIdInput cozinhaIdInput = new CozinhaIdInput();
		cozinhaIdInput.setId(restaurante.getCozinha().getId());
		
		RestauranteInput restauranteInput = new RestauranteInput();
		restauranteInput.setNome(restaurante.getNome());
		restauranteInput.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteInput.setCozinha(cozinhaIdInput);
		return restauranteInput;
	}

}

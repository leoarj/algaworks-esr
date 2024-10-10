package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CadastroProdutoService {
	
	private final ProdutoRepository produtoRepository;
	
	private final CadastroRestauranteService cadastroRestauranteService;

	@Transactional
	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}
	
	@Transactional
	public Produto salvarNovo(Produto produto, Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		produto.setRestaurante(restaurante);
		
		return salvar(produto);
	}
	
	public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findById(restauranteId, produtoId)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId, restauranteId));
	}
	
	public List<Produto> listarOuFalharTodosPorCodigoRestaurante(Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		//return restaurante.getProdutos();
		// ou
		return produtoRepository.findTodosByRestaurante(restaurante);
	}
	
	public List<Produto> listarOuFalharAtivosPorCodigoRestaurante(Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		return produtoRepository.findAtivosByRestaurante(restaurante);
	}
}

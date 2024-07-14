package com.algaworks.algafood.api.model.view;

/**
 * Interface para definir view de projeção relacionada a algum recurso.
 * 
 * Nela são definidas as diferentes projeções que podem ser usadas para representação de um recurso.
 */
public interface RestauranteView {

	/**
	 * Representa projeção padrão de resumo de recurso.
	 */
	public interface Resumo {}
	
	/**
	 * Representa projeção de resumo com apenas o id/nome do restautante
	 * (nome da interface deve ser semântico ao que ser quer representar).
	 */
	public interface ApenasNome {}
	
}

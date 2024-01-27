package com.algaworks.algafood.domain.exception;

/**
 * Exception de negócio lançada pelo domain service e tratada no controlador.
 * 
 * É uma boa prática não tratar exceções específicas de infraestrutura nos controladores,
 * em vez disso o correto é "traduzir" essas exceções em exceções relativas ao negócio
 * e que deem sentido semântico ao erro em questão, de forma a ter um tratamento mais
 * intuitivo e com mais sentido em outras camadas.
 * 
 */
public abstract class EntidadeNaoEncontradaException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncontradaException(String message) {
		super(message);
	}

}

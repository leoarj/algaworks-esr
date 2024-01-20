package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception de negócio lançada pelo domain service e tratada no controlador.
 * 
 * É uma boa prática não tratar exceções específicas de infraestrutura nos controladores,
 * em vez disso o correto é "traduzir" essas exceções em exceções relativas ao negócio
 * e que deem sentido semântico ao erro em questão, de forma a ter um tratamento mais
 * intuitivo e com mais sentido em outras camadas.
 * 
 */
// Definindo o código de status diretamente na exception (que vai ser retornado no controller, caso a exception ocorrer).
//@ResponseStatus(value = HttpStatus.NOT_FOUND) //, reason = "Entidade não encontrada")
// Estendendo ResponseStatusException
public class EntidadeNaoEncontradaException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncontradaException(HttpStatus status, String reason) {
		super(status, reason);
	}

	public EntidadeNaoEncontradaException(String message) {
		this(HttpStatus.NOT_FOUND, message);
	}

}

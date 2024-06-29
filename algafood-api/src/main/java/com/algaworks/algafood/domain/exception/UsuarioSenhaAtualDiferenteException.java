package com.algaworks.algafood.domain.exception;

public class UsuarioSenhaAtualDiferenteException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public UsuarioSenhaAtualDiferenteException(String message) {
		super(message);
	}
	
}

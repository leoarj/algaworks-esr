package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.model.FormaPagamento;

public class FormaPagamentoNaoAceitaException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoAceitaException(String message) {
		super(message);
	}
	
	public FormaPagamentoNaoAceitaException(FormaPagamento formaPagamento) {
		this(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.", formaPagamento.getDescricao()));
	}
}

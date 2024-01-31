package com.algaworks.algafood.core.validation;

import org.springframework.validation.BindingResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Necessário que a exceção contenha o resultado de validações
	// para que possam ser tratadas no Exception Handler
	private BindingResult bindingResult;
}

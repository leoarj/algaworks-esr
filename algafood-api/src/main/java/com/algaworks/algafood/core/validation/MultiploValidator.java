package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Encapsula regra de validação para ser utilizada como "constraint" de validação.
 * 
 * Primeiro parâmetro de tipo se refere à qual anotação será utilizada.
 * Segundo parâmetro de tipo se refere a quais tipos de objetos a anotação poderá ser associada.
 */
public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {

	private int numeroMultiplo;
	
	@Override
		public void initialize(Multiplo constraintAnnotation) {
			// Repassa o valor associado à anotação para variável interna do validador
			this.numeroMultiplo = constraintAnnotation.numero();
		}
	
	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		
		boolean valido = true;
		
		// Regra para validação, se o atributo anotado (seu valor) é múltiplo do número definido na anotação
		if (value != null) {
			var  valorDecimal = BigDecimal.valueOf(value.doubleValue());
			var multiploDecimal = BigDecimal.valueOf(this.numeroMultiplo);
			var resto = valorDecimal.remainder(multiploDecimal);
			
			valido = BigDecimal.ZERO.compareTo(resto) == 0;
		}
		
		return valido;
	}

}

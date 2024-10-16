package com.algaworks.algafood.core.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Anotação customizada para validação a nível de classe.
 */
@Target({ ElementType.TYPE }) // Limita o uso à definição de tipo/classe
@Retention(RUNTIME)
@Constraint(validatedBy = { ValorZeroIncluirDescricaoValidator.class })
public @interface ValorZeroIncluirDescricao {

	String message() default "descrição obrigatória inválida";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	String valorField();
	
	String descricaoField();
	
	String descricaoObrigatoria();
}

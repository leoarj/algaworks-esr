package com.algaworks.algafood.core.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * O elemento anotado deve respeitar os formatos de arquivo informados.<b></b>
 * @implNote Chave <b>FileContentType.message</b> para personalizar mensagem de validação no arquivo de mensagens.
 * @implNote Expressão <b>${allowedValues}</b> para interpolar as extensões de arquivos permitidos na mensagem de validação.
 * @implSpec Mais detalhes na implementação {@link FileContentTypeValidator}
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { FileContentTypeValidator.class })
public @interface FileContentType {
	
	public static final String ALLOWED_CONTENT_TYPES_EXPRESSION_VARIABLE = "allowedValues";
	
	String message() default "{FileContentType.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	String[] allowed();
	
}

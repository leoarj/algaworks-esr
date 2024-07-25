package com.algaworks.algafood.core.validation;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementação para validar elemento anotado com {@link FileContentType}
 * @implNote Utiliza customização do contexto de validação para personalizar mensagens.
 * @implNote Utilize a expressão <b>${allowedMediaTypes}</b> para interpolar as extensões de arquivos permitidos na mensagem de validação.
 * @see {@linkplain https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#_custom_contexts}
 */
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private static final Class<HibernateConstraintValidatorContext>
		CONSTRAINT_VALIDATOR_CONTEXT_PROVIDER_CLASS = HibernateConstraintValidatorContext.class;
	
	private static final String ALLOWED_MEDIA_TYPES_EXPRESSION_VARIABLE_NAME = "allowedMediaTypes";
	
	private Set<String> allowedMediaTypes;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
		allowedMediaTypes = Set.of(constraintAnnotation.allowed());
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		// Retorna instância do provedor de contexto de validação para acesso a APIs específicas (Hibernate Validator)
		context
			.unwrap(CONSTRAINT_VALIDATOR_CONTEXT_PROVIDER_CLASS)
			.addExpressionVariable(ALLOWED_MEDIA_TYPES_EXPRESSION_VARIABLE_NAME, allowedMediaTypes.toString()); //${}
			// ou
			//.addMessageParameter(ALLOWED_MEDIA_TYPES_EXPRESSION_VARIABLE_NAME, allowedMediaTypes.toString()); //{}
		
		return value == null || allowedMediaTypes.contains(value.getContentType());
	}

}

package com.algaworks.algafood.core.validation;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementação para validar elemento anotado com {@link FileContentType}
 * @implNote Utiliza customização do contexto de validação para personalizar mensagens.
 * @see {@linkplain https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#_custom_contexts}
 */
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private Set<String> allowedContentTypes;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
		allowedContentTypes = Set.of(constraintAnnotation.allowed());
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		// Retorna instância do provedor de contexto de validação para acesso a APIs específicas (Hibernate Validator)
		context
			.unwrap(HibernateConstraintValidatorContext.class)
			.addExpressionVariable(FileContentType.ALLOWED_CONTENT_TYPES_EXPRESSION_VARIABLE, allowedContentTypes.toString()); //${}
			// ou
			//.addMessageParameter(ALLOWED_MEDIA_TYPES_EXPRESSION_VARIABLE_NAME, allowedMediaTypes.toString()); //{}
		
		return value == null || allowedContentTypes.contains(value.getContentType());
	}

}

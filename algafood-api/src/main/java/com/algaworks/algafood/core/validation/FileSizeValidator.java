package com.algaworks.algafood.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

/**
 * Encapsula regra de validação para ser utilizada como "constraint" de validação.
 * 
 * Primeiro parâmetro de tipo se refere à qual anotação será utilizada.
 * Segundo parâmetro de tipo se refere a quais tipos de objetos a anotação poderá ser associada.
 */
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

	private DataSize maxSize;
	
	@Override
	public void initialize(FileSize constraintAnnotation) {
		// Repassa o valor associado à anotação para variável interna do validador
		this.maxSize = DataSize.parse(constraintAnnotation.max());
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		return value == null || value.getSize() <= this.maxSize.toBytes();
	}

}

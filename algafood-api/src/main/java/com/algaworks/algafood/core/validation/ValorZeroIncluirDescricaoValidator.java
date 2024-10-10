package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

import org.springframework.beans.BeanUtils;

public class ValorZeroIncluirDescricaoValidator implements ConstraintValidator<ValorZeroIncluirDescricao, Object> {

	private String valorField;
	private String descricaoField;
	private String descricaoObrigatoria;
	
	@Override
	public void initialize(ValorZeroIncluirDescricao constraint) {
		this.valorField = constraint.valorField();
		this.descricaoField = constraint.descricaoField();
		this.descricaoObrigatoria = constraint.descricaoObrigatoria();
	}
	
	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		boolean valido = true;
		
		try {
			/**
			 * Captura do objeto, com o auxílio da classe BeanUtils do Spring
			 * as propriedades definidas na anotação a nível de classe.
			 * 
			 * Obtém acesso aos getters, invocando o método no objeto de validação.
			 * 
			 * Caso não seja possível ler as propriedades uma exceção será
			 * lançada, caputurada e relançada como uma exceção apropriada (ValidationException).
			 */
			BigDecimal valor = (BigDecimal) BeanUtils
					.getPropertyDescriptor(objetoValidacao.getClass(), valorField)
					.getReadMethod()
					.invoke(objetoValidacao);
			
			String descricao = (String) BeanUtils
					.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField)
					.getReadMethod()
					.invoke(objetoValidacao);
			
			if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
				valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
			}
			
			return valido;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

}

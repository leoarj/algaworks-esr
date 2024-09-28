package com.algaworks.algafood.core.springdoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;


@Parameter(
		in = ParameterIn.QUERY,
		name = "page",
		description = "Número da página (0..N)",
		schema = @Schema(type = "integer", defaultValue = "0")
		)
@Parameter(
		in = ParameterIn.QUERY,
		name = "size",
		description = "Quantidade de elementos por página",
		schema = @Schema(type = "integer", defaultValue = "10")
		)
@Parameter(
		in = ParameterIn.QUERY,
		name = "sort",
		description = "Critério de ordenação: propriedade(asc|desc)",
		examples = {
				@ExampleObject("nome"),
				@ExampleObject("nome,asc"),
				@ExampleObject("nome,desc")
			}
		)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
/**
 * Anotação para agrupar propriedades referentes ao modelo de representação de objeto de paginação (Page).
 * Anota a propriedade em endpoint de controlador que utilize de paginação.
 */
public @interface PageableParameter {

}

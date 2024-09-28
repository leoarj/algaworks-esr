package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(name = "Problema")
@JsonInclude(Include.NON_NULL) // Para não incluir propriedades nulas na serialização/deserialização
@Getter
@Builder
public class Problem {
	
	@Schema(example = "400")
	private Integer status;
	
	@Schema(example = "2019-12-01T18:09:02.70844Z")
	private OffsetDateTime timestamp;
	
	@Schema(example = "https://algafood.com.br/dados-invalidos")
	private String type;
	
	@Schema(example = "Dados inválidos")
	private String title;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String detail;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String userMessage;
	
	@Schema(description = "Lista de objetos ou campos que geraram o erro (opcional)")
	private List<Object> objects;
	
	/**
	 * Classe estática interna para definir tipo aninhado Field,
	 * que representa o campo e erro associado ao mesmo no corpo de uma requisição.
	 */
	@Schema(name = "ObjetoProblema")
	@Getter
	@Builder
	public static class Object {
		@Schema(example = "preco")
		private String name;
		
		@Schema(example = "O preço é obrigatório")
		private String userMessage;
	}
	
}

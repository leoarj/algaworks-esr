package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL) // Para não incluir propriedades nulas na serialização/deserialização
@Getter
@Builder
public class Problem {
	
	private Integer status;
	private LocalDateTime timestamp;
	private String type;
	private String title;
	private String detail;
	private String userMessage;
	private List<Object> objects;
	
	/**
	 * Classe estática interna para definir tipo aninhado Field,
	 * que representa o campo e erro associado ao mesmo no corpo de uma requisição.
	 */
	@Getter
	@Builder
	public static class Object {
		private String name;
		private String userMessage;
	}
	
}

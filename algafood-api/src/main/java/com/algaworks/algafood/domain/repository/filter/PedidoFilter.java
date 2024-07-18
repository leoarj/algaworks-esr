package com.algaworks.algafood.domain.repository.filter;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa propriedades de filtro
 * que podem ser aplicadas ao recuros de Pedidos.
 */
@Getter
@Setter
public class PedidoFilter {
	
	private Long clienteId;
	private Long restauranteId;
	
	// Para o Spring saber qual formato para parse de data/hora (ISO 8601)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataCriacaoInicio;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataCriacaoFim;

}

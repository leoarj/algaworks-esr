package com.algaworks.algafood.domain.model.dto;

import java.math.BigDecimal;
//import java.time.LocalDate;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendaDiaria {
	
	@Schema(example = "2019-12-01T20:34:04Z")
	private Date data;
	
	@Schema(example = "20")
	private Long totalVendas;
	
	@Schema(example = "308.90")
	private BigDecimal totalFaturado;

	public VendaDiaria(java.sql.Date data, Long totalVendas, BigDecimal totalFaturado) {
		// Spring Boot 3 e Jakarta EE9
		// porque sendo compativeis, o Jakarta EE 9 tenta transformar
		// o java.sql.Date com toInstant(), o qual não ele não suporta.
		// Causaria erro no PdfVendaReportService.
		this.data = new Date(data.getTime());
		this.totalVendas = totalVendas;
		this.totalFaturado = totalFaturado;
	}
}

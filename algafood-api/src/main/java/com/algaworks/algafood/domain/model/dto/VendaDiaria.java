package com.algaworks.algafood.domain.model.dto;

import java.math.BigDecimal;
//import java.time.LocalDate;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class VendaDiaria {
	
	@Schema(example = "2019-12-01T20:34:04Z")
	private Date data;
	
	@Schema(example = "20")
	private Long totalVendas;
	
	@Schema(example = "308.90")
	private BigDecimal totalFaturado;
	
}

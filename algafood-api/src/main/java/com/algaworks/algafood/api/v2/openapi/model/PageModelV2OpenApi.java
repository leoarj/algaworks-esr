package com.algaworks.algafood.api.v2.openapi.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe para modelar a representação de Page na documentação da API.
 */
//@ApiModel("PageModel")
@Getter
@Setter
public class PageModelV2OpenApi {

//	@ApiModelProperty(example = "10", value = "Quantidade de registros por página")
	private Long size;
	
//	@ApiModelProperty(example = "50", value = "Total de registros")
	private Long totalElements;
	
//	@ApiModelProperty(example = "5", value = "Total de páginas")
	private Long totalPages;
	
//	@ApiModelProperty(example = "0", value = "Número da página (começa em 0)")
	private Long number;
	
}

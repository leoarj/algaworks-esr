package com.algaworks.algafood.client.input;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnderecoInput {

	private String cep;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private CidadeIdInput cidade;
}

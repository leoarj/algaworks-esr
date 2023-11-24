package com.algaworks.algafood;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.di.Service.AtivacaoClienteService;

@Configuration
public class ServiceConfig {

	
	/**
	 * Exemplo de configuração dos métodos de
	 * pós-inicialização e pré-destruição do bean.
	 * Os métodos devem existir na classe e devem ter
	 * exatamento os nomes indicados na anotação.
	 */
	@Bean(initMethod = "init", destroyMethod = "destroy")
	public AtivacaoClienteService ativacaoClienteService() {
		return new AtivacaoClienteService();
	}
	
}

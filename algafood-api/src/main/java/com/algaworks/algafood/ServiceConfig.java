package com.algaworks.algafood;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.di.Service.AtivacaoClienteService;
import com.algaworks.algafood.di.notificacao.Notificador;
  
/*
 * Classe de definição exclusiva para AtivacaoClienteService.
 */

@Configuration
public class ServiceConfig {

	/*
	 * Como AtivacaoClienteService tem uma dependência de Notificador
	 * e como os beans estão definidos em classes de configuração diferentes
	 * então uma dependência de notificador pode ser injetada via parâmetro
	 * e o container IoC já saberá o que deve injetar.
	 */
	
	@Bean
	public AtivacaoClienteService ativacaoClienteService(Notificador notificador) {
		return new AtivacaoClienteService(notificador);
	}
	
	
}

package com.algaworks.algafood.di.notificacao;

//import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;

/*
 * Se anotarmos mais de um canditato com @Primary,
 * obteremos uma "org.springframework.beans.factory.NoUniqueBeanDefinitionException".
 */

//@Primary
@Component
public class NotificadorEmail implements Notificador {
	
	@Override
	public void notificar(Cliente cliente, String mensagem) {
		System.out.printf("Notificando %s através do e-mail %s: %s\n", 
				cliente.getNome(), cliente.getEmail(), mensagem);
	}
}

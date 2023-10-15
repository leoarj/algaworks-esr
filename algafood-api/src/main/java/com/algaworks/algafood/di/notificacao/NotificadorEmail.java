package com.algaworks.algafood.di.notificacao;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;

/*
 * Revertendo NotificadorEmail anotado com @Component para testar
 * anotação @Autowired.
 */

/*
 * NotificadorEmail não estando como componente.
 * Como classe que o utiliza anotou como required = false,
 * então não há problemas no boot da aplicação.
 */

//@Component
public class NotificadorEmail implements Notificador {
	
	@Override
	public void notificar(Cliente cliente, String mensagem) {
		System.out.printf("Notificando %s através do e-mail %s: %s\n", 
				cliente.getNome(), cliente.getEmail(), mensagem);
	}
}

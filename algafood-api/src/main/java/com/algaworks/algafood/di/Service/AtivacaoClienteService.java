package com.algaworks.algafood.di.Service;

//import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.Notificador;

/*
 * Com uma classe de Config para retonar um bean de AtivacaoClienteService,
 * não há necessidade de anotar AtivacaoClienteService com @Component,
 * removendo assim um acoplamento com o Spring.
 */

//@Component
public class AtivacaoClienteService {

	private Notificador notificador;
	
	public AtivacaoClienteService(Notificador notificador) {
		this.notificador = notificador;
		
		System.out.println("AtivacaoClienteService: " + notificador);
	}


	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
	}
	
}

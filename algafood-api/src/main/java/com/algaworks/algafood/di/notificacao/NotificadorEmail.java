package com.algaworks.algafood.di.notificacao;

//import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;

/*
 * Com uma classe de Config para retonar um bean de NotificadorEmail,
 * não há necessidade de anotar NotificadorEmail com @Component,
 * removendo assim um acoplamento com o Spring.
 * 
 * Obs.: Utilizar essa dependência como referência de interface (Notificador).
 */

//@Component
public class NotificadorEmail implements Notificador {

	private boolean caixaAlta;
	private String hostServidorSmtp;
	
	public NotificadorEmail(String hostServidorSmtp) {
		this.hostServidorSmtp = hostServidorSmtp;
		System.out.println("NotificadorEmail");
	}
	
	@Override
	public void notificar(Cliente cliente, String mensagem) {
		if (this.caixaAlta) {
			mensagem = mensagem.toUpperCase();
		}
		
		System.out.printf("Notificando %s através do e-mail %s usando SMTP %s: %s\n", 
				cliente.getNome(), cliente.getEmail(), this.hostServidorSmtp, mensagem);
	}

	public void setCaixaAlta(boolean caixaAlta) {
		this.caixaAlta = caixaAlta;
	}
	
}

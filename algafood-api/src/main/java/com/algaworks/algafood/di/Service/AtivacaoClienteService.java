package com.algaworks.algafood.di.Service;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.NotificadorEmail;

@Component
public class AtivacaoClienteService {

	private NotificadorEmail notificador;
	
	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		notificador.notificarCliente(cliente, "Seu cadastro no sistema está ativo!");
	}
	
}

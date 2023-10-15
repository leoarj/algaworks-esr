package com.algaworks.algafood.di.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.Notificador;

@Component
public class AtivacaoClienteService {
	
	/*
	 * Qualificando qual bean será injetado.
	 * Mesma anotação deve estar no bean fornecido.
	 */
	
	@Qualifier("urgente")
	@Autowired
	private Notificador notificador;
	

	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
		
	}
	
}

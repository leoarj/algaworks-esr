package com.algaworks.algafood.di.Service;

import com.algaworks.algafood.di.modelo.Cliente;

/**
 * Representa um evento de ativacão de um Cliente.
 * Encapsula a instância do cliente ativado.
 * Os eventos gerados dessa classe serão consumidos
 * por métodos anotados com @EventListener.
 */
public class ClienteAtivadoEvent {

	private Cliente cliente;

	public ClienteAtivadoEvent(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
}

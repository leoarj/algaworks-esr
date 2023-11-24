package com.algaworks.algafood.di.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;

@Component
public class AtivacaoClienteService {
	
	// Removido do serviço de ativação a responsabilidade específica de notificar.
	
	/**
	 * Componente do Spring para disparar eventos dentro da aplicação.
	 * Esses eventos vão ser capturados por outros componentes
	 * e decidirão o que executar com base no evento recebido.
	 * */
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		//notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
		eventPublisher.publishEvent(new ClienteAtivadoEvent(cliente));
		
	}
	
}

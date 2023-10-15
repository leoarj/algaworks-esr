package com.algaworks.algafood.di.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.Notificador;

@Component
public class AtivacaoClienteService {

	/*
	 * Injetado beans como uma lista, para realizar a desambiguação.
	 * Útil nos casos em que todas as implementações da dependência
	 * podem ou devem ser executadas.
	 * 
	 * Observar que o consumo das dependências deve ser modificado para uma iteração.
	 */
	
	@Autowired
	private List<Notificador> notificadores;
	

	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		notificadores.forEach(
				notificador -> notificador.notificar(cliente, "Seu cadastro no sistema está ativo!"));
		
//		for (Notificador notificador : notificadores) {
//			notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
//		}
		
	}
	
}

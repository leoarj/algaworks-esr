package com.algaworks.algafood.di.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.Notificador;

/*
 * Revertendo NotificadorEmail anotado com @Component para testar
 * anotação @Autowired.
 */

@Component
public class AtivacaoClienteService {

	/*
	 * Torna a dependência opcional.
	 */
	
	@Autowired(required = false)
	private Notificador notificador;
	
	/*
	 * Uso mais comum é no construtor, por questões de convenção
	 * e clareza das dependências da classe.
	 * 
	 * Como há dois construtores como exemplo,
	 * um deles deve ser anotado com @Autowired para que o container
	 * saiba em qual gerenciar a injeção de dependência,
	 * mesmo que o tipo dos argumentos sejam diferentes.
	 */
	
//	@Autowired
//	public AtivacaoClienteService(Notificador notificador) {
//		this.notificador = notificador;
//		
//		System.out.println("AtivacaoClienteService: " + notificador);
//	}
	
//	public AtivacaoClienteService(String qualquer) {}


	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		if (notificador != null) {
			notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
		} else {
			System.out.println("Não existe notificador, mas cliente foi ativado");
		}
	}
	
//	@Autowired
//	public void setNotificador(Notificador notificador) {
//		this.notificador = notificador;
//	}
	
}

package com.algaworks.algafood.di.notificacao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;

/*
* Anotação @Profile diz que o componente ficará disponível em determinado ambiente (desenvolvimento).
*/

//@Profile("dev")
//@TipoDoNotificador(NivelUrgencia.SEM_URGENCIA)
//@Component // Comentado para não ser escaneado e gerenciado pelo container.
public class NotificadorEmailMock implements Notificador {
	
	public NotificadorEmailMock() {
		System.out.println("NotificadorEmail MOCK");
	}
	
	@Override
	public void notificar(Cliente cliente, String mensagem) {
		System.out.printf("MOCK: Notificação seria enviada para %s através do e-mail %s: %s\n", 
				cliente.getNome(), cliente.getEmail(), mensagem);
	}

}
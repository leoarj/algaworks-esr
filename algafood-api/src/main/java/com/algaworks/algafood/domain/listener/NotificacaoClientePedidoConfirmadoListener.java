package com.algaworks.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

/**
 * Representa um listener para um evento de domínio referente a confirmação de pedido,<br>
 * onde sua responsabilidade é o envio de notificação para o cliente.
 */
@Component
public class NotificacaoClientePedidoConfirmadoListener {

	@Autowired
	private EnvioEmailService envioEmailService;
	
	@EventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo("pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		envioEmailService.enviar(mensagem);
	}	
}

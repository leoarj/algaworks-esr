package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.domain.service.EnvioEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j // Habilita suporte para logger
public class FakeEnvioEmailService implements EnvioEmailService {
	
	@Autowired
	private ProcessadorEmailTemplate processadorEmailTemplate;
	
	@Override
	public void enviar(Mensagem mensagem) {	
		String corpo = processadorEmailTemplate.processarTemplate(mensagem);
		
		StringBuilder builderEmailFake = new StringBuilder();
		
		builderEmailFake
			.append("\n==========[ FAKE E-MAIL ]==========\n")
			//.append("-> REMETENTE..........: " + emailProperties.getRemetente() + "\n")
			.append("-> DESTINATÁRIO(S)....: " + mensagem.getDestinatarios().toString() + "\n")
			.append("-> ASSUNTO............: " + mensagem.getAssunto() + "\n")
			.append("-> CORPO: " + "\n")
			.append(corpo)
			.append("\n==========[ FIM FAKE E-MAIL ]==========");
		
		log.info(builderEmailFake.toString());
	}
}

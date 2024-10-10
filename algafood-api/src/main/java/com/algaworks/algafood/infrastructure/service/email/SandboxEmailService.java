package com.algaworks.algafood.infrastructure.service.email;

public class SandboxEmailService extends SmtpEnvioEmailService {

	@Override
	public void enviar(Mensagem mensagem) {
		Mensagem mensagemCopia = Mensagem.builder()
				.destinatario(emailProperties.getSandbox().getDestinatario())
				.assunto("SANDBOX - " + mensagem.getAssunto())
				.corpo(mensagem.getCorpo())
				.variaveis(mensagem.getVariaveis())
			.build();
	
		super.enviar(mensagemCopia);
	}	
}

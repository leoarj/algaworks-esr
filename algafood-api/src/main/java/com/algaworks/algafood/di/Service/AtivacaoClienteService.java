package com.algaworks.algafood.di.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.di.modelo.Cliente;
import com.algaworks.algafood.di.notificacao.NivelUrgencia;
import com.algaworks.algafood.di.notificacao.Notificador;
import com.algaworks.algafood.di.notificacao.TipoDoNotificador;

//@Component
public class AtivacaoClienteService {
	
	/*
	 * Qualificando qual bean será injetado.
	 * Mesma anotação deve estar no bean fornecido.
	 */
	
	@TipoDoNotificador(NivelUrgencia.SEM_URGENCIA)
	@Autowired
	private Notificador notificador;
	
	/**
	 * Anotação @PostConstruct indica um método
	 * que deve ser executado após a criação do objeto.
	 */
	//@PostConstruct
	public void init() {
		System.out.println("INIT " + notificador);
	}
	
	/**
	 * Anotação @PreDestroy indica um método
	 * que deve ser executado antes da destruição do objeto.
	 */
	//@PreDestroy
	public void destroy() {
		System.out.println("DESTROY");
	}
	
	public void ativarCliente(Cliente cliente) {
		cliente.ativar();
		
		notificador.notificar(cliente, "Seu cadastro no sistema está ativo!");
		
	}
	
}

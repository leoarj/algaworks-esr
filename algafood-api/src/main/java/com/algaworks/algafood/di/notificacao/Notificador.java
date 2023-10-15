package com.algaworks.algafood.di.notificacao;

import com.algaworks.algafood.di.modelo.Cliente;

public interface Notificador {

	void notificarCliente(Cliente cliente, String mensagem);

}
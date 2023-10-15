package com.algaworks.algafood.di.notificacao;

import com.algaworks.algafood.di.modelo.Cliente;

/*
 * Contrato para notificador.
 * Obs.: Utilizar referência de interface nas classes que possuem
 * essa dependência para não gerar acoplamento.
 */

public interface Notificador {

	void notificar(Cliente cliente, String mensagem);

}
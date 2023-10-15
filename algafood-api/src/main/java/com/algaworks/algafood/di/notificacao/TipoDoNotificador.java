package com.algaworks.algafood.di.notificacao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

/*
 * Para utilizar a anotação customizada a mesma
 * deve ser anotada com:
 * - @Retention = Em que nível deve ser mantida (lida),
 * no caso em tempo de execução.
 * 
 * - @Qualifier = Diz que a anotação é um qualificador,
 * e com isso pode ser usada como qualificador para beans.
 */

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface TipoDoNotificador {

	NivelUrgencia value();
	
}

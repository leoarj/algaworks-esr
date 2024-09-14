package com.algaworks.algafood;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.algaworks.algafood.core.io.Base64ProtocolResolver;
import com.algaworks.algafood.infrastructure.repository.CustomJpaRepositoryImpl;

@SpringBootApplication
// Para habilitar repositório customizado.
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class AlgafoodApiApplication {

	public static void main(String[] args) {
		// Para configurar timezone padrão na aplicação
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		// Alternativa sem META-INF/spring.factories
//		var app = new SpringApplication(AlgafoodApiApplication.class);
//		app.addListeners(new Base64ProtocolResolver()); // registra o resolvedor de base64 para que o framework possa utilizá-lo
//		app.run(args);
		
		SpringApplication.run(AlgafoodApiApplication.class, args);
	}

}

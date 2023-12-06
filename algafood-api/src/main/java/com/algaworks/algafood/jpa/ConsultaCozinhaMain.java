package com.algaworks.algafood.jpa;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

public class ConsultaCozinhaMain {
	
	public static void main(String[] args) {
		
		/**
		 * Objeto ApplicationContext para obter um contexto da aplicação AlgafoodApiApplication.
		 * Informando que a aplicação não é WEB, para não ficar aguardando requisições.
		 * 
		 * Com o contexto da aplicação fica possível acessar os beans gerenciados pelo container IoC.
		 */
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		CozinhaRepository cozinhaReposity = applicationContext.getBean(CozinhaRepository.class);
		
		List<Cozinha> cozinhas = cozinhaReposity.listar();
		
		cozinhas.forEach(cozinha -> System.out.println(cozinha.getNome()));
	}

}

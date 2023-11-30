package com.algaworks.algafood.jpa;

import java.util.stream.Stream;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaReposity;

public class InclusaoCozinhaMain {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		CozinhaReposity cozinhaReposity = applicationContext.getBean(CozinhaReposity.class);
		
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Brasileira");
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Japonesa");
		
		cozinha1 = cozinhaReposity.salvar(cozinha1);
		cozinha2 = cozinhaReposity.salvar(cozinha2);
		
		Stream.of(cozinha1, cozinha2)
			.forEach(cozinha -> System.out.printf("%d - %s\n", cozinha.getId(), cozinha.getNome()));
	}
	
}
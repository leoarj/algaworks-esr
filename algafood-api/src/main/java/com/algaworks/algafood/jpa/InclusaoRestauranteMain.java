package com.algaworks.algafood.jpa;

import java.util.stream.Stream;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

public class InclusaoRestauranteMain {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		RestauranteRepository restauranteRepository = applicationContext.getBean(RestauranteRepository.class);
		
		Restaurante restaurante1 = new Restaurante();
		restaurante1.setNome("Restaurante do Italiano");
		
		Restaurante restaurante2 = new Restaurante();
		restaurante2.setNome("Restaurante Bem Servido");
		
		restaurante1 = restauranteRepository.save(restaurante1);
		restaurante2 = restauranteRepository.save(restaurante2);
		
		Stream.of(restaurante1, restaurante2)
			.forEach(cozinha -> System.out.printf("%d - %s\n", cozinha.getId(), cozinha.getNome()));
	}
	
}
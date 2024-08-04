package com.algaworks.algafood.client;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.web.client.RestTemplate;

import com.algaworks.algafood.client.api.ClientApiException;
import com.algaworks.algafood.client.api.RestauranteClient;
import com.algaworks.algafood.client.input.CidadeIdInput;
import com.algaworks.algafood.client.input.CozinhaIdInput;
import com.algaworks.algafood.client.input.EnderecoInput;
import com.algaworks.algafood.client.input.RestauranteInput;
import com.algaworks.algafood.client.model.RestauranteModel;

public class InclusaoRestaurantesMain {

	public static void main(String[] args) {
		try {
			var cozinha = CozinhaIdInput.builder()
					.id(1L)
				.build();
			
			var cidade = CidadeIdInput.builder()
					.id(1L)
				.build();
			
			var endereco = EnderecoInput.builder()
					.cep("00000000")
					.logradouro("Av. Brasil")
					.numero("500")
					.bairro("Centro")
					.cidade(cidade)
				.build();
			
			var restaurante = RestauranteInput.builder()
					.nome("Comida matogrossense")
					.taxaFrete(new BigDecimal("7.00"))
					.cozinha(cozinha)
					.endereco(endereco)
				.build();
			
			RestTemplate restTemplate = new RestTemplate();
			
			RestauranteClient restauranteClient = new RestauranteClient(
					restTemplate, "http://api.algafood.local:8080");
			
			RestauranteModel restauranteSalvo = restauranteClient.adicionar(restaurante);
			
			System.out.println(restauranteSalvo);
		} catch (ClientApiException e) {
			if (Objects.nonNull(e.getProblem())) {
				System.out.println(e.getProblem().getUserMessage());
				
				if (Objects.nonNull(e.getProblem().getObjects())) {
					e.getProblem().getObjects().stream()
						.forEach(object -> System.out.println("- " + object.getUserMessage()));
				} 
			} else {
				System.out.println("Erro desconhecido");
				e.printStackTrace();
			}
		}
	}
}

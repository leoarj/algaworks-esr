package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Classe de teste para consulta e cadastro de restaurantes.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {
	
	private static final int RESTAURANTE_ID_INEXISTENTE = 10;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	private int quantidadeRestaurantesCadastrados;
	private Restaurante restauranteMarrocos;
	private String jsonCorretoRestauranteBrasileiro;
	private String jsonCorretoRestauranteBrasileiroFreteGratis;
	private String jsonIncorretoRestauranteBrasileiro;
	private String jsonIncorretoRestauranteBrasileiroTaxaFreteNegativa;
	private String jsonIncorretoRestauranteBrasileiroTaxaFreteGratisSemDescricaoObrigatoria;
	
	@BeforeEach
	public void setup() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		
		databaseCleaner.clearTables();
		prepararDados();
		
		jsonCorretoRestauranteBrasileiro = ResourceUtils.getContentFromResource("/json/correto/restaurante-brasileiro.json");
		jsonCorretoRestauranteBrasileiroFreteGratis = ResourceUtils.getContentFromResource("/json/correto/restaurante-brasileiro-taxafrete-gratis-descricao-obrigatoria.json");
		
		jsonIncorretoRestauranteBrasileiro = ResourceUtils.getContentFromResource("/json/inccorreto/restaurante-brasileiro.json");
		jsonIncorretoRestauranteBrasileiroTaxaFreteNegativa = ResourceUtils.getContentFromResource("/json/inccorreto/restaurante-brasileiro-taxafrete-negativa.json");
		jsonIncorretoRestauranteBrasileiroTaxaFreteGratisSemDescricaoObrigatoria = ResourceUtils.getContentFromResource("/json/inccorreto/restaurante-brasileiro-taxafrete-gratis-sem-descricao-obrigatoria.json");
	}
	
	// Status 200 quando consultar cozinhas
	@Test
	public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	// Quantidade correta de restaurantes
	@Test
	public void deveRetornarQuantidadeCorretaDeRestaurantes_QuandoConsultarRestaurantes() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeRestaurantesCadastrados));
	}
	
	
	// Status 201 quando cadastro restaurante correto
	@Test
	public void deveRetornar201_QuandoCadastrarRestaurante() {
		given()
			.body(jsonCorretoRestauranteBrasileiro)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	// Status 201 quando cadastro restaurante correto com taxa de frete grátis e descrição obrigatória
	@Test
	public void deveRetornar201_QuandoCadastrarRestauranteComTaxaFreteGratisComDescricaoObrigatoria() {
		given()
			.body(jsonCorretoRestauranteBrasileiroFreteGratis)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.body("nome", containsStringIgnoringCase("frete grátis"));
	}
	
	// Status 200 quando consultar restaurante existente
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
		given()
			.pathParam("restauranteId", restauranteMarrocos.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{restauranteId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(restauranteMarrocos.getNome()));
	}
	
	// Status 404 quando restaurante inexistente
	@Test
	public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
		given()
			.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{restauranteId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	// Status 400 quando cadastrar restaurante com cozinha inexistente
	@Test 
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
		given()
			.body(jsonIncorretoRestauranteBrasileiro)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComTaxaFreteNegativa() {
		given()
			.body(jsonIncorretoRestauranteBrasileiroTaxaFreteNegativa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComTaxaFreteZeroSemDescricaoGratis() {
		given()
			.body(jsonIncorretoRestauranteBrasileiroTaxaFreteGratisSemDescricaoObrigatoria)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	private void prepararDados() {
		Cozinha cozinhaJaponesa = new Cozinha();
		cozinhaJaponesa.setNome("Japonesa");
		cozinhaJaponesa = cozinhaRepository.save(cozinhaJaponesa);
		
		Cozinha cozinhaArabe = new Cozinha();
		cozinhaArabe.setNome("Árabe");
		cozinhaArabe = cozinhaRepository.save(cozinhaArabe);
		
		Cozinha cozinhaBrasileira = new Cozinha();
		cozinhaBrasileira.setNome("Brasileira");
		cozinhaRepository.save(cozinhaBrasileira);
		
		Restaurante restauranteTokyo = new Restaurante();
		restauranteTokyo.setNome("Restaurante Tokyo");
		restauranteTokyo.setTaxaFrete(new BigDecimal("10.00"));
		restauranteTokyo.setCozinha(cozinhaJaponesa);
		restauranteRepository.save(restauranteTokyo);
		
		restauranteMarrocos = new Restaurante();
		restauranteMarrocos.setNome("Restaurante Marrocos");
		restauranteMarrocos.setTaxaFrete(new BigDecimal("15.00"));
		restauranteMarrocos.setCozinha(cozinhaArabe);
		restauranteMarrocos = restauranteRepository.save(restauranteMarrocos);
		
		quantidadeRestaurantesCadastrados = (int) restauranteRepository.count();
	}

}

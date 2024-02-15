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
	
	private static final String RESTAURANTE_FRETE_GRATIS_DESCRICAO = "frete grátis";

	private static final int RESTAURANTE_ID_INEXISTENTE = 10;
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

    private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	
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
	private String jsonRestauranteCorreto;
	private String jsonRestauranteFreteGratisCorreto;
	private String jsonRestauranteCozinhaInexistente;
	private String jsonRestauranteTaxaFreteNegativa;
	private String jsonRestauranteTaxaFreteGratisSemDescricaoObrigatoria;
	
	// Obtidos a partir do exemplo
	private String jsonRestauranteSemFrete;
	private String jsonRestauranteSemCozinha;
	
	
	@BeforeEach
	public void setup() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		
		databaseCleaner.clearTables();
		prepararDados();
		
		jsonRestauranteCorreto = ResourceUtils.getContentFromResource(
				"/json/correto/restaurante-brasileiro.json");
		
		jsonRestauranteFreteGratisCorreto = ResourceUtils.getContentFromResource(
				"/json/correto/restaurante-brasileiro-taxafrete-gratis-descricao-obrigatoria.json");
		
		jsonRestauranteCozinhaInexistente = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-brasileiro-cozinha-inexistente.json");
		
		jsonRestauranteTaxaFreteNegativa = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-brasileiro-taxafrete-negativa.json");
		
		jsonRestauranteTaxaFreteGratisSemDescricaoObrigatoria = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-brasileiro-taxafrete-gratis-sem-descricao-obrigatoria.json");
		
		// Obtidos do exemplo do desafio
		jsonRestauranteSemFrete = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-brasileiro-sem-frete.json");
		
		jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-brasileiro-sem-cozinha.json");
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
			.body(jsonRestauranteCorreto)
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
			.body(jsonRestauranteFreteGratisCorreto)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.body("nome", containsStringIgnoringCase(RESTAURANTE_FRETE_GRATIS_DESCRICAO))
			.body("taxaFrete", equalTo(BigDecimal.ZERO.floatValue()));
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
			.body(jsonRestauranteCozinhaInexistente)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	// Status 400 quando cadastrar restaurante com taxa de frete negativa
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComTaxaFreteNegativa() {
		given()
			.body(jsonRestauranteTaxaFreteNegativa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}
	
	// Status 400 quando cadastrar restaurante com taxa de frete grátis sem descrição obrigatória
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComFreteGratisSemDescricaoObrigatoria() {
		given()
			.body(jsonRestauranteTaxaFreteGratisSemDescricaoObrigatoria)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("objects[0].userMessage", equalTo("descrição obrigatória inválida"));
	}
	
	// Testes fornecidos de exemplo
	@Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
        given()
            .body(jsonRestauranteSemFrete)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
        given()
            .body(jsonRestauranteSemCozinha)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
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

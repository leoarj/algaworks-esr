package com.algaworks.algafood;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.algaworks.algafood.core.io.Base64ProtocolResolver;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

/**
 * Classe de teste para consulta e cadastro de restaurantes.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
public class CadastroRestauranteIT {
	
	private static final String RESTAURANTE_FRETE_GRATIS_DESCRICAO = "frete grátis";

	private static final int RESTAURANTE_ID_INEXISTENTE = 10;
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

    private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	
//	@LocalServerPort
//	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private MockMvc mockMvc;
	
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
		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
//		RestAssuredMockMvc.port = port;
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
		RestAssuredMockMvc.mockMvc(mockMvc);
		RestAssuredMockMvc.basePath = "/v1/restaurantes";
		
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
	public void deveRetornarQuantidadeCorretaDeRestaurantes_QuandoConsultarRestaurantes() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			// No projeto foi implementado HATEOAS com HAL
			// Para evitar: java.lang.AssertionError: 1 expectation failed. JSON path doesn't match.
			.body("_embedded.restaurantes", hasSize(quantidadeRestaurantesCadastrados));
	}
	
	
	// Status 201 quando cadastro restaurante correto
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
		given()
//			.pathParam("restauranteId", restauranteMarrocos.getId())
			.accept(ContentType.JSON)
		.when()
//			.get("/{restauranteId}")
			.get(restauranteMarrocos.getId().toString())
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(restauranteMarrocos.getNome()));
	}
	
	// Status 404 quando restaurante inexistente
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
	public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
		given()
//			.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
//			.get("/{restauranteId}")
			.get(String.valueOf(RESTAURANTE_ID_INEXISTENTE))
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	// Status 400 quando cadastrar restaurante com cozinha inexistente
	@Test 
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
	// habilitar annotation no model de input
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
    @WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_RESTAURANTES"
			})
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
		
		// Devido a obrigatoriedade Endereco.Cidade
		Estado estado = new Estado();
		estado.setNome("MT");
		estadoRepository.save(estado);
		
		Cidade cidade = new Cidade();
		cidade.setNome("Cuiabá");
		cidade.setEstado(estado);
		cidadeRepository.save(cidade);
	}

}

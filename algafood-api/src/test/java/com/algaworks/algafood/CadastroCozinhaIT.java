package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Classe de testes para testar cadastro de cozinha.
 * 
 * Os testes seguem um padrão de nomenclatura, que deve dizer o sentido do teste.
 * 
 * Os testes são basicamente definidos em:
 * - Cenário = Atores (objetos) relacionados no teste.
 * - Ação = Interação entre os objetos que deverá produzir os resultados a serem validados.
 * - Validação = Verificação dos resultados obtidos a partir da ação,
 * onde os resultados já são esperados em um determinado estado.
 */
//@RunWith(SpringRunner.class) // JUnit4
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Para definir uma porta aleatória para o container de teste
@TestPropertySource("/application-test.properties") // Para utilizar propriedades personalizadas de teste
class CadastroCozinhaIT {

	// Para injetar a porta do servidor na variável
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	/**
	 * Método de callback para preparar a execuçãode cada teste.
	 * - Configura log para caso de falha das requisições/respostas.
	 * - Configura porta.
	 * - Configura rota.
	 */
	@BeforeEach
	public void setUp() {
		// Habilita log da requisição/resposta caso haja falha
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		// Migra/volta o estado do DB para cada teste
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	/**
	 * Teste de API, realizando chamada no recurso de /cozinhas
	 * e testando o endpoint get verificando se o código de status do retorno é 200 - OK.
	 */
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	/**
	 * Realiza teste validando corpo da resposta,
	 * utilizando a biblioteca hamcrest para abstrair lógica de correspondências.
	 */
	@Test
	public void deveConter2Cozinhas_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(2)); // Se no corpo da resposta existem 4 objetos (JSON)
			//.body("nome", hasItems("Indiana", "Tailandesa")); // Se para a chave "nome" existem os valores informados
	}
	
	/*
	 * Espera o retorno 201 - CREATED na criação de uma nova cozinha.
	 *
	 * Obs.: Vai quebrar o teste de verificação do corpo da resposta,
	 * já este teste vai alterar a quantidade de cozinhas cadastradas.
	 */
	@Test
	public void testRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body("{ \"nome\": \"Chinesa\" }")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		given()
			.pathParam("cozinhaId", 2) // Define e mapeia variável de caminho
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}") // Adiciona variável de caminho na requisição
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo("Americana"));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		given()
			.pathParam("cozinhaId", 100)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);

		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Americana");
		cozinhaRepository.save(cozinha2);		
	}

}

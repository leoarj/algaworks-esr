package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

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
class CadastroCozinhaIT {

	// Para injetar a porta do servidor na variável
	@LocalServerPort
	private int port;
	
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
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(4)) // Se no corpo da resposta existem 4 objetos (JSON)
			.body("nome", hasItems("Indiana", "Tailandesa")); // Se para a chave "nome" existem os valores informados
	}

}

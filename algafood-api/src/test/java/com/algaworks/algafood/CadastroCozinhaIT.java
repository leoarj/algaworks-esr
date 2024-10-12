package com.algaworks.algafood;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.algaworks.algafood.core.io.Base64ProtocolResolver;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

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
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // Para definir uma porta aleatória para o container de teste
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties") // Para utilizar propriedades personalizadas de teste
@ContextConfiguration(initializers = Base64ProtocolResolver.class) // Para inicialização do conversor Base64 no contexto de testes de integração
class CadastroCozinhaIT {

	private static final int COZINHA_ID_INEXISTENTE = 100;

	// Para injetar a porta do servidor na variável
//	@LocalServerPort
//	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private MockMvc mockMvc;
	
	private int quantidadeCozinhasCadastradas;
	private Cozinha cozinhaAmericana;
	private String jsonCorretoCozinhaChinesa;
	
	
	/**
	 * Método de callback para preparar a execuçãode cada teste.
	 * - Configura log para caso de falha das requisições/respostas.
	 * - Configura porta.
	 * - Configura rota.
	 */
	@BeforeEach
	public void setUp() {
		// Habilita log da requisição/resposta caso haja falha
		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
//		RestAssuredMockMvc.port = port;
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
		RestAssuredMockMvc.mockMvc(mockMvc);
		RestAssuredMockMvc.basePath = "/v1/cozinhas";
		
		// Migra/volta o estado do DB para cada teste
		databaseCleaner.clearTables();
		prepararDados();
		
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json");
	}
	
	/**
	 * Teste de API, realizando chamada no recurso de /cozinhas
	 * e testando o endpoint get verificando se o código de status do retorno é 200 - OK.
	 */
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_COZINHAS"
			})
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
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_COZINHAS"
			})
	public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("_embedded.cozinhas", hasSize(quantidadeCozinhasCadastradas)); // Se no corpo da resposta existem 4 objetos (JSON)
			//.body("nome", hasItems("Indiana", "Tailandesa")); // Se para a chave "nome" existem os valores informados
	}
	
	/*
	 * Espera o retorno 201 - CREATED na criação de uma nova cozinha.
	 *
	 * Obs.: Vai quebrar o teste de verificação (quando não tem a limpeza das tabelas) do corpo da resposta,
	 * já este teste vai alterar a quantidade de cozinhas cadastradas.
	 */
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_COZINHAS"
			})
	public void testRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body(jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_COZINHAS"
			})
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		given()
//			.pathParam("cozinhaId", cozinhaAmericana.getId()) // Define e mapeia variável de caminho
			.accept(ContentType.JSON)
		.when()
//			.get("/{cozinhaId}") // Adiciona variável de caminho na requisição
			.get(cozinhaAmericana.getId().toString())
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(cozinhaAmericana.getNome()));
	}
	
	@Test
	@WithMockUser(
			username = "joao.ger@algafood.com",
			authorities = {
					"SCOPE_READ",
					"SCOPE_WRITE",
					"EDITAR_COZINHAS"
			})
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		given()
//			.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
//			.get("/{cozinhaId}")
			.get(String.valueOf(COZINHA_ID_INEXISTENTE))
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		cozinhaRepository.save(cozinhaTailandesa);

		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);
		
		quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}

}

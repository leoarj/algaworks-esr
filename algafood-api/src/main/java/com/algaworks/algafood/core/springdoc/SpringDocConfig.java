package com.algaworks.algafood.core.springdoc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
//import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.openapi.model.LinksModelOpenApi;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
/*
 * Para configurr o esquema de segurança no teste da API.
 * No caso, está configurando para usar OAtuth2 com o fluxo Authorization Code.
 */
@SecurityScheme(name = "security_auth",
		type = SecuritySchemeType.OAUTH2,
		flows = @OAuthFlows(authorizationCode = @OAuthFlow(
				authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
				tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
				scopes = {
						@OAuthScope(name = "READ", description = "read scope"),
						@OAuthScope(name = "WRITE", description = "write scope")
				})))
public class SpringDocConfig {

	private static final String BAD_REQUEST_RESPONSE = "BadRequestResponse";
    private static final String NOT_FOUND_RESPONSE = "NotFoundResponse";
    private static final String NOT_ACCEPTABLE_RESPONSE = "NotAcceptableResponse";
    private static final String INTERNAL_SERVER_ERROR_RESPONSE = "InternalServerErrorResponse";
	
	@Bean
	public OpenAPI openAPI() {
		// Realiza a substituição de Links, para representação mais simples no modelo
		SpringDocUtils.getConfig()
			.replaceWithClass(org.springframework.hateoas.Links.class, LinksModelOpenApi.class);
		
		return new OpenAPI()
				.info(new Info()
						.title("AlgaFood API")
						.version("v1")
						.description("REST API do AlgaFood")
						.license(new License()
								.name("Apache 2.0")
								.url("https://springdoc.com")
								)
						).externalDocs(new ExternalDocumentation()
								.description("AlgaWorks")
								.url("https://algaworks.com")
						).tags(Arrays.asList(
								new Tag().name("Cidades").description("Gerencia as cidades"),
								new Tag().name("Grupos").description("Gerencia os grupos"),
								new Tag().name("Cozinhas").description("Gerencia as cozinhas"),
								new Tag().name("Formas de pagamento").description("Gerencia as formas de pagamento"),
								new Tag().name("Pedidos").description("Gerencia os pedidos"),
								new Tag().name("Restaurantes").description("Gerencia os restaurantes"),
								new Tag().name("Estados").description("Gerencia os estados"),
								new Tag().name("Produtos").description("Gerencia os produtos"),
								new Tag().name("Usuários").description("Gerencia os usuários"),
								new Tag().name("Estatísticas").description("Estatísticas da AlgaFood"),
								new Tag().name("Permissões").description("Gerencia as permissões")
						)).components(new Components()
								.schemas(gerarSchemas())
								.responses(gerarResponses())
						);
	}
	
	/**
	 * Configura um customizador para a documentaçãp.
	 * Recebe o bean registrado da documentação e personaliza propriedades.
	 */
	@Bean
	public OpenApiCustomiser openApiCustomiser() {
		return openApi -> {
			openApi.getPaths().values()
					.forEach(pathItem -> pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
						ApiResponses responses = operation.getResponses();

						responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
						
						switch (httpMethod) {
						case GET:
							responses.addApiResponse("406", new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
							break;
						case POST:
							responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
							break;
						case PUT:
							responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
							break;
//						case DELETE:
//							break;
						default:
							responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
							break;
						}

					}));

			// .stream()
			// .flatMap(pathItem -> pathItem.readOperations().stream())
			// .forEach(operation -> {
			// // Recupera respostas já registradas
			// ApiResponses responses = operation.getResponses();
			//
			// // Cria respostas personalizadas
			// ApiResponse apiResponseNaoEncontrado = new ApiResponse().description("Recurso
			// não encontrado");
			// ApiResponse apiResponseErroInterno = new ApiResponse().description("Erro
			// interno no servidor");
			// ApiResponse apiResponseSemRepresentacao = new ApiResponse()
			// .description("Recurso não possui uma representação que poderia ser aceita
			// pelo consumidor");
			//
			// // Para cada path/operações adiciona as respostas personalizadas
			// responses.addApiResponse("404", apiResponseNaoEncontrado);
			// responses.addApiResponse("406", apiResponseSemRepresentacao);
			// responses.addApiResponse("500", apiResponseErroInterno);
			// });
		};
	}
	
	/*
	 * Para testes com API que possua múltiplas documentações (pública/privada por exemplo).
	 */
//  @Bean
//  public GroupedOpenApi groupedOpenApi() {
//      return GroupedOpenApi.builder()
//              .group("AlgaFood API Admin")
//              .pathsToMatch("/v1/**")
//              .addOpenApiCustomiser(openApi -> {
//                  openApi.info(new Info()
//                          .title("AlgaFood API Admin")
//                          .version("v1")
//                          .description("REST API do AlgaFood")
//                          .license(new License()
//                                  .name("Apache 2.0")
//                                  .url("http://springdoc.com")
//                          )
//                  ).externalDocs(new ExternalDocumentation()
//                          .description("AlgaWorks")
//                          .url("https://algaworks.com")
//                  );
//              })
//              .build();
//  }
//
//  @Bean
//  public GroupedOpenApi groupedOpenApiCliente() {
//      return GroupedOpenApi.builder()
//              .group("AlgaFood API Cliente")
//              .pathsToMatch("/cliente/v1/**")
//              .addOpenApiCustomiser(openApi -> {
//                  openApi.info(new Info()
//                          .title("AlgaFood API Cliente")
//                          .version("v1")
//                          .description("REST API do AlgaFood")
//                          .license(new License()
//                                  .name("Apache 2.0")
//                                  .url("http://springdoc.com")
//                          )
//                  ).externalDocs(new ExternalDocumentation()
//                          .description("AlgaWorks")
//                          .url("https://algaworks.com")
//                  );
//              })
//              .build();
//  }
	
	/**
	 * Gera a associação de referências (nome do schema) e a class correspondente, expondo a estrutura para o OpenApi.
	 * Utilizado como compomente na configuração do OpenApi, para saber como será representado o modelo de {@link Problem} na documentação.
	 */
	private Map<String, Schema> gerarSchemas() {
		final Map<String, Schema> schemaMap = new HashMap<>();
		
		Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
		Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);
		
		schemaMap.putAll(problemSchema);
		schemaMap.putAll(problemObjectSchema);
		
		return schemaMap;
	}
	
	/**
	 * Gera associação do modelo de representação do objeto de problema,
	 * com referências que serão apontadas em ApiResponse dos verbos HTTP,
	 * na customização da configuração do OpenApi (OpenApiCustomizer). 
	 */
	private Map<String , ApiResponse> gerarResponses() {
		final Map<String , ApiResponse> apiResponseMap = new HashMap<>();
		
		// Realiza associação da estrutura do objeto problema com Media Type application/json
		Content content = new Content()
				.addMediaType(APPLICATION_JSON_VALUE,
						new MediaType().schema(new Schema<Problem>().$ref("Problema")));
		
		apiResponseMap.put(BAD_REQUEST_RESPONSE, new ApiResponse()
				.description("Requisição inválida")
				.content(content));
		
		apiResponseMap.put(NOT_FOUND_RESPONSE, new ApiResponse()
				.description("Recurso não encontrado")
				.content(content));
		
		apiResponseMap.put(NOT_ACCEPTABLE_RESPONSE, new ApiResponse()
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.content(content));
		
		apiResponseMap.put(INTERNAL_SERVER_ERROR_RESPONSE, new ApiResponse()
				.description("Erro interno no servidor")
				.content(content));
		
		return apiResponseMap;
	}
	
}

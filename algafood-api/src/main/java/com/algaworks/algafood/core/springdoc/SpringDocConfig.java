package com.algaworks.algafood.core.springdoc;

import java.util.Arrays;

import org.springdoc.core.customizers.OpenApiCustomiser;
//import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

	@Bean
	public OpenAPI openAPI() {
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
								new Tag().name("Cidades").description("Gerencia as cidades")
						));
	}
	
	/**
	 * Configura um customizador para a documentaçãp.
	 * Recebe o bean registrado da documentação e personaliza propriedades.
	 */
	@Bean
	public OpenApiCustomiser openApiCustomiser() {
		return openApi -> {
			openApi.getPaths()
				.values()
				.stream()
				.flatMap(pathItem -> pathItem.readOperations().stream())
				.forEach(operation -> {
					// Recupera respostas já registradas
					ApiResponses responses = operation.getResponses();
					
					// Cria respostas personalizadas
					ApiResponse apiResponseNaoEncontrado = new ApiResponse().description("Recurso não encontrado");
					ApiResponse apiResponseErroInterno = new ApiResponse().description("Erro interno no servidor");
					ApiResponse apiResponseSemRepresentacao = new ApiResponse()
							.description("Recurso não possui uma representação que poderia ser aceita pelo consumidor");
					
					// Para cada path/operações adiciona as respostas personalizadas
					responses.addApiResponse("404", apiResponseNaoEncontrado);
					responses.addApiResponse("406", apiResponseSemRepresentacao);
					responses.addApiResponse("500", apiResponseErroInterno);
				});
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
	
}

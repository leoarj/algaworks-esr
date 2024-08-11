package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PedidosResumoModelOpenApi;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class) // para habilitar a interpretação automática de alguns validações do Jakarta Bean Validation
public class SpringFoxConfig {

	@Bean
	public Docket apiDocket() {
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
				.select()
//					.apis(RequestHandlerSelectors.any()) // qualquer controllador
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
		        .paths(PathSelectors.any())
//		          .paths(PathSelectors.ant("/restaurantes/*"))
					.build()
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages()) // personaliza mensagens de erro quando verbo GET for o solicitado.
				.globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
		        .globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
		        .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
//		        .globalRequestParameters(Collections.singletonList(
//		                new RequestParameterBuilder()
//		                        .name("campos")
//		                        .description("Nomes das propriedades para filtrar na resposta, separados por vírgula")
//		                        .in(ParameterType.QUERY)
//		                        .required(true)
//		                        .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
//		                        .build())
//		        )
		        .additionalModels(typeResolver.resolve(Problem.class)) // registra classe de ProblemDetail para a documentação
		        .ignoredParameterTypes(ServletWebRequest.class)
		        .directModelSubstitute(Pageable.class, PageableModelOpenApi.class) // substituição para documentação de Pageable
		        .alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(Page.class, CozinhaModel.class),
						CozinhasModelOpenApi.class))
		        .alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(Page.class, PedidoResumoModel.class),
						PedidosResumoModelOpenApi.class))
		        .apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"),
						new Tag("Grupos", "Gerencia os grupos de usuários"),
						new Tag("Cozinhas", "Gerencia as cozinhas"),
						new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
						new Tag("Pedidos", "Gerencia os pedidos"),
						new Tag("Restaurantes", "Gerencia os restaurantes"),
						new Tag("Estados", "Gerencia os estados"),
						new Tag("Produtos", "Gerencia os produtos de restaurantes")); // para personalizar as tags referente a recursos, na UI da documentação
	}
	
	@Bean
	public JacksonModuleRegistrar springFoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}
	
	/**
	 * Gera a referência para a classe Problem.
	 */
	private Consumer<RepresentationBuilder> getProblemaModelReference() {
	    return r -> r.model(m -> m.name("Problema")
	            .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
	                    q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhandler")))));
	}
	
	/**
	 * Personaliza mensagens de erro quando verbo GET for o solicitado.
	 */
	private List<Response> globalGetResponseMessages() {
		  return Arrays.asList(
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		          .description("Erro interno do Servidor")
		          .representation(MediaType.APPLICATION_JSON )
                  .apply(getProblemaModelReference())
		          .build(),
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
		          .description("Recurso não possui representação que pode ser aceita pelo consumidor")
		          .build()
		  );
		}
	
	private List<Response> globalPostPutResponseMessages() {
	    return Arrays.asList(
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
	            .description("Requisição inválida (erro do cliente)")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
	            .description("Erro interno no servidor")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
	            .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
	            .description("Requisição recusada porque o corpo está em um formato não suportado")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build()
	    );
	  }
	
	private List<Response> globalDeleteResponseMessages() {
	    return Arrays.asList(
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
	            .description("Requisição inválida (erro do cliente)")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
	            .description("Erro interno no servidor")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build()
	    );
	  }
	
	/**
	 * Adiciona informações da API (substituirá informações na UI da documentação)
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("1")
				.contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();
	}
	
}
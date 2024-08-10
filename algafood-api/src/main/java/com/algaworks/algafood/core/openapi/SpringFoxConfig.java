package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;

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
		        .additionalModels(typeResolver.resolve(Problem.class)) // registra classe de ProblemDetail para a documentação
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades")); // para personalizar as tags referente a recursos, na UI da documentação
	}
	
	@Bean
	public JacksonModuleRegistrar springFoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}
	
	/**
	 * Personaliza mensagens de erro quando verbo GET for o solicitado.
	 */
	private List<Response> globalGetResponseMessages() {
		  return Arrays.asList(
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		          .description("Erro interno do Servidor")
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
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
	            .description("Erro interno no servidor")
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
	            .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
	            .description("Requisição recusada porque o corpo está em um formato não suportado")
	            .build()
	    );
	  }
	
	private List<Response> globalDeleteResponseMessages() {
	    return Arrays.asList(
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
	            .description("Requisição inválida (erro do cliente)")
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
	            .description("Erro interno no servidor")
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
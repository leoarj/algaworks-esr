package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Interface para desacoplar o controlador das anotações referentes a documentação do OpenAPI.
 */
//@Api(tags = "Cidades") // associa o controlador como um recurso da tag especificada no configuration

// Referencia o esquema de segurança definido na configuração do bean do Spring Doc.
@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Lista as cidades")
	CollectionModel<CidadeModel> listar();
	
	@Operation(summary = "Busca uma cidade por ID")
//	@ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID da cidade inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
	CidadeModel buscar(
			@Parameter(description = "ID de uma cidade", example = "1", required = true)
			Long cidadeId);
	
	@Operation(summary = "Cadastra uma cidade",
			description = "Cadastro de uma cidade, necessita de um estado e um nome válido")
//	@ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Cidade cadastrada"),
//	})
	CidadeModel adicionar(
			@RequestBody(description = "Representação de uma nova cidade", required = true)
			CidadeInput cidadeInput);
	
	@Operation(summary = "Atualiza uma cidade por ID")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Cidade atualizada"),
//		@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
	CidadeModel atualizar(
			@Parameter(description = "ID de uma cidade", example = "1", required = true)
			Long cidadeId,
			
			@RequestBody(description = "Representação de uma cidade com os novos dados", required = true)
			CidadeInput cidadeInput);
	
	@Operation(summary = "Exclui uma cidade por ID")
//	@ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Cidade excluída"),
//		@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
	void remover(
			@Parameter(description = "ID de uma cidade", example = "1", required = true)
			Long cidadeId);
	
}
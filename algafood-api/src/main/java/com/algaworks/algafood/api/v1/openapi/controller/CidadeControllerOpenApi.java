package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Interface para desacoplar o controlador das anotações referentes a documentação do OpenAPI.
 */

// Referencia o esquema de segurança definido na configuração do bean do Spring Doc.
@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades") // associa o controlador como um recurso da tag especificada no configuration
public interface CidadeControllerOpenApi {

	@Operation(summary = "Lista as cidades")
	CollectionModel<CidadeModel> listar();
	
	@Operation(summary = "Busca uma cidade por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID da cidade inválido",
						content = @Content(schema = @Schema(ref = "Problema"))),
					@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
						content = @Content(schema = @Schema(ref = "Problema")))
					
			})
	CidadeModel buscar(
			@Parameter(description = "ID de uma cidade", example = "1", required = true)
			Long cidadeId);
	
	@Operation(summary = "Cadastra uma cidade",
			description = "Cadastro de uma cidade, necessita de um estado e um nome válido")
	CidadeModel adicionar(
			@RequestBody(description = "Representação de uma nova cidade", required = true)
			CidadeInput cidadeInput);
	
	@Operation(summary = "Atualiza uma cidade por ID",
			responses = {
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400", description = "ID da cidade inválido",
							content = @Content(schema = @Schema(ref = "Problema"))),
					@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
					content = @Content(schema = @Schema(ref = "Problema")))
			})
	CidadeModel atualizar(
			@Parameter(description = "ID de uma cidade", example = "1", required = true)
			Long cidadeId,
			@RequestBody(description = "Representação de uma cidade com os novos dados", required = true)
			CidadeInput cidadeInput);
	
	@Operation(summary = "Exclui uma cidade por ID",
			responses = {
					@ApiResponse(responseCode = "204"),
					@ApiResponse(responseCode = "400", description = "ID da cidade inválido",
							content = @Content(schema = @Schema(ref = "Problema"))),
					@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
					content = @Content(schema = @Schema(ref = "Problema")))
			})
	void remover(
			@Parameter(description = "ID de uma cidade", example = "1", required = true)
			Long cidadeId);
	
}
package com.algaworks.algafood.api.v2.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;

/**
 * Interface para desacoplar o controlador das anotações referentes a documentação do OpenAPI.
 */
//@Api(tags = "Cidades") // associa o controlador como um recurso da tag especificada no configuration
public interface CidadeControllerV2OpenApi {

//	@ApiOperation("Lista as cidades")
	CollectionModel<CidadeModelV2> listar();
	
//	@ApiOperation("Busca uma cidade por ID")
//	@ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID da cidade inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
	CidadeModelV2 buscar(
//			@ApiParam(value = "ID de uma cidade", example = "1", required = true)
			Long cidadeId);
	
//	@ApiOperation("Cadastra uma cidade")
//	@ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Cidade cadastrada"),
//	})
	CidadeModelV2 adicionar(
//			@ApiParam(name = "corpo", value = "Representação de uma nova cidade", required = true)
			CidadeInputV2 cidadeInput);
	
//	@ApiOperation("Atualiza uma cidade por ID")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Cidade atualizada"),
//		@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
	CidadeModelV2 atualizar(
//			@ApiParam(value = "ID de uma cidade", example = "1", required = true) 
			Long cidadeId,
			
//			@ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
			CidadeInputV2 cidadeInput);
	
//	@ApiOperation("Exclui uma cidade por ID")
//	@ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Cidade excluída"),
//		@ApiResponse(responseCode = "404", description = "Cidade não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
	void remover(
//			@ApiParam(value = "ID de uma cidade", example = "1", required = true)
			Long cidadeId);
	
}
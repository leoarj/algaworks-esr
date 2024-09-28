package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.core.springdoc.PageableParameter;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Cozinhas")
@SecurityRequirement(name = "security_auth")
public interface CozinhaControllerOpenApi {

//    @ApiOperation("Lista as cozinhas com paginação")
	@PageableParameter
    PagedModel<CozinhaModel> listar(
    		@Parameter(hidden = true)
    		Pageable pageable);
    
//    @ApiOperation("Busca uma cozinha por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CozinhaModel buscar(
//            @ApiParam(value = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId);
    
//    @ApiOperation("Cadastra uma cozinha")
//    @ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Cozinha cadastrada"),
//	})
    CozinhaModel adicionar(
//            @ApiParam(name = "corpo", value = "Representação de uma nova cozinha", required = true)
            CozinhaInput cozinhaInput);
    
//    @ApiOperation("Atualiza uma cozinha por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Cozinha atualizada"),
//		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CozinhaModel atualizar(
//            @ApiParam(value = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId,
            
//            @ApiParam(name = "corpo", value = "Representação de uma cozinha com os novos dados")
            CozinhaInput cozinhaInput);
    
//    @ApiOperation("Exclui uma cozinha por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Cozinha excluída"),
//		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    void remover(
//            @ApiParam(value = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId);   
}        

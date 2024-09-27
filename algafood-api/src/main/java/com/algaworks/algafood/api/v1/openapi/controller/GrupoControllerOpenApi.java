package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Grupos")
@SecurityRequirement(name = "security_auth")
public interface GrupoControllerOpenApi {

//    @ApiOperation("Lista os grupos")
    CollectionModel<GrupoModel> listar();
    
//    @ApiOperation("Busca um grupo por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID da grupo inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Grupo não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    GrupoModel buscar(
//            @ApiParam(value = "ID de um grupo", example = "1", required = true)
            Long grupoId);
    
//    @ApiOperation("Cadastra um grupo")
//    @ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Grupo cadastrado"),
//	})
    GrupoModel adicionar(
//            @ApiParam(name = "corpo", value = "Representação de um novo grupo", required = true)
            GrupoInput grupoInput);
    
//    @ApiOperation("Atualiza um grupo por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Grupo atualizado"),
//		@ApiResponse(responseCode = "404", description = "Grupo não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    GrupoModel atualizar(
//            @ApiParam(value = "ID de um grupo", example = "1", required = true)
            Long grupoId,
            
//            @ApiParam(name = "corpo", value = "Representação de um grupo com os novos dados", required = true)
            GrupoInput grupoInput);
    
//    @ApiOperation("Exclui um grupo por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Grupo excluído"),
//		@ApiResponse(responseCode = "404", description = "Grupo não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    void remover(
//            @ApiParam(value = "ID de um grupo", example = "1", required = true)
            Long grupoId);
    
}

package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.PermissaoModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Grupos")
@SecurityRequirement(name = "security_auth")
public interface GrupoPermissaoControllerOpenApi {
    
//    @ApiOperation("Lista as permissões associadas a um grupo")
//    @ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID do grupo inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Grupo não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CollectionModel<PermissaoModel> listar(
//            @ApiParam(value = "ID do grupo", example = "1", required = true)
            Long grupoId);

//    @ApiOperation("Desassociação de permissão com grupo")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Desassociação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Grupo ou permissão não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> desassociar(
//            @ApiParam(value = "ID do grupo", example = "1", required = true)
            Long grupoId,
            
//            @ApiParam(value = "ID da permissão", example = "1", required = true)
            Long permissaoId);

//    @ApiOperation("Associação de permissão com grupo")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Grupo ou permissão não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> associar(
//            @ApiParam(value = "ID do grupo", example = "1", required = true)
            Long grupoId,
            
//            @ApiParam(value = "ID da permissão", example = "1", required = true)
            Long permissaoId);
}        

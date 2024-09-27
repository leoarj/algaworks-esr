package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.GrupoModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Usuários")
@SecurityRequirement(name = "security_auth")
public interface UsuarioGrupoControllerOpenApi {

//    @ApiOperation("Lista os grupos associados a um usuário")
//    @ApiResponses({
//		@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CollectionModel<GrupoModel> listar(
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId);

//    @ApiOperation("Desassociação de grupo com usuário")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Desassociação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Usuário ou grupo não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> desassociar(
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId,
            
//            @ApiParam(value = "ID do grupo", example = "1", required = true)
            Long grupoId);

//    @ApiOperation("Associação de grupo com usuário")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Usuário ou grupo não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> associar(
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId,
            
//            @ApiParam(value = "ID do grupo", example = "1", required = true)
            Long grupoId);
}

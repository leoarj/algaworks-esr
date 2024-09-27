package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Restaurantes")
@SecurityRequirement(name = "security_auth")
public interface RestauranteUsuarioResponsavelControllerOpenApi {

//    @ApiOperation("Lista os usuários responsáveis associados a restaurante")
//    @ApiResponses({
//		@ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CollectionModel<UsuarioModel> listar(
//            @ApiParam(value = "ID do restaurante", example = "1", required = true)
            Long restauranteId);

//    @ApiOperation("Desassociação de restaurante com usuário responsável")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Desassociação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> desassociar(
//            @ApiParam(value = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId);

//    @ApiOperation("Associação de restaurante com usuário responsável")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> associar(
//            @ApiParam(value = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            
//            @ApiParam(value = "ID do usuário", example = "1", required = true)
            Long usuarioId);
}


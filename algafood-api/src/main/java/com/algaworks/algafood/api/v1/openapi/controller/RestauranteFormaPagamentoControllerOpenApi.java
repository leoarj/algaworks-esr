package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Restaurantes")
@SecurityRequirement(name = "security_auth")
public interface RestauranteFormaPagamentoControllerOpenApi {
    
//    @ApiOperation("Lista as formas de pagamento associadas a restaurante")
//    @ApiResponses({
//		@ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CollectionModel<FormaPagamentoModel> listar(
//            @ApiParam(value = "ID do restaurante", example = "1", required = true)
            Long restauranteId);

//    @ApiOperation("Desassociação de restaurante com forma de pagamento")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Desassociação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Restaurante ou forma de pagamento não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> desassociar(
//            @ApiParam(value = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            
//            @ApiParam(value = "ID da forma de pagamento", example = "1", required = true)
            Long formaPagamentoId);

//    @ApiOperation("Associação de restaurante com forma de pagamento")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Restaurante ou forma de pagamento não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> associar(
//            @ApiParam(value = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            
//            @ApiParam(value = "ID da forma de pagamento", example = "1", required = true)
            Long formaPagamentoId);
}

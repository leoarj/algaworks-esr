package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Pedidos")
@SecurityRequirement(name = "security_auth")
public interface FluxoPedidoControllerOpenApi {

//	@ApiOperation("Confirmação de pedido")
//	@ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Pedido confirmado com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Pedido não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> confirmar(
//            @ApiParam(value = "Código do pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", 
//                required = true)
            String codigoPedido);

//    @ApiOperation("Cancelamento de pedido")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Pedido não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> cancelar(
//            @ApiParam(value = "Código do pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", 
//                required = true)
            String codigoPedido);

//    @ApiOperation("Registrar entrega de pedido")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Entrega de pedido registrada com sucesso"),
//		@ApiResponse(responseCode = "404", description = "Pedido não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<Void> entregar(
//            @ApiParam(value = "Código do pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", 
//                required = true)
            String codigoPedido);
}

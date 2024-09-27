package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.domain.filter.PedidoFilter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Pedidos")
@SecurityRequirement(name = "security_auth")
public interface PedidoControllerOpenApi {

//	@ApiImplicitParams({
//        @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
//                name = "campos", paramType = "query", type = "string")
//    })
//    @ApiOperation("Pesquisa os pedidos")
    PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable);
    
//    @ApiImplicitParams({
//        @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
//                name = "campos", paramType = "query", type = "string")
//    })
//    @ApiOperation("Busca um pedido por código")
//    @ApiResponses({
//		@ApiResponse(responseCode = "404", description = "Pedido não encontrado",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    PedidoModel buscar(
//            @ApiParam(value = "Código de um pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true)
            String codigoPedido); 
    
//    @ApiOperation("Registra um pedido")
//    @ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Pedido registrado"),
//	})
    PedidoModel adicionar(
//            @ApiParam(name = "corpo", value = "Representação de um novo pedido", required = true)
            PedidoInput pedidoInput);
	
}

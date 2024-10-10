package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.core.springdoc.PageableParameter;
import com.algaworks.algafood.domain.filter.PedidoFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pedidos")
@SecurityRequirement(name = "security_auth")
public interface PedidoControllerOpenApi {

    @Operation(summary = "Pesquisa os pedidos",
    		parameters = {
    			    @Parameter(
    			    		in = ParameterIn.QUERY,
    			    		name = "clienteId",
    			    		description = "ID do cliente para filtro da pesquisa",
    			    		example = "1",
    			    		schema = @Schema(type = "integer")
    			    		),
    			    @Parameter(
    			    		in = ParameterIn.QUERY,
    			    		name = "restauranteId",
    			    		description = "ID do restaurante para filtro da pesquisa",
    			    		example = "1",
    			    		schema = @Schema(type = "integer")
    			    		),
    			    @Parameter(
    			    		in = ParameterIn.QUERY,
    			    		name = "dataCriacaoInicio",
    			    		description = "Data/hora de criação inicial para filtro da pesquisa",
    			    		example = "2019-10-30T00:00:00Z",
    			    		schema = @Schema(type = "string", format = "date-time")
    			    		),
    			    @Parameter(
    			    		in = ParameterIn.QUERY,
    			    		name = "dataCriacaoFim",
    			    		description = "Data/hora de criação final para filtro da pesquisa",
    			    		example = "2019-11-01T10:00:00Z",
    			    		schema = @Schema(type = "string", format = "date-time")
    			    		),
    			    @Parameter(
    			    		in = ParameterIn.QUERY,
    			    		name = "campos",
    			    		description = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
    			    		example = "codigo,valorTotal,cliente",
    			    		schema = @Schema(type = "string"),
    			    		required = false
    			    		)
    		})
    @PageableParameter
    PagedModel<PedidoResumoModel> pesquisar(
    		@Parameter(hidden = true)
    		PedidoFilter filtro,
    		@Parameter(hidden = true)
    		Pageable pageable);
    @Operation(summary =  "Busca um pedido por código",
    		parameters = {
    				@Parameter(
    			    		in = ParameterIn.QUERY,
    			    		name = "campos",
    			    		description = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
    			    		example = "codigo,valorTotal,cliente",
    			    		schema = @Schema(type = "string"),
    			    		required = false
    			    		)
    		},
    		responses = {
    				@ApiResponse(responseCode = "200"),
    				@ApiResponse(responseCode = "404", description = "Pedido não encontrado",
    						content = @Content(schema = @Schema(ref = "Problema")))
    		})
    PedidoModel buscar(
            @Parameter(description = "Código de um pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true)
            String codigoPedido); 
    
    @Operation(summary = "Registra um pedido",
    		responses = {
    				@ApiResponse(responseCode = "201", description = "Pedido registrado")
    		})
    PedidoModel adicionar(
            @RequestBody(description = "Representação de um novo pedido", required = true)
            PedidoInput pedidoInput);
	
}

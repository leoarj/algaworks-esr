package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Formas de pagamento")
@SecurityRequirement(name = "security_auth")
public interface FormaPagamentoControllerOpenApi {

//    @ApiOperation(value = "Lista as formas de pagamento")
//    @ApiResponses({
//		@ApiResponse(responseCode = "200", description = "OK",
//				content = @Content(mediaType = "application/json",
//				schema = @Schema(implementation = FormasPagamentoModelOpenApi.class)))
//	})
    ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request);
    
//    @ApiOperation("Busca uma forma de pagamento por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID da forma de pagamento inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    ResponseEntity<FormaPagamentoModel> buscar(
//            @ApiParam(value = "ID de uma forma de pagamento", example = "1", required = true)
            Long formaPagamentoId,
            
            ServletWebRequest request);
    
//    @ApiOperation("Cadastra uma forma de pagamento")
//    @ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Forma de pagamento cadastrada"),
//	})
    FormaPagamentoModel adicionar(
//            @ApiParam(name = "corpo", value = "Representação de uma nova forma de pagamento", required = true)
            FormaPagamentoInput formaPagamentoInput);
    
//    @ApiOperation("Atualiza uma cidade por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada"),
//		@ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    FormaPagamentoModel atualizar(
//            @ApiParam(value = "ID de uma forma de pagamento", example = "1", required = true)
            Long formaPagamentoId,
            
//            @ApiParam(name = "corpo", value = "Representação de uma forma de pagamento com os novos dados", required = true)
            FormaPagamentoInput formaPagamentoInput);
    
//    @ApiOperation("Exclui uma forma de pagamento por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "204", description = "Forma de pagamento excluída"),
//		@ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    void remover(
//    		@ApiParam(value = "ID de uma forma de pagamento", example = "1", required = true)
    		Long formaPagamentoId);   
}


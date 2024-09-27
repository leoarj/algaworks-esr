package com.algaworks.algafood.api.v2.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;

//@Api(tags = "Cozinhas")
public interface CozinhaControllerV2OpenApi {

//    @ApiOperation("Lista as cozinhas com paginação")
    PagedModel<CozinhaModelV2> listar(Pageable pageable);
    
//    @ApiOperation("Busca uma cozinha por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",
//				content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))),
//		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CozinhaModelV2 buscar(
//            @ApiParam(value = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId);
    
//    @ApiOperation("Cadastra uma cozinha")
//    @ApiResponses({
//		@ApiResponse(responseCode = "201", description = "Cozinha cadastrada"),
//	})
    CozinhaModelV2 adicionar(
//            @ApiParam(name = "corpo", value = "Representação de uma nova cozinha", required = true)
            CozinhaInputV2 cozinhaInput);
    
//    @ApiOperation("Atualiza uma cozinha por ID")
//    @ApiResponses({
//		@ApiResponse(responseCode = "200", description = "Cozinha atualizada"),
//		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
//			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
//	})
    CozinhaModelV2 atualizar(
//            @ApiParam(value = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId,
            
//            @ApiParam(name = "corpo", value = "Representação de uma cozinha com os novos dados")
            CozinhaInputV2 cozinhaInput);
    
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

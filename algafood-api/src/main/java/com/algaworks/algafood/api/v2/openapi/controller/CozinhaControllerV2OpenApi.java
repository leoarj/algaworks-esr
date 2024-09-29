package com.algaworks.algafood.api.v2.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cozinhas")
public interface CozinhaControllerV2OpenApi {

	@Operation(summary = "Lista as cozinhas com paginação")
    PagedModel<CozinhaModelV2> listar(Pageable pageable);
    
@Operation(summary = "Busca uma cozinha por ID",
responses = {
		@ApiResponse(responseCode = "200"),
		@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",
				content = @Content(schema = @Schema(ref = "Problema"))),
		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
			content = @Content(schema = @Schema(ref = "Problema")))
})
    CozinhaModelV2 buscar(
    		@Parameter(description = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId);
    
@Operation(summary = "Cadastra uma cozinha",
responses = {
		@ApiResponse(responseCode = "201", description = "Cozinha cadastrada")
})
    CozinhaModelV2 adicionar(
    		@Parameter(description = "ID de uma cozinha", example = "1", required = true)
            CozinhaInputV2 cozinhaInput);
    
@Operation(summary = "Atualiza uma cozinha por ID",
responses = {
		@ApiResponse(responseCode = "200", description = "Cozinha atualizada"),
		@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",
			content = @Content(schema = @Schema(ref = "Problema"))),
		@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
			content = @Content(schema = @Schema(ref = "Problema")))
})
    CozinhaModelV2 atualizar(
    		@Parameter(description = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId,
            
            @RequestBody(description = "Representação de uma cozinha com os novos dados")
            CozinhaInputV2 cozinhaInput);
    
@Operation(summary = "Exclui uma cozinha por ID",
responses = {
		@ApiResponse(responseCode = "204"),
		@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",
				content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
				content = @Content(schema = @Schema(ref = "Problema")))
})
    void remover(
    		@Parameter(description = "ID de uma cozinha", example = "1", required = true)
            Long cozinhaId);   
}        

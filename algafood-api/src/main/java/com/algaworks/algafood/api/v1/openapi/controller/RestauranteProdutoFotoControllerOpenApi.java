package com.algaworks.algafood.api.v1.openapi.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Produtos")
@SecurityRequirement(name = "security_auth")
public interface RestauranteProdutoFotoControllerOpenApi {

	@Operation(summary = "Atualiza a foto do produto de um restaurante",
			responses = {
					@ApiResponse(responseCode = "200", description = "Foto do produto atualizada"),
					@ApiResponse(responseCode = "404", description = "Produto de restaurante não encontrado",
							content = @Content(schema = @Schema(ref = "Problema")))
			})
    FotoProdutoModel atualizarFoto(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            @Parameter(description = "ID do produto", example = "1", required = true)
            Long produtoId,
            @RequestBody(description = "Arquivo da foto do produto (máximo 500KB, apenas JPG e PNG)", required = true)
            FotoProdutoInput fotoProdutoInput) throws IOException;

	@Operation(summary = "Exclui a foto do produto de um restaurante",
			responses = {
					@ApiResponse(responseCode = "204", description = "Foto do produto excluída"),
					@ApiResponse(responseCode = "400", description = "ID do restaurante ou produto inválido",
						content = @Content(schema = @Schema(ref = "Problema"))),
					@ApiResponse(responseCode = "404", description = "Foto de produto não encontrada",
							content = @Content(schema = @Schema(ref = "Problema")))
			})
    void remover(
    		@Parameter(description = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            @Parameter(description = "ID do produto", example = "1", required = true)
            Long produtoId);

    @Operation(summary = "Busca a foto do produto de um restaurante",
    		responses = {
				@ApiResponse(responseCode = "200",
						content = {
								@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FotoProdutoModel.class)),
								@Content(mediaType = MediaType.IMAGE_JPEG_VALUE, schema = @Schema(type = "string", format = "binary")),
								@Content(mediaType = MediaType.IMAGE_PNG_VALUE, schema = @Schema(type = "string", format = "binary"))
						}),
				@ApiResponse(responseCode = "400", description = "ID do restaurante ou produto inválido",
						content = @Content(schema = @Schema(ref = "Problema"))),
				@ApiResponse(responseCode = "404", description = "Foto de produto não encontrada",
					content = @Content(schema = @Schema(ref = "Problema")))
    		})
    FotoProdutoModel buscar(
    		@Parameter(description = "ID do restaurante", example = "1", required = true)
            Long restauranteId,
            @Parameter(description = "ID do produto", example = "1", required = true)
            Long produtoId);

    @Operation(hidden = true)
    ResponseEntity<?> servir(Long restauranteId, Long produtoId, String acceptHeader) 
            throws HttpMediaTypeNotAcceptableException;	
}

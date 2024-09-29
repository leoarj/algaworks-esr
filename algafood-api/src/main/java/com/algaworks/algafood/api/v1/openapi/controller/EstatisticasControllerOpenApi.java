package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.rootentrypoint.EstatisticasModel;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Estatísticas")
@SecurityRequirement(name = "security_auth")
public interface EstatisticasControllerOpenApi {

	@Operation(hidden = true)
	EstatisticasModel estatisticas();
	
	@Operation(summary = "Consulta estatísticas de vendas diárias",
			parameters = {
					@Parameter(
							in = ParameterIn.QUERY,
					    		name = "restauranteId",
					    		description = "ID do restaurante",
					    		example = "1",
					    		schema = @Schema(type = "integer"),
					    		required = false
							),
					@Parameter(
							in = ParameterIn.QUERY,
					    		name = "dataCriacaoInicio",
					    		description = "Data/hora inicial da criação do pedido",
					    		example = "2019-12-01T00:00:00Z",
					    		schema = @Schema(type = "string", format = "date-time"),
					    		required = false
							),
					@Parameter(
							in = ParameterIn.QUERY,
					    		name = "dataCriacaoFim",
					    		description = "Data/hora final da criação do pedido",
					    		example = "2019-12-02T23:59:59Z",
					    		schema = @Schema(type = "string", format = "date-time"),
					    		required = false
							)
			},
    		responses = {
				@ApiResponse(responseCode = "200",
						content = {
								@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = VendaDiaria.class))),
								@Content(mediaType = MediaType.APPLICATION_PDF_VALUE, schema = @Schema(type = "string", format = "binary"))
						})
    		})
    List<VendaDiaria> consultarVendasDiarias(
    		@Parameter(hidden = true)
            VendaDiariaFilter filtro,
            @Parameter(description = "Deslocamento de horário a ser considerado na consulta em relação ao UTC",
            		schema = @Schema(type = "string", defaultValue = "+00:00"))
            String timeOffset);

    @Operation(hidden = true)
    ResponseEntity<byte[]> consultarVendasDiariasPdf(
            VendaDiariaFilter filtro,
            String timeOffset);
}

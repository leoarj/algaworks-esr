package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.rootentrypoint.EstatisticasModel;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//@Api(tags = "Estatísticas")
@SecurityRequirement(name = "security_auth")
public interface EstatisticasControllerOpenApi {

//	@ApiOperation(value = "Estatísticas", hidden = true)
	EstatisticasModel estatisticas();
	
//    @ApiOperation("Consulta estatísticas de vendas diárias")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "restauranteId", value = "ID do restaurante", 
//                example = "1", dataType = "int", dataTypeClass = Integer.class),
//        @ApiImplicitParam(name = "dataCriacaoInicio", value = "Data/hora inicial da criação do pedido",
//            example = "2019-12-01T00:00:00Z", dataType = "date-time", dataTypeClass = OffsetDateTime.class),
//        @ApiImplicitParam(name = "dataCriacaoFim", value = "Data/hora final da criação do pedido",
//            example = "2019-12-02T23:59:59Z", dataType = "date-time", dataTypeClass = OffsetDateTime.class)
//    })
    List<VendaDiaria> consultarVendasDiarias(
            VendaDiariaFilter filtro,
            
//            @ApiParam(value = "Deslocamento de horário a ser considerado na consulta em relação ao UTC",
//                defaultValue = "+00:00")
            String timeOffset);

    ResponseEntity<byte[]> consultarVendasDiariasPdf(
            VendaDiariaFilter filtro,
            String timeOffset);
}

package com.algaworks.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta classe implementa o serviço de relatório de vendas diárias,
 * utilizando a bibliteca do JasperReports.
 * A projeção de dados utilizadas no relatório é provida pelo serviço
 * de consulta de vendas diárias.
 * 
 * Devido ser uma implementação específica e com uso de bibliotecas externas
 * é uma classe que fica no pacote de infraestrutura.
 */
@Service
public class PdfVendaReportService implements VendaReportService {

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		try {
			
			var inputStream = this.getClass().getResourceAsStream("/reports/vendas-diarias.jasper");
			
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
			var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir o relatório de vendas diárias", e);
		}
	}

}

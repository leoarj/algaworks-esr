package com.algaworks.algafood.core.data;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Classe para tradução de propriedades de ordenação conforme um mapa fornecido,
 * onde a propriedade de origem é traduzida para a propriedade de destino correta,
 * para execução da ordenação na consulta da entidade.
 */
public class PageableTranslator {
	
	public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
		/*
		 * Retorna uma nova lista com as ordenações com as propriedades correspondentes.
		 * Filtra se a propriedade exista no mapa.
		 * Cria uma nova order, com base na direção (asc,desc) e com propriedade correta.
		 * Coleta as orders em uma lista.
		 * Instancia um novo pageable com as ordenações traduzidas.
		 */
		
		// TODO refatorar para lançar exception de negócio posteriormente
		var orders = pageable.getSort().stream()
				.filter(order -> fieldsMapping.containsKey(order.getProperty()))
				.map(order -> new Sort.Order(order.getDirection(),
						fieldsMapping.get(order.getProperty())))
				.collect(Collectors.toList());
		
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(orders));
	}

}

package com.algaworks.algafood.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Classe wrapper para encapsular um {@linkplain Pageable}<br>
 * de modo a substituir a obtenção do {@linkplain Pageable} pelo framework<br>
 * no que diz respeito à geração dos links HATEOAS e repassagem dos parâmetros<br>
 * da consulta conforme específicados pelo cliente da API, dessa forma a tradução<br>
 * dos parâmetros fica separada e restrita ao processamento dos filtros na camada de persistência.
 */
public class PageWrapper<T> extends PageImpl<T> {

	private static final long serialVersionUID = 1L;

	private Pageable pageable;
	
	public PageWrapper(Page<T> page, Pageable pageable) {
		super(page.getContent(), pageable, page.getTotalElements());
		
		this.pageable = pageable;
	}
	
	/**
	 * Necessário realizar a sobreescrita porque esse método é chamado pelo framework<br>
	 * na construção dos links de HyperMidia, no caso será utilizado o {@link Pageable} repassado no construtor.
	 */
	@Override
	public Pageable getPageable() {
		return this.pageable;
	}

}

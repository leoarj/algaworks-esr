package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@Column(name = "taxa_frete", nullable = true) // Não necessário, apenas para referência.
	private BigDecimal taxaFrete;
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cozinha_id", nullable = false) // Não necessário, apenas para referência.
	private Cozinha cozinha;
	
	/**
	 * @JoinTable para definir a tabela de associação.
	 * @joinColumns para definir as colunas que apontam para cada chave primária de cada tabela.
	 * joinColumns para definir nome da chave estrangeira referente a primeira entidade.
	 * inverseJoinColumns para definir nome da chave estrangeira referente a segunda entidade.
	 */
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
}

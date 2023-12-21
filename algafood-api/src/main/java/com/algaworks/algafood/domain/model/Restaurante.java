package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
}

package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.Groups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	
	//@NotNull
	//@NotEmpty
	@NotBlank(groups = Groups.CadastroRestaurante.class)
	@Column(nullable = false)
	private String nome;
	
	//@DecimalMin("0")
	@PositiveOrZero(groups = Groups.CadastroRestaurante.class)
	@Column(name = "taxa_frete", nullable = false) // Não necessário, apenas para referência.
	private BigDecimal taxaFrete;
	
	@Valid // Para validar em cascata (Por padrão não é realizada validação em cascata)
	@NotNull(groups = Groups.CadastroRestaurante.class)
	//@JsonIgnoreProperties("hibernateLazyInitializer") // Para ignorar proxy gerado pelo Hibernate no caso de utilizar carregamento Lazy
	//@JsonIgnore
	@ManyToOne //(fetch = FetchType.LAZY) // Habilitar o carregamento tardio
	@JoinColumn(name = "cozinha_id", nullable = false) // Não necessário, apenas para referência.
	private Cozinha cozinha;
	
	@JsonIgnore
	@Embedded
	private Endereco endereco;
	
	@JsonIgnore
	@CreationTimestamp // específica da implementação (Hibernate)
	@Column(nullable = false, columnDefinition = "datetime") // para especificar o tipo no DB
	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	@UpdateTimestamp // específica da implementação (Hibernate)
	@Column(nullable = false, columnDefinition = "datetime") // para especificar o tipo no DB
	private LocalDateTime dataAtualizacao;
	
	/**
	 * @JoinTable para definir a tabela de associação.
	 * @joinColumns para definir as colunas que apontam para cada chave primária de cada tabela.
	 * joinColumns para definir nome da chave estrangeira referente a primeira entidade.
	 * inverseJoinColumns para definir nome da chave estrangeira referente a segunda entidade.
	 */
	@JsonIgnore
	//@ManyToMany(fetch = FetchType.EAGER) // Apenas para teste de carregamento "ansioso", não recomendado nesse caso
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
}

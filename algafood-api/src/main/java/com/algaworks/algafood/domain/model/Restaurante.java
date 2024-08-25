package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(name = "taxa_frete", nullable = false) // Não necessário (name), apenas para referência.
	private BigDecimal taxaFrete;
	
	@ManyToOne
	@JoinColumn(name = "cozinha_id", nullable = false) // Não necessário (name), apenas para referência.
	private Cozinha cozinha;
	
	@Embedded
	private Endereco endereco;
	
	private Boolean ativo = Boolean.TRUE;
	
	private Boolean aberto = Boolean.FALSE;
	
	@CreationTimestamp // específica da implementação (Hibernate)
	@Column(nullable = false, columnDefinition = "datetime") // para especificar o tipo no DB
	private OffsetDateTime dataCadastro;
	
	@UpdateTimestamp // específica da implementação (Hibernate)
	@Column(nullable = false, columnDefinition = "datetime") // para especificar o tipo no DB
	private OffsetDateTime dataAtualizacao;
	
	/**
	 * @JoinTable para definir a tabela de associação.
	 * @joinColumns para definir as colunas que apontam para cada chave primária de cada tabela.
	 * joinColumns para definir nome da chave estrangeira referente a primeira entidade.
	 * inverseJoinColumns para definir nome da chave estrangeira referente a segunda entidade.
	 */
	//@ManyToMany(fetch = FetchType.EAGER) // Apenas para teste de carregamento "ansioso", não recomendado nesse caso
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel",
	joinColumns = @JoinColumn(name = "restaurante_id"),
	inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	/**
	 * Construtor, para selecionar apenas id e nome na query específica no repository.
	 */
	public Restaurante(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public void ativar() {
		setAtivo(true);
	}
	
	public void inativar() {
		setAtivo(false);
	}
	
	public void abrir() {
		setAberto(true);
	}
	
	public void fechar() {
		setAberto(false);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().remove(formaPagamento);
	}
	
	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().add(formaPagamento);
	}
	
	public boolean removerResponsavel(Usuario responsavel) {
		return getResponsaveis().remove(responsavel);
	}
	
	public boolean adicionarResponsavel(Usuario responsavel) {
		return getResponsaveis().add(responsavel);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().contains(formaPagamento);
	}
	
	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !aceitaFormaPagamento(formaPagamento);
	}
	
	public boolean isAtivo() {
		return this.ativo;
	}
	
	public boolean isInativo() {
		return !isAtivo();
	}
	
	public boolean isAberto() {
		return this.aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}
	
	public boolean podeSerAtivado() {
		return isInativo();
	}
	
	public boolean podeSerInativado() {
		return isAtivo();
	}
	
	public boolean podeSerAberto() {
		return isAtivo() && isFechado();
	}
	
	public boolean podeSerFechado() {
		return isAtivo() && isAberto();
	}
}

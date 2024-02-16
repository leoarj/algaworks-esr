package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.core.validation.Groups;
import com.algaworks.algafood.core.validation.ValorZeroIncluirDescricao;

import lombok.Data;
import lombok.EqualsAndHashCode;

// Anotação customizada a nível de classe para validar mais de uma propriedade conforme condições impostas.
@ValorZeroIncluirDescricao(valorField = "taxaFrete",
		descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
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
	@NotBlank
	@Column(nullable = false)
	private String nome;
	
	@NotNull // Necessário porque @PositiveOrZero não valida se é nulo
	//@DecimalMin("0")
	@PositiveOrZero//(message = "{TaxaFrete.invalida}") // para ler do resource bundle do Bean Validation
	//@Multiplo(numero = 5) // Constraint personalizada
	@Column(name = "taxa_frete", nullable = false) // Não necessário, apenas para referência.
	private BigDecimal taxaFrete;
	
	@Valid // Para validar em cascata (Por padrão não é realizada validação em cascata
	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@NotNull
	//@JsonIgnoreProperties("hibernateLazyInitializer") // Para ignorar proxy gerado pelo Hibernate no caso de utilizar carregamento Lazy
	@ManyToOne //(fetch = FetchType.LAZY) // Habilitar o carregamento tardio
	@JoinColumn(name = "cozinha_id", nullable = false) // Não necessário, apenas para referência.
	private Cozinha cozinha;
	
	@Embedded
	private Endereco endereco;
	
	@CreationTimestamp // específica da implementação (Hibernate)
	@Column(nullable = false, columnDefinition = "datetime") // para especificar o tipo no DB
	private LocalDateTime dataCadastro;
	
	@UpdateTimestamp // específica da implementação (Hibernate)
	@Column(nullable = false, columnDefinition = "datetime") // para especificar o tipo no DB
	private LocalDateTime dataAtualizacao;
	
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
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
}

package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

/**
 * Classe de testes para testar cadastro de cozinha.
 * 
 * Os testes seguem um padrão de nomenclatura, que deve dizer o sentido do teste.
 * 
 * Os testes são basicamente definidos em:
 * - Cenário = Atores (objetos) relacionados no teste.
 * - Ação = Interação entre os objetos que deverá produzir os resultados a serem validados.
 * - Validação = Verificação dos resultados obtidos a partir da ação,
 * onde os resultados já são esperados em um determinado estado.
 */
@SpringBootTest
class CadastroCozinhaIT {

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	/**
	 * Não deve falhar para o caso de cozinha ser informada corretamente para cadastro.
	 * Nesse caso, garantir que a operação ocorra sem falhas para dados informados corretamente.
	 */
	@Test
	void testarCadastroCozinhaComSucesso() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// ação
		novaCozinha = cadastroCozinhaService.salvar(novaCozinha);
		
		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	/**
	 * Deve falhar para o caso de cozinha ser informada incorretamente para cadastro.
	 * Nesse caso, garantir que um erro seja capturado.
	 */
	@Test
	void testarCadastroCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		// Necessário devido ao JUnit 5
		ConstraintViolationException erroEsperado =
				Assertions.assertThrows(ConstraintViolationException.class, () -> {
					cadastroCozinhaService.salvar(novaCozinha);
				});
		
		assertThat(erroEsperado).isNotNull();
	}
	
	//JUnit4: @Test(expected = EntidadeEmUsoException.class)
	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		EntidadeEmUsoException erroEsperaodo =
				Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
					cadastroCozinhaService.excluir(1L);
				});
		
		assertThat(erroEsperaodo).isNotNull();
	}
	
	//JUnit4: @Test(expected = EntidadeNaoEncontradaException.class)
	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		EntidadeNaoEncontradaException erroEsperado =
				Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
					cadastroCozinhaService.excluir(10L);
				});
		
		assertThat(erroEsperado).isNotNull();
	}

}

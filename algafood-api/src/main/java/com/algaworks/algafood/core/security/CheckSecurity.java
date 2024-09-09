package com.algaworks.algafood.core.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Meta-anotação agrupar e simplificar definição de segurança nos métodos.
 */
public @interface CheckSecurity {

	// TODO Criar anotações padrão para valores padrão?
	
	public @interface Cozinhas {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeEditar {}
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
	}
	
	public @interface Restaurantes {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_RESTAURANTES')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeGerenciarCadastro {}
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
				+ "(hasAuthority('EDITAR_RESTAURANTES') or "
				+ "@algaSecurity.gerenciaRestaurante(#restauranteId))")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeGerenciarFuncionamento {}
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
	}
	
	public @interface Pedidos {
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "
				+ "@algaSecurity.getUsuarioId() == returnObject.cliente.id or "
				+ "@algaSecurity.gerenciaRestaurante(returnObject.restaurante.id)")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeBuscar {}
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and "
				+ "(hasAuthority('CONSULTAR_PEDIDOS') or "
				+ "@algaSecurity.getUsuarioId() == #filtro.clienteId or "
				+ "@algaSecurity.gerenciaRestaurante(#filtro.restauranteId))")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodePesquisar {}
		
		@PreAuthorize("hasAuthority('GERENCIAR_PEDIDOS') and isAuthenticated()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeCriar {}
		
		public @interface Fluxo {
			
			@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
					+ "(hasAuthority('GERENCIAR_PEDIDOS') or "
					+ "@algaSecurity.gerenciaRestauranteDoPedido(#codigoPedido))")
			@Retention(RetentionPolicy.RUNTIME)
			@Target(ElementType.METHOD)
			public @interface PodeGerenciar {}
		}
	}
	
	public @interface FormasPagamento {
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeEditar {}
	}
	
	public @interface Cidades {
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CIDADES')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeEditar {}
	}
	
	public @interface Estados {
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_ESTADOS')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeEditar {}
	}
	
	public @interface UsuariosGruposPermissoes {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
				+ "@algaSecurity.getUsuarioId() == #usuarioId")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeAlterarPropriaSenha {}
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
				+ "(hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or"
				+ "@algaSecurity.getUsuarioId() == #usuarioId)")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeAlterarUsuario {}
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeEditar {}
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
	}
	
	public @interface Estatisticas {
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('GERAR_RELATORIOS')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {}
	}
	
}

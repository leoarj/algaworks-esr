package com.algaworks.algafood.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

/**
 * Componente para obter informações do contexto de segurança,<br>
 * como dados do usuário atenticado mediante token JKT.
 */
@Component
public class AlgaSecurity {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	// Bean necessário para testes de integração
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Retorna autenticação referente ao contexto atual.
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	/**
	 * Recupera claim customizada do objeto/token referente a autenticação atual.
	 */
	public Long getUsuarioId() {
		if (getAuthentication().getPrincipal() instanceof Jwt) {
			Jwt jwt = (Jwt) getAuthentication().getPrincipal();
			
			// Long.parseLong devido a nova customização do JWT
			//return Long.parseLong(jwt.getClaim("usuario_id"));
			
			Object usuarioId = jwt.getClaim("usuario_id");
			
			if (usuarioId == null) {
				return null;
			}
			
			return Long.valueOf(usuarioId.toString());
		}
		// Para testes de integração
		String userName = getAuthentication().getName();
		return this.usuarioRepository.findByEmail(userName).get().getId();
	}
	
	public boolean isAutenticado() {
		return getAuthentication().isAuthenticated();
	}
	
	public boolean temEscopoEscrita() {
		return hasAuthority("SCOPE_WRITE");
	}
	
	public boolean temEscopoLeitura() {
		return hasAuthority("SCOPE_READ");
	}
	
	public boolean podeConsultarRestaurantes() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean gerenciaRestaurante(Long restauranteId) {
		if (restauranteId == null) {
			return false;
		}
		
		return restauranteRepository.existsResponsavel(restauranteId, getUsuarioId());
	}
	
	public boolean podeGerenciarCadastroRestaurantes() {
		return temEscopoEscrita() && hasAuthority("EDITAR_RESTAURANTES");
	}
	
	public boolean podeGerenciarFuncionamentoRestaurantes(Long restauranteId) {
		return temEscopoEscrita() &&
				(hasAuthority("EDITAR_RESTAURANTES") || gerenciaRestaurante(restauranteId));
	}
	
	public boolean podeConsultarUsuariosGruposPermissoes() {
		return temEscopoLeitura() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}
	
	public boolean podeEditarUsuariosGruposPermissoes() {
		return temEscopoEscrita() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
	}
	
	public boolean podePesquisarPedidos(Long clienteId, Long restauranteId) {
		return temEscopoLeitura() &&
				(hasAuthority("CONSULTAR_PEDIDOS") ||
						usuarioAutenticadoIgual(restauranteId) || gerenciaRestaurante(restauranteId));
	}
	
	public boolean podePesquisarPedidos() {
		return isAutenticado() && temEscopoLeitura();
	}
	
	public boolean podeConsultarFormasPagamento() {
		return isAutenticado() && temEscopoLeitura();
	}
	
	public boolean podeConsultarCidades() {
		return isAutenticado() && temEscopoLeitura();
	}
	
	public boolean podeConsultarEstados() {
		return isAutenticado() && temEscopoLeitura();
	}
	
	public boolean podeConsultarCozinhas() {
		return isAutenticado() && temEscopoLeitura();
	}
	
	public boolean podeConsultarEstatisticas() {
		return temEscopoLeitura() && hasAuthority("GERAR_RELATORIOS");
	}
	
	public boolean gerenciaRestauranteDoPedido(String codigoPedido) {
		if (codigoPedido == null) {
			return false;
		}
		
		return pedidoRepository.isPedidoGerenciadoPor(codigoPedido, getUsuarioId());
	}
	
	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null
				&& getUsuarioId().equals(usuarioId);
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	
	public boolean podeGerenciarPedidos(String codigoPedido) {
		return hasAuthority("SCOPE_WRITE") &&
				(hasAuthority("GERENCIAR_PEDIDOS") || gerenciaRestauranteDoPedido(codigoPedido));
	}
}

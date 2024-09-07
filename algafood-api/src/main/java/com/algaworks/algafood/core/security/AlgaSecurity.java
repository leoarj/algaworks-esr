package com.algaworks.algafood.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Componente para obter informações do contexto de segurança,<br>
 * como dados do usuário atenticado mediante token JKT.
 */
@Component
public class AlgaSecurity {

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
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		return jwt.getClaim("usuario_id");
	}
	
}

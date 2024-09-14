package com.algaworks.algafood.core.security.authorizationserver;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.algaworks.algafood.domain.model.Usuario;

import lombok.Getter;

/**
 * Estende a classe {@link User} para adicionar mais informações referentes ao usuário.<br>
 * A classe {@link User} já implementa a interface {@link UserDetails}
 */
@Getter
public class AuthUser extends User {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String fullName;
	
	public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities); // repassando autorizações para autenticação
		
		this.userId = usuario.getId();
		this.fullName = usuario.getNome();
	}
}

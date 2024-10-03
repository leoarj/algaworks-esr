package com.algaworks.algafood.core.security.authorizationserver;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

/**
 * Fornece um componente que dirá ao framwork como recuperar dados do usuário.<br>
 * Neste caso, um componente que utiliza JPA para buscar um usuário de acordo com o e-mail.<br>
 * Um bean desse componente é injetado no objeto da classe {@link AuthorizationServerConfig},<br>
 * e repassada na configuração.
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional(readOnly = true) // para não fechar a transação logo após recuperar pelo findByEmail().
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail informado"));
		
		//return new AuthUser(usuario, getAuthorities(usuario));
		return new User(usuario.getEmail(), usuario.getSenha(), getAuthorities(usuario));
	}
	
	/**
	 * Recupera e transforma as permissões associadas aos grupos associados ao usuário<br>
	 * e transforma isso em uma coleção de permissões para serem passadas para um {@link AuthUser}.
	 */
	private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
		return usuario.getGrupos().stream()
				.flatMap(grupo -> grupo.getPermissoes().stream())
				.map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
				.collect(Collectors.toSet()); // para não haver duplicados
	}
}

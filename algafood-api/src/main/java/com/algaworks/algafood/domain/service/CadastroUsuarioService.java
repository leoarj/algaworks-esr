package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CadastroUsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	private final CadastroGrupoService cadastroGrupoService;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		// Para o JPA não gerenciar a instância
		// devido a possibilidade de ter sido alterada
		// e ao ser buscado outro objeto para comparação
		// pode trazer mais de um resultado que deveria ser único,
		// causando conflito.
		usuarioRepository.detach(usuario);
		
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		usuario.alterarSenha(senhaAtual, novaSenha);
	}
	
	@Transactional
	public void associarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		usuario.adicionarGrupo(grupo);
	}
	
	@Transactional
	public void desassociarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		usuario.removerGrupo(grupo);
	}
	
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
}

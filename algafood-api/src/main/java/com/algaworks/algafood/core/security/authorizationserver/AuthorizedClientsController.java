package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Principal;
import java.util.List;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

/**
 * Controlador para implementação própria de consulta e tela de listagem
 * de clients com autorizações/consentimentos aprovados armazenados no banco de dados.
 */
@Controller
@RequiredArgsConstructor
public class AuthorizedClientsController {

	private final OAuth2AuthorizationQueryService oAuth2AuthorizationQueryService;
	
	/**
	 * Mapeamento para {@literal /oauth2/authorized-clients}, para retornar página renderizada
	 * com a listagem dos clients com autorizações concedidas e persistidas no banco de dados.
	 * Recupera lista de clients autorizados para o {@link Principal} em questão através
	 * de um bean {@link OAuth2AuthorizationQueryService} e atribui como atributo no {@link Model}
	 * que vai ser processado pela rendering engine.
	 */
	@GetMapping("/oauth2/authorized-clients")
	public String clientsList(Principal principal, Model model) {
		List<RegisteredClient> clients = oAuth2AuthorizationQueryService.listClientsWithConsent(principal.getName());
		model.addAttribute("clients", clients);
		return "pages/authorized-clients";
	}
	
}

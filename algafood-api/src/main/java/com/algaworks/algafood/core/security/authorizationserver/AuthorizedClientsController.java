package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

/**
 * Controlador para implementação própria de consulta e tela de listagem
 * de clients com autorizações/consentimentos aprovados armazenados no banco de dados.
 */
@Controller
@RequiredArgsConstructor
public class AuthorizedClientsController {

	private final OAuth2AuthorizationQueryService oAuth2AuthorizationQueryService;
	private final RegisteredClientRepository clientRepository;
	private final OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService;
	private final OAuth2AuthorizationService oAuth2AuthorizationService;
	
	/**
	 * Mapeamento para {@literal /oauth2/authorized-clients}, para retornar página renderizada
	 * com a listagem dos clients com consentimentos e autorizações concedidas e persistidas no banco de dados.
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
	
	/**
	 * Mapeamento para {@literal /oauth2/authorized-clients/revoke}, para permitir revogação (deleção)
	 * de consentimentos e autorizações concedidas, as quais estejam armazenadas no banco de dados.
	 * Recupera o {@link RegisteredClient} a partir do {@literal client_id} passado na requisição,
	 * Busca e remove o registro referente aos consentimentos e busca e remove os registros referentes as autorizações.
	 * Ao final, redireciona para o endpoint {@literal authorized-clients}.
	 */
	@PostMapping("/oauth2/authorized-clients/revoke")
	public String revoke(Principal principal,
			Model model,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId) {
		// Recupera client via client_id passado vai requisição
		RegisteredClient registeredClient = this.clientRepository.findByClientId(clientId);
		
		if (registeredClient == null) {
			throw new AccessDeniedException(String.format("Cliente %s não encontrado", clientId));
		}
		
		// Recupera consentimentos (0-1) pelo id do client e principal_name associado
		OAuth2AuthorizationConsent consent = this.oAuth2AuthorizationConsentService
				.findById(registeredClient.getId(), principal.getName());
		
		// Recupera autorizações (0-N) pelo id do client e principal_name associado
		List<OAuth2Authorization> authorizations = this.oAuth2AuthorizationQueryService
				.listAuthorizations(principal.getName(), registeredClient.getId());
		
		if (consent != null) {
			this.oAuth2AuthorizationConsentService.remove(consent);
		}
		
		for (OAuth2Authorization authorization : authorizations) {
			this.oAuth2AuthorizationService.remove(authorization);
		}
		
		return "redirect:/oauth2/authorized-clients";
	}
	
}

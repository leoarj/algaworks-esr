package com.algaworks.algafood.core.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Componente para interceptar requisições e adicionar cabeçalho<br>
 * personalizado referente a depreciação de API.
 */
@Component
public class ApiRetirementHandler implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getRequestURI().startsWith("/v1/")) {
//			response.addHeader("X-AlgaFood-Deprecated",
//					"Essa versão da API está depreciada e deixará de existir a partir de 01/01/2021."
//							+ "Use a versão mais atual da API.");
			
			response.setStatus(HttpStatus.GONE.value());
			
			return false;
		}
		
		return true;
	}	
}

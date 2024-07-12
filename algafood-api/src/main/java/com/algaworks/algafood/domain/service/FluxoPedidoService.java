package com.algaworks.algafood.domain.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FluxoPedidoService {

	private final EmissaoPedidoService emissaoPedidoService;

	@Transactional
	public void confirmar(Long pedidoId) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

		validarStatusPedido(pedido, StatusPedido.CRIADO, StatusPedido.CONFIRMADO);

		pedido.setStatus(StatusPedido.CONFIRMADO);
		pedido.setDataConfirmacao(OffsetDateTime.now());
	}

	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

		validarStatusPedido(pedido, StatusPedido.CRIADO, StatusPedido.CANCELADO);

		pedido.setStatus(StatusPedido.CANCELADO);
		pedido.setDataCancelamento(OffsetDateTime.now());
	}

	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

		validarStatusPedido(pedido, StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE);

		pedido.setStatus(StatusPedido.ENTREGUE);
		pedido.setDataEntrega(OffsetDateTime.now());
	}

	private void validarStatusPedido(Pedido pedido, StatusPedido statusEsperado, StatusPedido statusDestino) {
		if (!pedido.getStatus().equals(statusEsperado)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser alterado de %s para %s",
					pedido.getId(), pedido.getStatus().getDescricao(),
					statusDestino.getDescricao()));
		}
	}

}

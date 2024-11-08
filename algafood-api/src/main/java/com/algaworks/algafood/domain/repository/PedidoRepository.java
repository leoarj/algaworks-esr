package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;


@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>,
	JpaSpecificationExecutor<Pedido> {
	
	@Query("from Pedido p join fetch p.itens ip join fetch ip.produto join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha join fetch p.enderecoEntrega.cidade ce join fetch ce.estado join fetch p.formaPagamento where p.codigo = :codigo")
	Optional<Pedido> findByCodigo(String codigo);
	
	// Consulta otimizad para findAll (evitar problema N+1)
	@Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
	List<Pedido> findAll();

//	@Query("select r.id from Pedido p join p.restaurante r where p.codigo = :codigoPedido")
//	Long getRestauranteIdByCodigoPedido(String codigoPedido);
	
	boolean isPedidoGerenciadoPor(String codigoPedido, Long usuarioId);
	
}

package com.gvendas.gestaovendas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gvendas.gestaovendas.entities.ItemVenda;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

	@Query("select new com.gvendas.gestaovendas.entities.ItemVenda("
			+ " iv.codigo, iv.quantidade, iv.precoVendido, iv.produto, iv.venda)"
			+ " from ItemVenda iv"
			+ " where iv.venda.codigo = :codigoVenda")
	List<ItemVenda> findByVendaCodigo(Long codigoVenda);
}

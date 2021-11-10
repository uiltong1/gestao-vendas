package com.gvendas.gestaovendas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gvendas.gestaovendas.entities.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

	List<Venda> findByClienteCodigo(Long codigoCliente);
}

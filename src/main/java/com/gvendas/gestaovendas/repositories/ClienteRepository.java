package com.gvendas.gestaovendas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gvendas.gestaovendas.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	Cliente findByNome(String nome);
}

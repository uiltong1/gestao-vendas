package com.gvendas.gestaovendas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gvendas.gestaovendas.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findByCategoriaCodigo(Long codigo);

	@Query("Select prod from Produto prod where prod.codigo = :codigo and prod.categoria.codigo = :codigoCategoria ")
	Optional<Produto> searchCodigoProduto(Long codigo, Long codigoCategoria);
	
	Optional<Produto> findByCategoriaCodigoAndDescricao(Long codigoCategoria, String descricao);

}

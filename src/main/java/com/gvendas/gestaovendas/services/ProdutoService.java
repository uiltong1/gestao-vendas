package com.gvendas.gestaovendas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gvendas.gestaovendas.Exception.GenericException;
import com.gvendas.gestaovendas.entities.Categoria;
import com.gvendas.gestaovendas.entities.Produto;
import com.gvendas.gestaovendas.repositories.CategoriaRepository;
import com.gvendas.gestaovendas.repositories.ProdutoRepository;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaService categoriaService;

	public List<Produto> list() {
		return produtoRepository.findAll();
	}

	public Optional<Produto> getById(Long codigo) {
		return produtoRepository.findById(codigo);
	}

	public List<Produto> listByCategoria(Long codigo) {
		return produtoRepository.findByCategoriaCodigo(codigo);
	}

	public Optional<Produto> searchCodigoProduto(Long codigo, Long codigoCategoria) {
		return produtoRepository.searchCodigoProduto(codigo, codigoCategoria);
	}

	public Produto save(Long codigoCategoria, Produto produto) {
		validateExistCategoria(codigoCategoria);
		validateExistProdutoCategoria(produto);
		return produtoRepository.save(produto);
	}

	public Produto update(Long codigo, Long codigoCategoria, Produto produto) {
		Produto produtoSalvar = validateExistProduto(codigo, codigoCategoria);
		validateExistCategoria(codigoCategoria);
		validateExistProdutoCategoria(produto);
		BeanUtils.copyProperties(produto, produtoSalvar, "codigo");
		return produtoRepository.save(produtoSalvar);
	}

	public void destroy(Long codigoProduto, Long codigoCategoria) {
		Produto produto = validateExistProduto(codigoProduto, codigoCategoria);
		produtoRepository.delete(produto);
	}

	public void updateQuantideProdutoEstoque(Produto produto) {
		produtoRepository.save(produto);
	}

	private Produto validateExistProduto(Long codigo, Long codigoCategoria) {
		Optional<Produto> produto = searchCodigoProduto(codigo, codigoCategoria);
		if (produto.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return produto.get();
	}

	public Produto validateExistProduto(Long codigo) {
		Optional<Produto> produto = produtoRepository.findById(codigo);
		if (produto.isEmpty()) {
			throw new GenericException(String.format("O produto de código %s não existe.", codigo));
		}
		return produto.get();
	}

	private void validateExistCategoria(Long codigoCategoria) {
		if (codigoCategoria == null) {
			throw new GenericException("A categoria não pode ser nula.");
		}

		if (categoriaService.getById(codigoCategoria).isEmpty()) {
			throw new GenericException(String.format("A categoria %s não foi encontrada.", codigoCategoria));
		}

	}

	private void validateExistProdutoCategoria(Produto produto) {
		Optional<Produto> produtoDescricao = produtoRepository
				.findByCategoriaCodigoAndDescricao(produto.getCategoria().getCodigo(), produto.getDescricao());
		if (produtoDescricao.isPresent() && produtoDescricao.get().getCodigo() != produto.getCodigo()) {
			throw new GenericException(String.format("O produto %s já está cadastrado na categoria %s.",
					produto.getDescricao().toUpperCase(), produto.getCategoria().getCodigo()));
		}
	}
}

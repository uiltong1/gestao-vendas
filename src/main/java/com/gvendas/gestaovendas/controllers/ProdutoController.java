package com.gvendas.gestaovendas.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gvendas.gestaovendas.dto.produto.ProdutoRequestDTO;
import com.gvendas.gestaovendas.dto.produto.ProdutoResponseDTO;
import com.gvendas.gestaovendas.entities.Produto;
import com.gvendas.gestaovendas.services.ProdutoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Produto")
@RestController
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@ApiOperation(value = "Listar", nickname = "listProduto")
	@GetMapping
	public List<ProdutoResponseDTO> list() {
		return produtoService.list().stream().map(produto -> ProdutoResponseDTO.convertToProdutoDTO(produto))
				.collect(Collectors.toList());
	}

	@ApiOperation(value = "Listar produtos por categoria", nickname = "listProdutoByCategoria")
	@GetMapping("/categoria{codigoCategoria}")
	public List<ProdutoResponseDTO> listByCategoria(@PathVariable Long codigoCategoria) {
		return produtoService.listByCategoria(codigoCategoria).stream()
				.map(produto -> ProdutoResponseDTO.convertToProdutoDTO(produto)).collect(Collectors.toList());
	}

	@ApiOperation(value = "Consultar", nickname = "getProduto")
	@GetMapping("/{codigo}")
	public ResponseEntity<ProdutoResponseDTO> get(@PathVariable Long codigo) {
		Optional<Produto> produto = produtoService.getById(codigo);
		return produto.isPresent() ? ResponseEntity.ok(ProdutoResponseDTO.convertToProdutoDTO(produto.get()))
				: ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Buscar produto por código e categoria", nickname = "getByProdutoCategoria")
	@GetMapping("{codigo}/categoria{codigoCategoria}")
	public ResponseEntity<ProdutoResponseDTO> getByProdutoCategoria(@PathVariable Long codigo, Long codigoCategoria) {
		Optional<Produto> produto = produtoService.searchCodigoProduto(codigo, codigoCategoria);
		return produto.isPresent() ? ResponseEntity.ok(ProdutoResponseDTO.convertToProdutoDTO(produto.get()))
				: ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Salvar", nickname = "salvarProduto")
	@PostMapping("categoria/{codigoCategoria}")
	public ResponseEntity<ProdutoResponseDTO> save(@PathVariable Long codigoCategoria,
			@Valid @RequestBody ProdutoRequestDTO produto) {
		Produto produtoSalvar = produtoService.save(codigoCategoria, produto.convertEntityToProduto(codigoCategoria));
		return ResponseEntity.ok(ProdutoResponseDTO.convertToProdutoDTO(produtoSalvar));
	}

	@ApiOperation(value = "Atualizar", nickname = "atualizarProduto")
	@PutMapping("{codigoProduto}/categoria/{codigoCategoria}")
	public ResponseEntity<ProdutoResponseDTO> update(@PathVariable Long codigoProduto,
			@PathVariable Long codigoCategoria, @Valid @RequestBody ProdutoRequestDTO produto) {
		Produto produtoAtualizar = produtoService.update(codigoProduto, codigoCategoria,
				produto.convertEntityToProduto(codigoCategoria, codigoProduto));
		return ResponseEntity.ok(ProdutoResponseDTO.convertToProdutoDTO(produtoAtualizar));
	}

	@ApiOperation(value = "Deletar", nickname = "deletarProduto")
	@DeleteMapping("{codigoProduto}/categoria/{codigoCategoria}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void destroy(@PathVariable Long codigoProduto, @PathVariable Long codigoCategoria) {
		produtoService.destroy(codigoProduto, codigoCategoria);
	}
}

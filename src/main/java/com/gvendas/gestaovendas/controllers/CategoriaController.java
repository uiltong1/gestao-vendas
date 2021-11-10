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

import com.gvendas.gestaovendas.dto.categoria.CategoriaRequestDTO;
import com.gvendas.gestaovendas.dto.categoria.CategoriaResponseDTO;
import com.gvendas.gestaovendas.entities.Categoria;
import com.gvendas.gestaovendas.services.CategoriaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Categoria")
@RestController
@RequestMapping("/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	@ApiOperation(value = "Listar")
	@GetMapping
	public List<CategoriaResponseDTO> list() {
		return categoriaService.list().stream()
				.map(categoria -> CategoriaResponseDTO.convertCategoriaResponseDTO(categoria))
				.collect(Collectors.toList());
	}

	@ApiOperation(value = "Consultar")
	@GetMapping("/{codigo}")
	public ResponseEntity<CategoriaResponseDTO> getById(@PathVariable Long codigo) {
		Optional<Categoria> categoria = categoriaService.getById(codigo);
		return categoria.isPresent()
				? ResponseEntity.ok(CategoriaResponseDTO.convertCategoriaResponseDTO(categoria.get()))
				: ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Criar")
	@PostMapping
	public ResponseEntity<CategoriaResponseDTO> save(@Valid @RequestBody CategoriaRequestDTO categoriaDto) {
		Categoria categoriaResponse = categoriaService.save(categoriaDto.convetToEntidadeCategoria());
		return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.convertCategoriaResponseDTO(categoriaResponse));
	}

	@ApiOperation(value = "Atualizar")
	@PutMapping("/{codigo}")
	public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long codigo, @Valid @RequestBody CategoriaRequestDTO categoriaDto) {
		Categoria categoriaAtualizar = categoriaService.update(codigo, categoriaDto.convetToEntidadeCategoria(codigo));
		return ResponseEntity.ok(CategoriaResponseDTO.convertCategoriaResponseDTO(categoriaAtualizar));
	}

	@ApiOperation(value = "Deletar")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void destroy(@PathVariable Long codigo) {
		categoriaService.destroy(codigo);
	}

}

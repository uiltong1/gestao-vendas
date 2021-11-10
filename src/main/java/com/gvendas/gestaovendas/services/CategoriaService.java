package com.gvendas.gestaovendas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gvendas.gestaovendas.repositories.CategoriaRepository;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

import com.gvendas.gestaovendas.Exception.GenericException;
import com.gvendas.gestaovendas.entities.Categoria;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Categoria> list() {
		return categoriaRepository.findAll();
	}

	public Optional<Categoria> getById(Long id) {
		return categoriaRepository.findById(id);
	}

	public Categoria save(Categoria categoria) {
		catergoriaExist(categoria);
		return categoriaRepository.save(categoria);
	}

	public Categoria update(Long codigo, Categoria categoria) {
		Categoria categoriaData = verificaExistenciaCategoria(codigo);
		BeanUtils.copyProperties(categoria, categoriaData, "codigo");
		return categoriaRepository.save(categoriaData);
	}

	public void destroy(Long codigo) {
		categoriaRepository.deleteById(codigo);
	}

	private Categoria verificaExistenciaCategoria(Long codigo) {
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		if (categoria.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}

		return categoria.get();
	}

	private void catergoriaExist(Categoria categoria) {
		Categoria categoriaExist = categoriaRepository.findByNome(categoria.getNome());

		if (categoriaExist != null && categoriaExist.getCodigo() != categoria.getCodigo()) {
			throw new GenericException(String.format("A categoria %s já existe.", categoria.getNome().toUpperCase()));
		}
	}
}

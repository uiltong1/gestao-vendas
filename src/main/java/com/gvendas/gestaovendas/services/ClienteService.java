package com.gvendas.gestaovendas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gvendas.gestaovendas.Exception.GenericException;
import com.gvendas.gestaovendas.entities.Cliente;
import com.gvendas.gestaovendas.repositories.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public List<Cliente> list() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> getById(Long codigo) {
		return clienteRepository.findById(codigo);
	}

	public Cliente save(Cliente cliente) {
		validatedClienteDuplicado(cliente);
		return clienteRepository.save(cliente);
	}

	public Cliente update(Long codigo, Cliente cliente) {
		Cliente clienteExist = validatedClienteExist(codigo);
		validatedClienteDuplicado(cliente);
		BeanUtils.copyProperties(cliente, clienteExist, "codigo");
		return clienteRepository.save(clienteExist);
	}
	
	public void delete(Long codigo) {
		clienteRepository.deleteById(codigo);
	}

	private Cliente validatedClienteExist(Long codigo) {
		Optional<Cliente> cliente = getById(codigo);
		if (cliente.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return cliente.get();
	}

	private void validatedClienteDuplicado(Cliente cliente) {
		Cliente clienteExist = clienteRepository.findByNome(cliente.getNome());
		if (clienteExist != null && clienteExist.getCodigo() != cliente.getCodigo()) {
			throw new GenericException(String.format("O cliente %s já existe.", cliente.getNome().toUpperCase()));
		}
	}
}

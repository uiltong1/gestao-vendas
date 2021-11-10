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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gvendas.gestaovendas.dto.cliente.ClienteRequestDTO;
import com.gvendas.gestaovendas.dto.cliente.ClienteResponseDTO;
import com.gvendas.gestaovendas.entities.Cliente;
import com.gvendas.gestaovendas.repositories.ClienteRepository;
import com.gvendas.gestaovendas.services.ClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Cliente")
@RestController
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@ApiOperation(value = "Listar", nickname = "clientelist")
	@GetMapping
	public List<ClienteResponseDTO> list() {
		return clienteService.list().stream().map(cliente -> ClienteResponseDTO.convertClienteToClienteDTO(cliente))
				.collect(Collectors.toList());
	}

	@ApiOperation(value = "Consultar", nickname = "clienteGet")
	@GetMapping("/{codigo}")
	public ResponseEntity<ClienteResponseDTO> getById(Long codigo) {
		Optional<Cliente> cliente = clienteService.getById(codigo);
		return cliente.isPresent() ? ResponseEntity.ok(ClienteResponseDTO.convertClienteToClienteDTO(cliente.get()))
				: ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Cadastrar", nickname = "clientePost")
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> save(@Valid @RequestBody ClienteRequestDTO cliente) {
		Cliente clienteSave = clienteService.save(cliente.convertToEntityCliente());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ClienteResponseDTO.convertClienteToClienteDTO(clienteSave));
	}

	@ApiOperation(value = "Atualizar", nickname = "clientePut")
	@PostMapping("/{codigo}")
	public ResponseEntity<ClienteResponseDTO> update(@PathVariable Long codigo,
			@Valid @RequestBody ClienteRequestDTO cliente) {
		Cliente clienteUpdate = clienteService.update(codigo, cliente.convertToEntityCliente(codigo));
		return ResponseEntity.ok(ClienteResponseDTO.convertClienteToClienteDTO(clienteUpdate));
	}

	@ApiOperation(value = "Deletar", nickname = "clienteDelete")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long codigo) {
		clienteService.delete(codigo);
	}
}
package com.gvendas.gestaovendas.controllers;

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

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaRequestDTO;
import com.gvendas.gestaovendas.services.venda.VendaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Venda")
@RestController
@RequestMapping("/venda")
public class VendaController {

	@Autowired
	private VendaService vendaService;

	@ApiOperation(value = "Listar venda por cliente", nickname = "listVendaCliente")
	@GetMapping("/cliente/{codigoCliente}")
	public ResponseEntity<ClienteVendaResponseDTO> listVendaCliente(@PathVariable Long codigoCliente) {
		return ResponseEntity.ok(vendaService.listVendaCliente(codigoCliente));
	}

	@ApiOperation(value = "Listar venda por código", nickname = "getVendaCodigo")
	@GetMapping("/{codigo}")
	public ResponseEntity<ClienteVendaResponseDTO> getVendaCodigo(@PathVariable Long codigo) {
		return ResponseEntity.ok(vendaService.getVendaByCodigo(codigo));
	}

	@ApiOperation(value = "Registrar venda", nickname = "registrarVenda")
	@PostMapping("cliente/{codigoCliente}")
	public ResponseEntity<ClienteVendaResponseDTO> save(@PathVariable Long codigoCliente,
			@Valid @RequestBody VendaRequestDTO vendaDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.save(codigoCliente, vendaDto));
	}
	
	@ApiOperation(value = "Atualizar venda", nickname = "atualizarVenda")
	@PutMapping("{codigoVenda}/cliente/{codigoCliente}")
	public ResponseEntity<ClienteVendaResponseDTO> save(@PathVariable Long codigoVenda, @PathVariable Long codigoCliente,
			@Valid @RequestBody VendaRequestDTO vendaDto) {
		return ResponseEntity.ok(vendaService.update(codigoVenda, codigoCliente, vendaDto));
	}

	@ApiOperation(value = "Deletar", nickname = "deletarVenda")
	@DeleteMapping("/{codigoVenda}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void destroy(@PathVariable Long codigoVenda) {
		vendaService.delete(codigoVenda);
	}
}

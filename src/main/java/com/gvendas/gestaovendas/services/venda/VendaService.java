package com.gvendas.gestaovendas.services.venda;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gvendas.gestaovendas.Exception.GenericException;
import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.VendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entities.Cliente;
import com.gvendas.gestaovendas.entities.ItemVenda;
import com.gvendas.gestaovendas.entities.Produto;
import com.gvendas.gestaovendas.entities.Venda;
import com.gvendas.gestaovendas.repositories.ItemVendaRepository;
import com.gvendas.gestaovendas.repositories.VendaRepository;
import com.gvendas.gestaovendas.services.ClienteService;
import com.gvendas.gestaovendas.services.ProdutoService;

@Service
public class VendaService extends AbstractVendaService {

	private VendaRepository vendaRepository;
	private ClienteService clienteService;
	private ItemVendaRepository itemVendaRepository;
	private ProdutoService produtoService;

	@Autowired
	public VendaService(VendaRepository vendaRepository, ClienteService clienteService,
			ItemVendaRepository itemVendaRepository, ProdutoService produtoService) {
		this.vendaRepository = vendaRepository;
		this.clienteService = clienteService;
		this.itemVendaRepository = itemVendaRepository;
		this.produtoService = produtoService;

	}

	public ClienteVendaResponseDTO listVendaCliente(Long codigoCliente) {
		Cliente cliente = validatedClienteVendaExist(codigoCliente);
		List<VendaResponseDTO> vendaResponseDTO = vendaRepository.findByClienteCodigo(codigoCliente).stream()
				.map(venda -> createVendaResponseDTO(venda, itemVendaRepository.findByVendaCodigo(venda.getCodigo())))
				.collect(Collectors.toList());

		return new ClienteVendaResponseDTO(cliente.getNome(), vendaResponseDTO);
	}

	public ClienteVendaResponseDTO getVendaByCodigo(Long codigo) {
		Venda venda = validatedVendaExist(codigo);
		return convertClienteVendaResponseDTO(venda, itemVendaRepository.findByVendaCodigo(venda.getCodigo()));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public ClienteVendaResponseDTO save(Long codigoCliente, VendaRequestDTO vendaDto) {
		Cliente cliente = validatedClienteVendaExist(codigoCliente);
		validatedProdutoExist(vendaDto.getItensVendaDTO());
		Venda venda = saveVenda(cliente, vendaDto);
		return convertClienteVendaResponseDTO(venda, itemVendaRepository.findByVendaCodigo(venda.getCodigo()));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public ClienteVendaResponseDTO update(Long codigoVenda, Long codigoCliente, @Valid VendaRequestDTO vendaDto) {
		validatedVendaExist(codigoVenda);
		Cliente cliente = validatedClienteVendaExist(codigoCliente);
		List<ItemVenda> itensVenda = itemVendaRepository.findByVendaCodigo(codigoVenda);
		validarProdutoExisteEDevolver(itensVenda);
		itemVendaRepository.deleteAll(itensVenda);
		validatedProdutoExist(vendaDto.getItensVendaDTO());
		Venda saveVenda = atualizarVenda(codigoVenda, cliente, vendaDto);
		return convertClienteVendaResponseDTO(saveVenda, itemVendaRepository.findByVendaCodigo(codigoVenda));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void delete(Long codigoVenda) {
		validatedVendaExist(codigoVenda);
		List<ItemVenda> itensVenda = itemVendaRepository.findByVendaCodigo(codigoVenda);
		validarProdutoExisteEDevolver(itensVenda);
		itemVendaRepository.deleteAll(itensVenda);
		vendaRepository.deleteById(codigoVenda);
	}

	public void validarProdutoExisteEDevolver(List<ItemVenda> itensVenda) {
		itensVenda.forEach(item -> {
			Produto produto = produtoService.validateExistProduto(item.getProduto().getCodigo());
			produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
			produtoService.updateQuantideProdutoEstoque(produto);
		});
	}

	private Venda saveVenda(Cliente cliente, VendaRequestDTO vendaDto) {
		Venda venda = vendaRepository.save(new Venda(vendaDto.getData(), cliente));
		vendaDto.getItensVendaDTO().stream().map(itemVendaDto -> createItemVenda(itemVendaDto, venda))
				.forEach(itemVendaRepository::save);
		return venda;
	}

	private Venda atualizarVenda(Long codigoVenda, Cliente cliente, VendaRequestDTO vendaDto) {
		Venda venda = vendaRepository.save(new Venda(codigoVenda, vendaDto.getData(), cliente));
		vendaDto.getItensVendaDTO().stream().map(itemVendaDto -> createItemVenda(itemVendaDto, venda))
				.forEach(itemVendaRepository::save);
		return venda;
	}

	private void validatedProdutoExist(List<ItemVendaRequestDTO> itensVendaDTO) {
		itensVendaDTO.forEach(item -> {
			Produto produto = produtoService.validateExistProduto(item.getCodigoProduto());
			validateQuantidadeProdutoExist(produto, item.getQuantidade());
			produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
			produtoService.updateQuantideProdutoEstoque(produto);
		});
	}

	private void validateQuantidadeProdutoExist(Produto produto, Integer qtdeVenda) {
		if (!(produto.getQuantidade() >= qtdeVenda)) {
			throw new GenericException(String.format("A quantidade %s do produto %s não está disponível no estoque.",
					qtdeVenda, produto.getDescricao()));
		}
	}

	private Cliente validatedClienteVendaExist(Long codigoCliente) {
		Optional<Cliente> cliente = clienteService.getById(codigoCliente);
		if (cliente.isEmpty()) {
			throw new GenericException(
					String.format("O cliente de código %s informado não existe no cadastro.", codigoCliente));
		}
		return cliente.get();
	}

	protected Venda validatedVendaExist(Long codigo) {
		Optional<Venda> venda = vendaRepository.findById(codigo);
		if (venda.isEmpty()) {
			throw new GenericException(String.format("Venda de código %s não foi encontrada.", codigo));
		}

		return venda.get();
	}

}

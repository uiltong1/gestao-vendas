package com.gvendas.gestaovendas.services.venda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entities.ItemVenda;
import com.gvendas.gestaovendas.entities.Produto;
import com.gvendas.gestaovendas.entities.Venda;

public abstract class AbstractVendaService {

	protected VendaResponseDTO createVendaResponseDTO(Venda venda, List<ItemVenda> itemVendaList) {

		List<ItemVendaResponseDTO> itemVendaResponseDTO = itemVendaList.stream().map(this::createItensVendaResponseDTO)
				.collect(Collectors.toList());
		return new VendaResponseDTO(venda.getCodigo(), venda.getData(), itemVendaResponseDTO);
	}

	protected ItemVendaResponseDTO createItensVendaResponseDTO(ItemVenda itemVenda) {
		return new ItemVendaResponseDTO(itemVenda.getCodigo(), itemVenda.getQuantidade(), itemVenda.getPrecoVendido(),
				itemVenda.getProduto().getCodigo(), itemVenda.getProduto().getDescricao());
	}

	protected ClienteVendaResponseDTO convertClienteVendaResponseDTO(Venda venda, List<ItemVenda> itensVendaList) {
		return new ClienteVendaResponseDTO(venda.getCliente().getNome(),
				Arrays.asList(createVendaResponseDTO(venda, itensVendaList)));
	}

	protected ItemVenda createItemVenda(ItemVendaRequestDTO itemVendaDto, Venda venda) {
		return new ItemVenda(itemVendaDto.getQuantidade(), itemVendaDto.getPrecoVendido(),
				new Produto(itemVendaDto.getCodigoProduto()), venda);
	}

}

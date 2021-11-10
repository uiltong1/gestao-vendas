package com.gvendas.gestaovendas.dto.produto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.gvendas.gestaovendas.entities.Categoria;
import com.gvendas.gestaovendas.entities.Produto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Produto Request DTO")
public class ProdutoRequestDTO {

	@ApiModelProperty(value = "Descrição")
	@NotBlank(message = "Descrição")
	@Length(min = 3, max = 100, message = "Descrição")
	private String descricao;

	@ApiModelProperty(value = "Quantidade")
	@NotNull(message = "Quantidade")
	private Integer quantidade;

	@ApiModelProperty(value = "Preço curto")
	@NotNull(message = "Preço curto")
	private BigDecimal precoCurto;

	@ApiModelProperty(value = "Preço venda")
	@NotNull(message = "Preço venda")
	private BigDecimal precoVenda;

	@ApiModelProperty(value = "Observação")
	private String observacao;

	public Produto convertEntityToProduto(Long codigoCategoria) {
		return new Produto(descricao, quantidade, precoCurto, precoVenda, observacao, new Categoria(codigoCategoria));
	}
	
	public Produto convertEntityToProduto(Long codigoCategoria, Long codigoProduto) {
		return new Produto(codigoProduto, descricao, quantidade, precoCurto, precoVenda, observacao, new Categoria(codigoCategoria));
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoCurto() {
		return precoCurto;
	}

	public void setPrecoCurto(BigDecimal precoCurto) {
		this.precoCurto = precoCurto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}

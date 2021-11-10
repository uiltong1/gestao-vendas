package com.gvendas.gestaovendas.dto.cliente;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.gvendas.gestaovendas.entities.Cliente;
import com.gvendas.gestaovendas.entities.Endereco;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Cliente request")
public class ClienteRequestDTO {

	@ApiModelProperty(value = "Nome")
	@NotBlank(message = "Nome")
	@Length(min = 3, max = 256, message = "Nome")
	private String nome;

	@ApiModelProperty(value = "Telefone")
	@NotBlank(message = "Telefone")
	@Pattern(regexp = "\\([\\d]{2}\\)[\\d]{5}[- .][\\d]{4}", message = "Telefone")
	private String telefone;

	@ApiModelProperty(value = "Ativo")
	@NotNull(message = "Ativo")
	private Boolean ativo;

	@ApiModelProperty(value = "Emdereço")
	@NotNull(message = "Endereço")
	@Valid
	private EnderecoRequestDTO endereco;

	public Cliente convertToEntityCliente() {
		Endereco enderecoNew = new Endereco(endereco.getNumero(), endereco.getBairro(), endereco.getComplemento(),
				endereco.getCep(), endereco.getCidade(), endereco.getEstado());
		return new Cliente(nome, telefone, ativo, enderecoNew);
	}

	public Cliente convertToEntityCliente(Long codigo) {
		Endereco enderecoNew = new Endereco(endereco.getNumero(), endereco.getBairro(), endereco.getComplemento(),
				endereco.getCep(), endereco.getCidade(), endereco.getEstado());
		return new Cliente(codigo, nome, telefone, ativo, enderecoNew);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public EnderecoRequestDTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoRequestDTO endereco) {
		this.endereco = endereco;
	}

}

package com.ludmylla.spring.boot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message = "Nome não pode ser vazio!")
	@NotNull(message = "Nome não pode ser nulo!")
	@Size(min = 3, max = 30, message = "Nome não pode menor que 3 ou maior que 30!")
	private String nome;
	
	@NotEmpty(message = "Sobrenome não pode ser vazio!")
	@NotNull(message = "Sobrenome não pode ser nulo!")
	@Size(min = 3, max = 30, message = "Sobrenome não pode menor que 3 ou maior que 30!")
	private String sobrenome;
	
	
	@NotNull(message = "Idade não pode ser nulo!")
	@Min(value = 16, message = "Idade deve ser maior ou igual a 16")
	@Max(value = 70, message = "Idade deve ser menor ou igual a 70")
	private int idade;
	
	private String sexo;

	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.REMOVE)
	private List<Telefone> telefones = new ArrayList<>();
	
	private String cep;
	private String rua;
	private String bairro;
	private String cidade;
	private String uf;
	private String ibge;
	
	/*Muitas pessoas para esta profissão*/
	@ManyToOne
	@JoinColumn(name ="profissao_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Profissao profissao;
	
	@Enumerated(EnumType.STRING)
	private Cargo cargo;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Lob
	private byte[] curriculo;
	
	private String nomeFileCurriculo;
	private String tipoFileCurriculo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}
	
	public Profissao getProfissao() {
		return profissao;
	}
	
	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}
	
	public Cargo getCargo() {
		return cargo;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	public Date getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public byte[] getCurriculo() {
		return curriculo;
	}
	
	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}
	
	public String getNomeFileCurriculo() {
		return nomeFileCurriculo;
	}
	
	public void setNomeFileCurriculo(String nomeFileCurriculo) {
		this.nomeFileCurriculo = nomeFileCurriculo;
	}
	
	public String getTipoFileCurriculo() {
		return tipoFileCurriculo;
	}
	
	public void setTipoFileCurriculo(String tipoFileCurriculo) {
		this.tipoFileCurriculo = tipoFileCurriculo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", idade=" + idade + ", sexo="
				+ sexo + ", telefones=" + telefones + ", cep=" + cep + ", rua=" + rua + ", bairro=" + bairro
				+ ", cidade=" + cidade + ", uf=" + uf + ", ibge=" + ibge + ", profissao=" + profissao + ", cargo="
				+ cargo + ", dataNascimento=" + dataNascimento + ", curriculo=" + Arrays.toString(curriculo)
				+ ", nomeFileCurriculo=" + nomeFileCurriculo + ", tipoFileCurriculo=" + tipoFileCurriculo + "]";
	}

}

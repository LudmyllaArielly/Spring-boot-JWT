package com.ludmylla.spring.boot.model;

/*enum*/
public enum Cargo {
	
	JUNIOR("Júnior"),
	PLENO("Pleno"),
	SENIOR("Sênior");
	
	private String nome;
	private String valor;
	
	private Cargo(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getValor() {
		return valor = this.name();
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	/* Não é necessário por foi feito no getValor
	@Override
	public String toString() {
		return this.name();
	}*/
}

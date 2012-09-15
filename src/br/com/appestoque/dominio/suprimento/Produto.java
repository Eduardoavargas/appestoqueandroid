package br.com.appestoque.dominio.suprimento;

public class Produto {
	
	private Long id;
	private String nome;
	private String numero;
	private Double valor;
	private Double minimo;
	
	public Produto() {
	}
	
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
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo = minimo;
	}

}
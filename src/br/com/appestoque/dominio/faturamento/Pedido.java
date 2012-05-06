package br.com.appestoque.dominio.faturamento;

import java.util.Date;

import br.com.appestoque.dominio.cadastro.Cliente;

public class Pedido {

	private Long id;
	private String numero;
	private Date data;
	private String obs;
	private Cliente cliente;
	private Short sincronizado;
	
	public Pedido() {
		super();
	}
	
	public Pedido(	Long id, 
					String numero, 
					Date data, 
					String obs,
					Long idCliente) {
		super();
		this.id = id;
		this.numero = numero;
		this.data = data;
		this.obs = obs; 
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean getSincronizado() {
		return sincronizado.equals(new Short("1"));
	}

	public void setSincronizado(Short sincronizado) {
		this.sincronizado = sincronizado;
	}
	
}
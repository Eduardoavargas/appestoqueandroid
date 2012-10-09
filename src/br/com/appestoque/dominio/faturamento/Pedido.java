package br.com.appestoque.dominio.faturamento;

import java.util.Date;

import br.com.appestoque.dominio.cadastro.Cliente;

public class Pedido {

	public static final int PEDIDO_SEQUENCIA_ID 			= 0;
	public static final int PEDIDO_SEQUENCIA_QUANTIDADE 	= 1;
	
	private Long id;
	private String numero;
	private Date data;
	private String obs;
	private Cliente cliente;
	private Short sincronizado;
	private Double total;
	
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

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
}
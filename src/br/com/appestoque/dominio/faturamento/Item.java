package br.com.appestoque.dominio.faturamento;

import br.com.appestoque.dominio.suprimento.Produto;

public class Item {
	
	public static final int ITEM_SEQUENCIA_ID 			= 0;
	public static final int ITEM_SEQUENCIA_QUANTIDADE 	= 1;
	public static final int ITEM_SEQUENCIA_VALOR 		= 2;
	public static final int ITEM_SEQUENCIA_PRODUTO 		= 3;
	public static final int ITEM_SEQUENCIA_PEDIDO 		= 4;
	public static final int ITEM_SEQUENCIA_NUMERO 		= 5;
	public static final int ITEM_SEQUENCIA_OBS 			= 6;
	
	private Long id;	
	private Double quantidade;
	private Double valor;
	private Pedido pedido;
	private Produto produto;
	private String obs;
	
	public Item(Long id, Double valor, Double quantidade, Pedido pedido, Produto produto, String obs) {
		super();
		this.id = id;
		this.quantidade = quantidade;
		this.valor = valor;
		this.pedido = pedido;
		this.produto = produto;
		this.obs = obs;
	}
	
	public Item() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public Pedido getPedido() {
		return pedido;
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	
}
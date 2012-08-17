package br.com.appestoque.dominio.cadastro;

public class Cliente {

	public static final int CLIENTE_SEQUENCIA_ID 			= 0;
	public static final int CLIENTE_SEQUENCIA_NOME 			= 1;
	public static final int CLIENTE_SEQUENCIA_CNPJ 			= 2;
	public static final int CLIENTE_SEQUENCIA_ENDERECO 		= 3;
	public static final int CLIENTE_SEQUENCIA_NUMERO 		= 4;
	public static final int CLIENTE_SEQUENCIA_CEP 			= 5;
	public static final int CLIENTE_SEQUENCIA_COMPLEMENTO 	= 6;
	public static final int CLIENTE_SEQUENCIA_BAIRRO 		= 7;
	public static final int CLIENTE_SEQUENCIA_CIDADE 		= 8;
	public static final int CLIENTE_SEQUENCIA_SINCRONIZADO	= 9;
	
	private Long id;
	private String nome;
	private String cnpj;
	private String endereco;
	private Long numero;
	private String cep;
	private String complemento;
	private String bairro;
	private String cidade;
	private Long sincronizado;
	
	public Cliente() {
	}
	
	public Cliente(	Long id,
					String nome, 
					String cnpj, 
					String endereco,
					Long numero, 
					String cep, 
					String complemento, 
					String bairro,
					String cidade,
					Long sincronizado) {
		this.id = id;
		this.nome = nome;
		this.cnpj = cnpj;
		this.endereco = endereco;
		this.numero = numero;
		this.cep = cep;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cidade = cidade;
		this.sincronizado = sincronizado;
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
	
	public String getCnpj() {
		return cnpj;
	}
	
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public Long getNumero() {
		return numero;
	}
	
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
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
	
	public boolean isSincronizado(){
		return sincronizado==1L;
	}
	
	public void setSincronizado(boolean sincronzado) {
		this.sincronizado = sincronzado?1L:0L;
	}
	
}
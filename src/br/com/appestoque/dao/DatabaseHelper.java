package br.com.appestoque.dao;

import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	public static final String BASEDEDADOS_ARQUIVO = "appestoque.db";	
    
	private static final int BASEDEDADOS_VERSAO = 2;
	private static final int VERSAO_LANCAMENTO 	= 1;
	private static final int VERSAO_1 			= 2;
	
	public DatabaseHelper(Context context) {
        super(context, BASEDEDADOS_ARQUIVO, null, BASEDEDADOS_VERSAO);        
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(" create table "+ ProdutoDAO.TABELA 		+ " ( "
					+ ProdutoDAO.PRODUTO_CHAVE_ID       	+ " integer primary key,  				"
			        + ProdutoDAO.PRODUTO_CHAVE_NOME     	+ " text not null, 	 					" 
			        + ProdutoDAO.PRODUTO_CHAVE_NUMERO   	+ " text not null,	 	 				"
			        + ProdutoDAO.PRODUTO_CHAVE_VALOR    	+ " real not null	 	 				"
			        + "  ); ");
		
		db.execSQL(" create table "+ ClienteDAO.TABELA 		+ " ( "
					+ ClienteDAO.CLIENTE_CHAVE_ID        	+ " integer primary key,  				"
			        + ClienteDAO.CLIENTE_CHAVE_NOME      	+ " text not null, 	 					" 
			        + ClienteDAO.CLIENTE_CHAVE_CNPJ      	+ " text not null,	 	 				"
			        + ClienteDAO.CLIENTE_CHAVE_ENDERECO  	+ " text not null,	 	 				"
			        + ClienteDAO.CLIENTE_CHAVE_NUMERO    	+ " integer not null,	 				"
			        + ClienteDAO.CLIENTE_CHAVE_CEP       	+ " text not null,	 	 				"
			        + ClienteDAO.CLIENTE_CHAVE_COMPLEMENTO  + " text not null,	 	 				"
			        + ClienteDAO.CLIENTE_CHAVE_BAIRRO  		+ " text not null,	 	 				"
			        + ClienteDAO.CLIENTE_CHAVE_CIDADE  		+ " text not null	 	 				"
			        + "  ); ");
		
		db.execSQL(" create table "+ PedidoDAO.TABELA 		+ " ( "
					+ PedidoDAO.PEDIDO_CHAVE_ID  			+ " integer primary key AUTOINCREMENT,	"
			        + PedidoDAO.PEDIDO_CHAVE_NUMERO     	+ " text not null, 	 					" 
			        + PedidoDAO.PEDIDO_CHAVE_DATA     		+ " integer not null, 	 				"
			        + PedidoDAO.PEDIDO_CHAVE_OBS     		+ " text not null, 	 					"
			        + PedidoDAO.PEDIDO_CHAVE_CLIENTE     	+ " integer not null, 					"
			        + PedidoDAO.PEDIDO_CHAVE_SINCRONIZADO  	+ " integer DEFAULT 0 					"
			        + "  ); ");
		
		db.execSQL(" create table "+ ItemDAO.TABELA 		+ " ( "
					+ ItemDAO.ITEM_CHAVE_ID  				+ " integer primary key AUTOINCREMENT,	"
			        + ItemDAO.ITEM_CHAVE_QUANTIDADE     	+ " real not null, 	 					" 
			        + ItemDAO.ITEM_CHAVE_VALOR     			+ " real not null, 	 					"
			        + ItemDAO.ITEM_CHAVE_PRODUTO     		+ " integer not null, 	 				"			        
			        + ItemDAO.ITEM_CHAVE_PEDIDO     		+ " integer not null, 					"
			        + ItemDAO.ITEM_CHAVE_NUMERO   			+ " text null	 	 					"
			        + "  ); ");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (newVersion) {
			case VERSAO_LANCAMENTO: 
				onCreate(db);
				break;
			case VERSAO_1:
				db.execSQL( " alter table " + ItemDAO.TABELA + " add column  " + ItemDAO.ITEM_CHAVE_NUMERO + " text null ;" ); 
				break;
		}
	}
	
}
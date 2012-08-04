package br.com.appestoque.dao.faturamento;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.appestoque.dao.DatabaseHelper;
import br.com.appestoque.dao.IDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import br.com.appestoque.dominio.faturamento.Item;
import br.com.appestoque.dominio.faturamento.Pedido;
import br.com.appestoque.dominio.suprimento.Produto;

public class ItemDAO implements IDAO<Item,Long>{
	
	public static final String ITEM_CHAVE_ID = "_id";
	public static final String ITEM_CHAVE_QUANTIDADE = "quantidade";
	public static final String ITEM_CHAVE_VALOR = "valor";
	public static final String ITEM_CHAVE_PRODUTO = "idProduto";
	public static final String ITEM_CHAVE_PEDIDO = "idPedido";
	public static final String ITEM_CHAVE_NUMERO = "numero";
	
	public static final String TABELA = "itens";
	
	private DatabaseHelper databaseHelper;
	
	private SQLiteDatabase db;
	
	private Context context;
	
	public ItemDAO(Context context) {
		this.context = context;
		this.databaseHelper = new DatabaseHelper(context);		
    }
	
	@Override
	public Cursor listar() {
		Cursor cursor = db.query(TABELA, new String[] {	ITEM_CHAVE_ID,
														ITEM_CHAVE_QUANTIDADE,
														ITEM_CHAVE_VALOR,
														ITEM_CHAVE_PRODUTO,
														ITEM_CHAVE_PEDIDO}, 
														null, null, null, null, null);
    	return cursor;
	}
	
	public Cursor listar(long id) {
		Cursor cursor = db.query(TABELA, new String[] {	ITEM_CHAVE_ID,
														ITEM_CHAVE_QUANTIDADE,
														ITEM_CHAVE_VALOR,
														ITEM_CHAVE_PRODUTO,
														ITEM_CHAVE_PEDIDO,
														ITEM_CHAVE_NUMERO}, 
														ITEM_CHAVE_PEDIDO + " = " + id , null, null, null, ITEM_CHAVE_NUMERO + " asc ");
    	return cursor;
	}
	
	public long adicionar(Item item) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ITEM_CHAVE_QUANTIDADE,item.getQuantidade());
        initialValues.put(ITEM_CHAVE_VALOR,item.getValor());
        initialValues.put(ITEM_CHAVE_PRODUTO,item.getProduto().getId());
        initialValues.put(ITEM_CHAVE_NUMERO,item.getProduto().getNumero());
        initialValues.put(ITEM_CHAVE_PEDIDO,item.getPedido().getId());
        long ret = db.insert(TABELA, null,initialValues);
        return ret;
    }
	
	@Override
	public Item pesquisar(long id) {
		
    	Cursor cursor =  db.query(TABELA, new String[] {ITEM_CHAVE_ID,
														ITEM_CHAVE_QUANTIDADE,
														ITEM_CHAVE_VALOR,
														ITEM_CHAVE_PRODUTO,														
														ITEM_CHAVE_PEDIDO,
														ITEM_CHAVE_NUMERO}, ITEM_CHAVE_ID + " = " + id , 
    							null, null, null, null);
		
    	if(cursor.getCount()>0){
    		cursor.moveToFirst();
    		Item item = new Item();    		
    		item.setPedido(new Pedido());
    		item.setProduto(new Produto());
    		item.setId(cursor.getLong(Item.ITEM_SEQUENCIA_ID));
    		item.setQuantidade(cursor.getDouble(Item.ITEM_SEQUENCIA_QUANTIDADE));
    		item.setValor(cursor.getDouble(Item.ITEM_SEQUENCIA_VALOR));
    		
    		Cursor produtoCrs = db.query("produtos",
    				new String[] { ProdutoDAO.PRODUTO_CHAVE_ID, 
    							   ProdutoDAO.PRODUTO_CHAVE_NOME,
    							   ProdutoDAO.PRODUTO_CHAVE_NUMERO, 
    							   ProdutoDAO.PRODUTO_CHAVE_VALOR },
    							   ProdutoDAO.PRODUTO_CHAVE_ID + " = " + cursor.getLong(Item.ITEM_SEQUENCIA_PRODUTO), null, null, null, null);
    		Produto produto = new Produto();
    		if (produtoCrs.getCount() > 0) {
    			produtoCrs.moveToFirst();
    			produto.setId(produtoCrs.getLong(0));
    			produto.setNome(produtoCrs.getString(1));
    			produto.setNumero(produtoCrs.getString(2));
    			produto.setValor(produtoCrs.getDouble(3));
    			produtoCrs.close();
    		}
    		
    		item.setProduto(produto);
    		item.getPedido().setId(cursor.getLong(Item.ITEM_SEQUENCIA_PEDIDO));
    		
    		return item;
    	}else{
    		return null;
    	}
	}
	
	public long atualizar(Item item){
		ContentValues initialValues = new ContentValues();
		initialValues.put(ITEM_CHAVE_QUANTIDADE,item.getQuantidade());
        initialValues.put(ITEM_CHAVE_VALOR,item.getValor());
        initialValues.put(ITEM_CHAVE_PRODUTO,item.getProduto().getId());
        initialValues.put(ITEM_CHAVE_PEDIDO,item.getPedido().getId());
		return db.update(TABELA, initialValues, ITEM_CHAVE_ID + " = " + item.getId() , null);
	}
	
	@Override
	public void limpar() {
    	db.delete(TABELA, null, null);
	}
	
	public List<Item> listar(Pedido pedido) {
		Cursor cursor = db.query(TABELA, new String[] {	ITEM_CHAVE_ID,
														ITEM_CHAVE_QUANTIDADE,
														ITEM_CHAVE_VALOR,
														ITEM_CHAVE_PRODUTO,
														ITEM_CHAVE_PEDIDO}, 
														ITEM_CHAVE_PEDIDO + " = " + pedido.getId(), null, null, null, null);
		List<Item> itens = new ArrayList<Item>();
		if(cursor.getCount()>0){	
			ProdutoDAO produtoDAO = new ProdutoDAO(this.context);
			produtoDAO.abrir();
    		while(cursor.moveToNext()){
    			Produto produto = produtoDAO.pesquisar(cursor.getLong(3)); 
    			Item item = new Item(cursor.getLong(0),cursor.getDouble(2),cursor.getDouble(1),pedido, produto);
    			itens.add(item);
    		}    		
    		produtoDAO.fechar();
		}	
    	return itens;
	}
	
	public boolean remover(long id){
		return db.delete(TABELA, ITEM_CHAVE_ID + " = " + id , null) > 0;
	}
	
    public void fechar(){
    	this.databaseHelper.close();
    }
    
    public void abrir(){
    	db = databaseHelper.getReadableDatabase();
    }

}
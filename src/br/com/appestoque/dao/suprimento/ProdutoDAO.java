package br.com.appestoque.dao.suprimento;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.appestoque.dao.DatabaseHelper;
import br.com.appestoque.dao.IDAO;
import br.com.appestoque.dominio.suprimento.Produto;

public class ProdutoDAO implements IDAO<Produto,Long> {
	
	public static final String PRODUTO_CHAVE_ID = "_id";
	public static final String PRODUTO_CHAVE_NOME = "nome";
    public static final String PRODUTO_CHAVE_NUMERO = "numero";
    public static final String PRODUTO_CHAVE_VALOR = "valor";
    public static final String PRODUTO_CHAVE_MINIMO = "minimo";
    public static final String PRODUTO_CHAVE_QUANTIDADE = "quantidade";
    
    public static final String TABELA = "produtos";
    
	private DatabaseHelper databaseHelper;
	
	private SQLiteDatabase db;
	private Cursor cursor; 
	
	public ProdutoDAO(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
    }
	
	public Cursor listar() {		
		cursor = db.query(TABELA,
				new String[] { PRODUTO_CHAVE_ID, PRODUTO_CHAVE_NOME,
						PRODUTO_CHAVE_NUMERO, PRODUTO_CHAVE_VALOR, PRODUTO_CHAVE_MINIMO }, null,
				null, null, null, null);
		return cursor;
    }
	
	public List<Produto> produtos() {		
		cursor = db.query(TABELA,
				new String[] { PRODUTO_CHAVE_ID, PRODUTO_CHAVE_NOME,
							   PRODUTO_CHAVE_NUMERO, PRODUTO_CHAVE_VALOR , PRODUTO_CHAVE_MINIMO }, null,
				null, null, null, null);

		List<Produto> produtos = new ArrayList<Produto>();

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Produto produto = new Produto();
				produto.setId(cursor.getLong(cursor
						.getColumnIndex(ProdutoDAO.PRODUTO_CHAVE_ID)));
				produto.setNome(cursor.getString(cursor
						.getColumnIndex(ProdutoDAO.PRODUTO_CHAVE_NOME)));
				produto.setNumero(cursor.getString(cursor
						.getColumnIndex(ProdutoDAO.PRODUTO_CHAVE_NUMERO)));
				produto.setValor(cursor.getDouble(cursor
						.getColumnIndex(ProdutoDAO.PRODUTO_CHAVE_VALOR)));
				produto.setMinimo(cursor.getDouble(cursor
						.getColumnIndex(ProdutoDAO.PRODUTO_CHAVE_MINIMO)));
				produto.setQuantidade(cursor.getDouble(cursor
						.getColumnIndex(ProdutoDAO.PRODUTO_CHAVE_QUANTIDADE)));
				produtos.add(produto);
			}
		}
		return produtos;
    }
	
    public long criar(Long id, String nome, String numero, double valor, double minimo, double quantidade) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(PRODUTO_CHAVE_ID, id);
		initialValues.put(PRODUTO_CHAVE_NOME, nome);
		initialValues.put(PRODUTO_CHAVE_NUMERO, numero);
		initialValues.put(PRODUTO_CHAVE_VALOR, valor);
		initialValues.put(PRODUTO_CHAVE_MINIMO, minimo);
		initialValues.put(PRODUTO_CHAVE_QUANTIDADE, quantidade);
		long ret = db.insert(TABELA, null, initialValues);
		return ret;
    }
    
    public boolean atualizar(Produto produto) {
		ContentValues initialValues = new ContentValues();
		if(produto.getNome()!=null){
			initialValues.put(PRODUTO_CHAVE_NOME, produto.getNome());
		}
		if(produto.getValor()!=null){
			initialValues.put(PRODUTO_CHAVE_VALOR, produto.getValor());
		}
		if(produto.getMinimo()!=null){
			initialValues.put(PRODUTO_CHAVE_MINIMO, produto.getMinimo());
		}
		if(produto.getQuantidade()!=null){
			initialValues.put(PRODUTO_CHAVE_QUANTIDADE, produto.getQuantidade());
		}
		return db.update(TABELA, initialValues, PRODUTO_CHAVE_ID+"="+produto.getId(), null)>0;
    }
    
    public Cursor pesquisar(String numero) {
		cursor = db.query(TABELA,
				new String[] { 	PRODUTO_CHAVE_ID, 
								PRODUTO_CHAVE_NOME,
								PRODUTO_CHAVE_NUMERO, 
								PRODUTO_CHAVE_VALOR,
								PRODUTO_CHAVE_MINIMO,
								PRODUTO_CHAVE_QUANTIDADE},
								PRODUTO_CHAVE_NUMERO + " like '" + numero + "%'", null, null,
				null, null);
		return cursor;
    }
    
    public void limpar(){
    	db.delete(TABELA, null, null);
    }
    
    public Produto pesquisar(long id){
		cursor = db.query(TABELA,
				new String[] { PRODUTO_CHAVE_ID, 
							   PRODUTO_CHAVE_NOME,
							   PRODUTO_CHAVE_NUMERO, 
							   PRODUTO_CHAVE_VALOR,
							   PRODUTO_CHAVE_MINIMO,
							   PRODUTO_CHAVE_QUANTIDADE},
							   PRODUTO_CHAVE_ID + " = " + id, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			Produto produto = new Produto();
			produto.setId(cursor.getLong(0));
			produto.setNome(cursor.getString(1));
			produto.setNumero(cursor.getString(2));
			produto.setValor(cursor.getDouble(3));
			produto.setMinimo(cursor.getDouble(4));
			produto.setQuantidade(cursor.getDouble(5));
			cursor.close();
			return produto;
		} else {
			cursor.close();
			return null;
		}
    }
    
    public Produto consultar(String numero){
		cursor = db.query(TABELA,
				new String[] { 	PRODUTO_CHAVE_ID, 
								PRODUTO_CHAVE_NOME,
								PRODUTO_CHAVE_NUMERO, 
								PRODUTO_CHAVE_VALOR,
								PRODUTO_CHAVE_MINIMO,
								PRODUTO_CHAVE_QUANTIDADE},
								PRODUTO_CHAVE_NUMERO + " = '" + numero + "'", null, null, null,
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			Produto produto = new Produto();
			produto.setId(cursor.getLong(0));
			produto.setNome(cursor.getString(1));
			produto.setNumero(cursor.getString(2));
			produto.setValor(cursor.getDouble(3));
			produto.setMinimo(cursor.getDouble(4));
			produto.setQuantidade(cursor.getDouble(5));
			cursor.close();
			return produto;
		} else {
			return null;
		}
    }
    
    public Produto consultarNome(String nome){
		cursor = db.query(TABELA,
				new String[] { 	PRODUTO_CHAVE_ID, 
								PRODUTO_CHAVE_NOME,
								PRODUTO_CHAVE_NUMERO, 
								PRODUTO_CHAVE_VALOR,
								PRODUTO_CHAVE_MINIMO,
								PRODUTO_CHAVE_QUANTIDADE},
								PRODUTO_CHAVE_NOME + " = '" + nome + "'", null, null, null,
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			Produto produto = new Produto();
			produto.setId(cursor.getLong(0));
			produto.setNome(cursor.getString(1));
			produto.setNumero(cursor.getString(2));
			produto.setValor(cursor.getDouble(3));
			produto.setMinimo(cursor.getDouble(4));
			produto.setQuantidade(cursor.getDouble(5));
			cursor.close();
			return produto;
		} else {
			return null;
		}
    }
    
    public void fechar(){
    	this.databaseHelper.close();
    }
    
    public void abrir(){
    	db = databaseHelper.getReadableDatabase();
    }

}
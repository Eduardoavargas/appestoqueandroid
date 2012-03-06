package br.com.appestoque.dao.faturamento;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.appestoque.dao.DatabaseHelper;
import br.com.appestoque.dao.IDAO;
import br.com.appestoque.dominio.faturamento.Pedido;

public class PedidoDAO implements IDAO<Pedido,Long>{
	
	public static final String PEDIDO_CHAVE_ID = "_id";
	public static final String PEDIDO_CHAVE_NUMERO = "numero";
	public static final String PEDIDO_CHAVE_DATA = "data";
	public static final String PEDIDO_CHAVE_OBS = "obs";
	public static final String PEDIDO_CHAVE_CLIENTE = "idCliente";
	public static final String PEDIDO_CHAVE_SINCRONISMO = "sincronismo";
	
	public static final String TABELA = "pedidos";
	
	private DatabaseHelper databaseHelper;
	
	public PedidoDAO(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
    }
	
	@Override
	public Cursor listar() {
		SQLiteDatabase db = databaseHelper.getReadableDatabase(); 
		Cursor cursor = db.query(TABELA, new String[] {	PEDIDO_CHAVE_ID,
														PEDIDO_CHAVE_NUMERO,
														PEDIDO_CHAVE_DATA,
														PEDIDO_CHAVE_OBS,
														PEDIDO_CHAVE_CLIENTE,
														PEDIDO_CHAVE_SINCRONISMO}, 
														null, null, null, null, null);
    	return cursor;
	}
	
	public long criar(String numero, long data, String obs, Long idCliente) {
    	SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(PEDIDO_CHAVE_NUMERO,numero);
        initialValues.put(PEDIDO_CHAVE_DATA,data);
        initialValues.put(PEDIDO_CHAVE_OBS,obs);
        initialValues.put(PEDIDO_CHAVE_CLIENTE,idCliente);
        
        long ret = db.insert(TABELA, null,initialValues);
        return ret;
    }
	
	@Override
	public Pedido pesquisar(long id) {
    	SQLiteDatabase db = databaseHelper.getReadableDatabase(); 
    	Cursor cursor =  db.query(TABELA, new String[] {PEDIDO_CHAVE_ID,
														PEDIDO_CHAVE_NUMERO,
														PEDIDO_CHAVE_DATA,
														PEDIDO_CHAVE_OBS,
														PEDIDO_CHAVE_CLIENTE,
														PEDIDO_CHAVE_SINCRONISMO
														}, PEDIDO_CHAVE_ID + " = " + id , 
    							null, null, null, null);    	
    	if(cursor.getCount()>0){
    		cursor.moveToFirst();
    		Pedido pedido = new Pedido();
    		pedido.setId(cursor.getLong(0));
    		pedido.setNumero(cursor.getString(1));
    		pedido.setData(new Date(cursor.getLong(2)));
    		pedido.setObs(cursor.getString(3));
    		pedido.setIdCliente(cursor.getLong(4));
    		return pedido;
    	}else{
    		return null;
    	}
	}
	
	@Override
	public void limpar() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
    	db.delete(TABELA, null, null);
	}
	
	@Override
	public void fechar() {
		if(databaseHelper!=null){
    		databaseHelper.close();
    	}
	}

}
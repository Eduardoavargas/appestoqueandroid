package br.com.appestoque.dao.cadastro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.appestoque.dao.DatabaseHelper;
import br.com.appestoque.dao.IDAO;
import br.com.appestoque.dominio.cadastro.Cliente;

public class ClienteDAO implements IDAO<Cliente,Long>{

	public static final String CLIENTE_CHAVE_ID = "_id";
	public static final String CLIENTE_CHAVE_NOME = "nome";
	public static final String CLIENTE_CHAVE_CNPJ = "cnpj";
	public static final String CLIENTE_CHAVE_ENDERECO = "endereco";
	public static final String CLIENTE_CHAVE_NUMERO = "numero";
	public static final String CLIENTE_CHAVE_CEP = "cep";
	public static final String CLIENTE_CHAVE_COMPLEMENTO = "complemento";
	public static final String CLIENTE_CHAVE_BAIRRO = "bairro";
	public static final String CLIENTE_CHAVE_CIDADE = "cidade";

	public static final String TABELA = "clientes";
    
	private DatabaseHelper databaseHelper;
	
	public ClienteDAO(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
    }
	
	@Override
	public Cursor listar() {
		SQLiteDatabase db = databaseHelper.getReadableDatabase(); 
		Cursor cursor = db.query(TABELA, new String[] {	CLIENTE_CHAVE_ID, 
														CLIENTE_CHAVE_NOME, 
														CLIENTE_CHAVE_CNPJ, 
														CLIENTE_CHAVE_ENDERECO, 
														CLIENTE_CHAVE_NUMERO,
														CLIENTE_CHAVE_CEP, 
														CLIENTE_CHAVE_COMPLEMENTO, 
														CLIENTE_CHAVE_BAIRRO, 
														CLIENTE_CHAVE_CIDADE}, 
														null, null, null, null, null);
    	return cursor;
	}
	
	public long criar(	Long id,
						String nome, 
						String cnpj, 
						String endereco,
						Long numero, 
						String cep, 
						String complemento, 
						String bairro,
						String cidade) {
    	SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(CLIENTE_CHAVE_ID,id);
        initialValues.put(CLIENTE_CHAVE_NOME,nome); 
        initialValues.put(CLIENTE_CHAVE_CNPJ,cnpj); 
        initialValues.put(CLIENTE_CHAVE_ENDERECO,endereco);
        initialValues.put(CLIENTE_CHAVE_NUMERO,numero); 
        initialValues.put(CLIENTE_CHAVE_CEP,cep); 
        initialValues.put(CLIENTE_CHAVE_COMPLEMENTO,complemento); 
        initialValues.put(CLIENTE_CHAVE_BAIRRO,bairro);
        initialValues.put(CLIENTE_CHAVE_CIDADE,cidade);
        long ret = db.insert(TABELA, null,initialValues);
        return ret;
    }

	@Override
	public Cliente pesquisar(long id) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cliente objeto = null;
    	Cursor cursor =  db.query(TABELA, new String[] {CLIENTE_CHAVE_ID, 
														CLIENTE_CHAVE_NOME, 
														CLIENTE_CHAVE_CNPJ, 
														CLIENTE_CHAVE_ENDERECO, 
														CLIENTE_CHAVE_NUMERO,
														CLIENTE_CHAVE_CEP, 
														CLIENTE_CHAVE_COMPLEMENTO, 
														CLIENTE_CHAVE_BAIRRO, 
														CLIENTE_CHAVE_CIDADE}, 
														CLIENTE_CHAVE_ID + " = " + id ,
														null, null, null, null);    	
    	if(cursor.getCount()>0){
    		cursor.moveToFirst();
			objeto = new Cliente(cursor.getLong(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getLong(4), cursor.getString(5),
					cursor.getString(6), cursor.getString(7),
					cursor.getString(8));
    		
    	}
    	return objeto;
	}
	
    public Cursor pesquisar(String nome) {
    	SQLiteDatabase db = databaseHelper.getReadableDatabase(); 
    	Cursor cursor = db.query(TABELA, new String[] {	CLIENTE_CHAVE_ID, 
														CLIENTE_CHAVE_NOME, 
														CLIENTE_CHAVE_CNPJ, 
														CLIENTE_CHAVE_ENDERECO, 
														CLIENTE_CHAVE_NUMERO,
														CLIENTE_CHAVE_CEP, 
														CLIENTE_CHAVE_COMPLEMENTO, 
														CLIENTE_CHAVE_BAIRRO, 
														CLIENTE_CHAVE_CIDADE}, 
														CLIENTE_CHAVE_NOME + " like '" + nome + "%'" , 
														null, null, null, null);
    	return cursor;
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
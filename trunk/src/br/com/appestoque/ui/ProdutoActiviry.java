package br.com.appestoque.ui;

import br.com.appestoque.provider.ProdutoDbAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;

public class ProdutoActiviry extends ListActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProdutoDbAdapter produtoDbAdapter = new ProdutoDbAdapter(this);
		produtoDbAdapter.open();
		Cursor cursor = produtoDbAdapter.listar();
		startManagingCursor(cursor);
		String[] from = new String[]{ProdutoDbAdapter.PRODUTO_CHAVE_NOME,ProdutoDbAdapter.PRODUTO_CHAVE_NUMERO};
		int[] to = new int[]{android.R.id.text1, android.R.id.text2};
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to);
		setListAdapter(simpleCursorAdapter);
		produtoDbAdapter.close();
	}
    
    public void onHomeClick(View v) {
    	final Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
	
}
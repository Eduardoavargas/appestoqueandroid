package br.com.appestoque.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import br.com.appestoque.Constantes;
import br.com.appestoque.HttpCliente;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ProdutoActivity extends BaseListaAtividade{
	
	private ProdutoDAO produtoDAO;
	private ProgressDialog progressDialog;
	
	private List <NameValuePair> parametros;
	
	private String uuid;
	private String url;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);	
			progressDialog.dismiss();
		}
	};

	private class ProdutosAdapter extends CursorAdapter {

		public ProdutosAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final TextView numero = (TextView) view.findViewById(R.id.numero);
            final TextView nome = (TextView) view.findViewById(R.id.nome);
            numero.setText(cursor.getString(2));
            nome.setText(cursor.getString(1));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.produto_activity_lista, parent, false);
		}
		
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();		
		setContentView(R.layout.produto_activity);
		if(produtoDAO==null){
			produtoDAO = new ProdutoDAO(this);
		}
		Cursor cursor = null;
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        cursor = produtoDAO.pesquisar(query);
	    }else{
	    	cursor = produtoDAO.listar();
	    }
	    startManagingCursor(cursor);	    
		setListAdapter(new ProdutosAdapter(this,cursor));
		registerForContextMenu(getListView());
	}
    
    public void onAtualizarClick(View v) {
    	
		produtoDAO = new ProdutoDAO(this);

		Context context = getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (connectivity != null) {
			NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
				this.uuid = preferencias.getString("UUID", UUID.randomUUID().toString());
				url = Constantes.SERVIDOR + Constantes.RESTFUL_PRODUTO;
				parametros = new ArrayList <NameValuePair>();
				parametros.add(new BasicNameValuePair("uuid",uuid));
				progressDialog = ProgressDialog.show(this,"",getString(R.string.mensagem_conexao),true);
				if(HttpCliente.checarServidor(url,parametros,ProdutoActivity.this)){
					progressDialog = ProgressDialog.show(this, "", getString(R.string.mensagem_1) , true);
					this.runOnUiThread(new Runnable() {
						public void run() {
							try {
								JSONArray objetos = HttpCliente.ReceiveHttpPost(url,parametros,ProdutoActivity.this);
								if (objetos != null) {
									produtoDAO.limpar();
									Long id;
									String nome = null;
									String numero = null;
									Double preco = null;
									for (int i = 0; i <= objetos.length() - 1; ++i) {
										id = objetos.getJSONObject(i).getLong(ProdutoDAO.PRODUTO_CHAVE_ID);
										nome = objetos.getJSONObject(i).getString(ProdutoDAO.PRODUTO_CHAVE_NOME);
										numero = objetos.getJSONObject(i).getString(ProdutoDAO.PRODUTO_CHAVE_NUMERO);
										preco = objetos.getJSONObject(i).getDouble(ProdutoDAO.PRODUTO_CHAVE_VALOR);
										produtoDAO.criar(id, nome, numero, preco);
									}
								}else{
									Util.dialogo(ProdutoActivity.this,getString(R.string.mensagem_5));
								}
							} catch (Exception e) {
								Util.dialogo(ProdutoActivity.this,e.getMessage());
							}
							handler.sendEmptyMessage(0);
						}
					});
				}else{
					Util.dialogo(ProdutoActivity.this,getString(R.string.mensagem_servidor_nao_responde));
					progressDialog.dismiss();
				}
			} else {
				Util.dialogo(ProdutoActivity.this,getString(R.string.mensagem_2));
			}
		} else {
			Util.dialogo(ProdutoActivity.this, getString(R.string.mensagem_3));
		}	
    	
    }
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.produto_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Intent intent = null;
		switch (item.getItemId()) {
			case R.id.item_menu_sincronizar:
				Toast.makeText(getApplicationContext(), "Sincronizar", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.item_menu_visualizar:
				intent = new Intent(this, ProdutoEditarActivity.class);
		    	intent.putExtra(ProdutoDAO.PRODUTO_CHAVE_ID, info.id);
		    	startActivity(intent);				
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	produtoDAO.fechar();
    }
	
}
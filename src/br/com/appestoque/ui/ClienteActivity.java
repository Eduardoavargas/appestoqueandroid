package br.com.appestoque.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import br.com.appestoque.Constantes;
import br.com.appestoque.HttpCliente;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;

public class ClienteActivity extends BaseListaAtividade{
	
	private ClienteDAO clienteDAO;
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

	private class ClientesAdapter extends CursorAdapter {

		public ClientesAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final TextView cnpj = (TextView) view.findViewById(R.id.cnpj);
            final TextView nome = (TextView) view.findViewById(R.id.nome);            
            nome.setText(cursor.getString(1));
            cnpj.setText(cursor.getString(2));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.cliente_activity_lista, parent, false);
		}
		
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		
		setContentView(R.layout.cliente_activity);
		if(clienteDAO==null){
			clienteDAO = new ClienteDAO(this);
		}
		Cursor cursor = null;
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        cursor = clienteDAO.pesquisar(query);
	    }else{
	    	cursor = clienteDAO.listar();
	    }
	    startManagingCursor(cursor);	    
		setListAdapter(new ClientesAdapter(this,cursor));
		registerForContextMenu(getListView());
	}
	
    public void onAtualizarClick(View v) {
		clienteDAO = new ClienteDAO(this);
		Context context = getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
				this.uuid = preferencias.getString("UUID", UUID.randomUUID().toString());
				url = Constantes.SERVIDOR + Constantes.RESTFUL_CLIENTE;
				parametros = new ArrayList <NameValuePair>();
				parametros.add(new BasicNameValuePair("uuid",uuid));
				progressDialog = ProgressDialog.show(this,"",getString(R.string.mensagem_conexao),true);
				if(HttpCliente.checarServidor(url,parametros,ClienteActivity.this)){
					progressDialog.dismiss();
					progressDialog = ProgressDialog.show(this, "", getString(R.string.mensagem_1) , true);
					this.runOnUiThread(new Runnable() {
						public void run() {
							try {
								parametros.add(new BasicNameValuePair("sincronismo","true"));
								JSONArray objetos = HttpCliente.ReceiveHttpPost(url,parametros,ClienteActivity.this);
								if (objetos != null) {
									clienteDAO.limpar();
									Long id = null;
									String nome = null;
									String cnpj = null;
									String endereco = null;
									Long numero = null;
									String cep = null;
									String complemento = null;
									String bairro = null;
									String cidade = null;
									for (int i = 0; i <= objetos.length() - 1; ++i) {
										id = objetos.getJSONObject(i).getLong(ClienteDAO.CLIENTE_CHAVE_ID);
										nome = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_NOME);
										cnpj = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_CNPJ);
										endereco = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_ENDERECO);
										numero = objetos.getJSONObject(i).getLong(ClienteDAO.CLIENTE_CHAVE_NUMERO);
										cep = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_CEP);
										complemento = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_COMPLEMENTO);
										bairro = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_BAIRRO);
										cidade = objetos.getJSONObject(i).getString(ClienteDAO.CLIENTE_CHAVE_CIDADE);
										clienteDAO.criar(id, nome, cnpj, endereco, numero, cep, complemento, bairro, cidade);
									}
								}else{
									Util.dialogo(ClienteActivity.this,getString(R.string.mensagem_6));
								}						
							} catch (Exception e) {
								Util.dialogo(ClienteActivity.this,e.getMessage());
							}
							handler.sendEmptyMessage(0);
						}
					});
				}else{
					progressDialog.dismiss();
					Util.dialogo(ClienteActivity.this,getString(R.string.mensagem_servidor_nao_responde));
				}
			} else {
				Util.dialogo(ClienteActivity.this,getString(R.string.mensagem_2));
			}
		} else {
			Util.dialogo(ClienteActivity.this, getString(R.string.mensagem_3));
		}	
    	
    }
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cliente_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Intent intent = null;
		switch (item.getItemId()) {
			case R.id.item_menu_criar_pedido:
				intent = new Intent(this, PedidoIncluirActivity.class);
				intent.putExtra(ClienteDAO.CLIENTE_CHAVE_ID, info.id);
		    	startActivity(intent);
				return true;
			case R.id.item_menu_sincronizar:
				Toast.makeText(getApplicationContext(), "Sincronizar", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.item_menu_visualizar:
				intent = new Intent(this, ClienteEditarActivity.class);
		    	intent.putExtra(ClienteDAO.CLIENTE_CHAVE_ID, info.id);
		    	startActivity(intent);				
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	clienteDAO.fechar();
    }

}
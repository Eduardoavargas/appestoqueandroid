package br.com.appestoque.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.stream.JsonReader;

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

public class ClienteActivity extends BaseListaAtividade implements Runnable{
	
	private ClienteDAO clienteDAO;
	private ProgressDialog progressDialog;
	private List <NameValuePair> parametros;
	
	public void run() {
		SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
		String uuid = preferencias.getString("UUID", UUID.randomUUID().toString());
		String url = Constantes.SERVIDOR + Constantes.RESTFUL_CLIENTE;
		parametros = new ArrayList<NameValuePair>();
		parametros.add(new BasicNameValuePair("uuid", uuid));
		parametros.add(new BasicNameValuePair("sincronismo","true"));
		InputStream inputStream = HttpCliente.recebeDados(url, parametros,ClienteActivity.this);
		if (inputStream != null) {
			try{
				JsonReader reader = new JsonReader(new InputStreamReader(inputStream,"UTF-8"));
				Long id = null;
				String nome = null;
				String cnpj = null;
				String endereco = null;
				Long numero = null;
				String cep = null;
				String complemento = null;
				String bairro = null;
				String cidade = null;
				try {
					clienteDAO.limpar();
					reader.beginArray();
				     while (reader.hasNext()) {
				    	 reader.beginObject();
				         while (reader.hasNext()) {
					           String name = reader.nextName();
					           if (name.equals("_id")) {
					        	   id = reader.nextLong();
					           } else if (name.equals("nome")) {
					        	   nome = reader.nextString();
					           } else if (name.equals("cnpj") ) {
					        	   cnpj = reader.nextString();
					           } else if (name.equals("endereco")) {
					        	   endereco = reader.nextString();
					           } else if (name.equals("endereco")) {
					        	   endereco = reader.nextString();
					           } else if (name.equals("numero")) {
					        	   numero = reader.nextLong();
					           } else if (name.equals("cep")) {
					        	   cep = reader.nextString();
					           } else if (name.equals("complemento")) {
					        	   complemento = reader.nextString();
					           } else if (name.equals("bairro")) {
					        	   bairro = reader.nextString();
					           } else if (name.equals("cidade")) {
					        	   cidade = reader.nextString();
					           } else {
					        	   reader.skipValue();
					           }
				         }
				         reader.endObject();
				         clienteDAO.criar(id, nome, cnpj, endereco, numero, cep, complemento, bairro, cidade);
				     }
				     reader.endArray();
				}finally {
					reader.close();
				}
			} catch (ClientProtocolException e) {
				Util.dialogo(ClienteActivity.this,e.getMessage());
			} catch (IOException e) {
				Util.dialogo(ClienteActivity.this,e.getMessage());
			}
			
		}
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
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
				progressDialog = ProgressDialog.show(this, "", getString(R.string.mensagem_1) , true);
				Thread thread = new Thread(this);
				thread.start();
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
    	clienteDAO.fechar();
    	super.onDestroy();
    }

}
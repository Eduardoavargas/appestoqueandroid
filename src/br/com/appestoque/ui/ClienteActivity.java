package br.com.appestoque.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

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
import android.os.Looper;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.appestoque.Constantes;
import br.com.appestoque.HttpCliente;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.dominio.cadastro.Cliente;
import br.com.appestoque.seguranca.Criptografia;
import br.com.appestoque.util.Conversor;

public class ClienteActivity extends BaseListaAtividade implements Runnable{
	
	private ProgressDialog progressDialog;
	private List <NameValuePair> parametros;
	
	private ClienteDAO clienteDAO;
	
	public void run() {
		Looper.prepare();
		Message message = new Message();
		SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
		String email = preferencias.getString("email", null);
		String senha = preferencias.getString("senha", null);
		String url = Constantes.SERVIDOR + Constantes.RESTFUL_CLIENTE;
		Criptografia criptografia = new Criptografia();
		parametros = new ArrayList<NameValuePair>();
		parametros.add(new BasicNameValuePair("email", email));
		try {
			parametros.add(new BasicNameValuePair("senha",Conversor.byteToString(criptografia.cifrar(senha),br.com.appestoque.util.Constantes.DELIMITADOR)));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					clienteDAO.abrir();
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
				         Cliente cliente = clienteDAO.pesquisar(id);
				         if(cliente==null){
				        	 clienteDAO.criar(id, nome, cnpj, endereco, numero, cep, complemento, bairro, cidade,true);
				         }else if(!cliente.getNome().equals(nome)||!cliente.getCnpj().equals(cnpj)||!cliente.getEndereco().equals(endereco)
				        		 ||!cliente.getNumero().equals(numero)||!cliente.getCep().equals(cep)
				        		 ||!cliente.getComplemento().equals(complemento)||!cliente.getBairro().equals(bairro)
				        		 ||!cliente.getCidade().equals(cidade)){
				        	 cliente.setNome(!cliente.getNome().equals(nome)?nome:null);
				        	 cliente.setCnpj(!cliente.getCnpj().equals(cnpj)?cnpj:null);
				        	 cliente.setEndereco(!cliente.getEndereco().equals(endereco)?endereco:null);
				        	 cliente.setNumero(!cliente.getNumero().equals(numero)?numero:null);
				        	 cliente.setCep(!cliente.getCep().equals(cep)?cep:null);
				        	 cliente.setComplemento(!cliente.getComplemento().equals(complemento)?complemento:null);
				        	 cliente.setBairro(!cliente.getBairro().equals(bairro)?bairro:null);
				        	 cliente.setCidade(!cliente.getCidade().equals(cidade)?cidade:null);
				        	 clienteDAO.atualizar(cliente);
				         }
				         
				     }
				     reader.endArray();
				}finally {
					clienteDAO.fechar();
					reader.close();
				}			
				message.what = Constantes.SUCESSO;
			} catch (ClientProtocolException e) {
				message.what = Constantes.FALHA;
				Bundle bundle = new Bundle();
				bundle.putString("mensagem", e.getMessage());
				message.setData(bundle);
			} catch (IOException e) {
				message.what = Constantes.FALHA;
				Bundle bundle = new Bundle();
				bundle.putString("mensagem", e.getMessage());
				message.setData(bundle);
			}
			
		}
		handler.sendMessage(message);
		Looper.loop();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			switch (msg.what) {
				case Constantes.SUCESSO:
					Util.dialogo(ClienteActivity.this,getString(R.string.mensagem_sincronismo_conclusao));
					break;
				case Constantes.FALHA:
					Util.dialogo(ClienteActivity.this, msg.getData().getString("mensagem"));
					break;	
				default:
					break;
			}
			
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
	protected void onResume(){
		Intent intent = getIntent();		
		setContentView(R.layout.cliente_activity);
		if(clienteDAO==null){
			clienteDAO = new ClienteDAO(this);
		}
		clienteDAO.abrir();
		Cursor cursor = null;
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        cursor = clienteDAO.pesquisar(query);
	    }else{
	    	cursor = clienteDAO.listar();
	    }
		setListAdapter(new ClientesAdapter(this,cursor));
		registerForContextMenu(getListView());
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		clienteDAO.fechar();
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		clienteDAO.fechar();
		super.onStop();
	}

	public void onAtualizarClick(View v) {
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
	
	public void onAdicionarClick(View v) {
    	startActivity(new Intent(this, ClienteAdicionarActivity.class));
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
	
	public void onListItemClick(ListView listView, View view, int position, long itemId){
		super.onListItemClick(listView, view, position, itemId);
		Intent intent = new Intent(this, ClienteEditarActivity.class);
    	intent.putExtra(ClienteDAO.CLIENTE_CHAVE_ID, itemId);
    	startActivity(intent);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		clienteDAO.fechar();
	}
    
}
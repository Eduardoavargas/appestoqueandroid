package br.com.appestoque.ui;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint.Join;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import br.com.appestoque.Constantes;
import br.com.appestoque.HttpCliente;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import br.com.appestoque.dominio.cadastro.Cliente;
import br.com.appestoque.dominio.faturamento.Item;
import br.com.appestoque.dominio.faturamento.Pedido;
import br.com.appestoque.seguranca.Criptografia;
import br.com.appestoque.util.Conversor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("unused")
public class PedidoActivity extends BaseListaAtividade implements Runnable{
 
	private PedidoDAO pedidoDAO;
	private ItemDAO itemDAO;
	private ClienteDAO clienteDAO;
	private ProgressDialog progressDialog;
	private Long idPedido;
	private String uuid;
	private String url;
	private List <NameValuePair> parametros;
	
	private Pedido pedido;
	
	public void processarPedido(Pedido pedido){
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case Constantes.SUCESSO:
					final View iconView = findViewById(android.R.id.icon1);
		            LayerDrawable iconDrawable = (LayerDrawable) iconView.getBackground();
		            iconDrawable.getDrawable(0).setColorFilter(Constantes.COR_AZUL_1, PorterDuff.Mode.SRC_ATOP);
		            /*
		             *atualizando dados do pedido após sincronismo com o servidor 
		             */
		            pedido.setSincronizado(new Short("1"));
					pedidoDAO.abrir();
					long retorno = pedidoDAO.atualizar(pedido);
					pedidoDAO.fechar();
		            Util.dialogo(PedidoActivity.this,getString(R.string.mensagem_sincronismo_conclusao));		            
					break;
				case Constantes.FALHA:
					Util.dialogo(PedidoActivity.this, msg.getData().getString("mensagem"));
					break;	
				default:break;
			}
			progressDialog.dismiss();
		}
	};
	
	public void run() {
		
		SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
		
		String email = preferencias.getString("email", null);
		String senha = preferencias.getString("senha", null);
		
		Double latitude = Double.parseDouble(preferencias.getString("latitude",null)), longitude = Double.parseDouble(preferencias.getString("longitude",null));
		
		url = Constantes.SERVIDOR + Constantes.RESTFUL_PEDIDO;
		parametros = new ArrayList <NameValuePair>();
		
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
		
		itemDAO.abrir();
		List<Item> itens = itemDAO.listar(pedido);
		itemDAO.fechar();
		JSONObject pedidoJSON = new JSONObject();
		JSONArray itms = new JSONArray();
		try {
			pedidoJSON.put("numero",pedido.getNumero());
			pedidoJSON.put("data",pedido.getData().getTime());
			pedidoJSON.put("sincronizado",pedido.getCliente().isSincronizado());
			pedidoJSON.put("idCliente",pedido.getCliente().getId());
			if(!pedido.getCliente().isSincronizado()){
				pedidoJSON.put("nome",pedido.getCliente().getNome());
				pedidoJSON.put("cnpj",pedido.getCliente().getCnpj());
				pedidoJSON.put("endereco",pedido.getCliente().getEndereco());
				pedidoJSON.put("num",pedido.getCliente().getNumero());
				pedidoJSON.put("cep",pedido.getCliente().getCep());
				pedidoJSON.put("complemento",pedido.getCliente().getComplemento());
				pedidoJSON.put("bairro",pedido.getCliente().getBairro());
				pedidoJSON.put("cidade",pedido.getCliente().getCidade());
			}
			pedidoJSON.put("latitude",latitude);
			pedidoJSON.put("longitude",longitude);
			pedidoJSON.put("obs",pedido.getObs());
			for(Item itm :itens){
				JSONObject itemJSON = new JSONObject();
				itemJSON.put("idProduto", itm.getProduto().getId());
				itemJSON.put("quantidade", itm.getQuantidade());
				itemJSON.put("valor", itm.getValor());
				itemJSON.put("obs", itm.getObs());
				itms.put(itemJSON);
			}
			pedidoJSON.put("itens", itms);
		}catch(JSONException e){
			Util.dialogo(PedidoActivity.this, e.getMessage());
		}
		
		parametros.add(new BasicNameValuePair("json",pedidoJSON.toString()));
		
		Message message = new Message();
		Bundle bundle = new Bundle();
		
		try{
			JSONObject json = HttpCliente.SendHttpPost(url,parametros,PedidoActivity.this);
			message.what = Constantes.SUCESSO;
		}catch(Exception e){
			message.what = Constantes.FALHA;
			bundle.putString("mensagem",e.getMessage());
		}
		
		message.setData(bundle);
		handler.sendMessage(message);
		
	}
	
	@Override
    protected void onResume() {
		Intent intent = getIntent();		
		setContentView(R.layout.pedido_activity);
		
		if(itemDAO==null){
			itemDAO = new ItemDAO(this);
		}
		itemDAO.abrir();
		
		if(pedidoDAO==null){
			pedidoDAO = new PedidoDAO(this);
		}
		pedidoDAO.abrir();
		
		if(clienteDAO==null){
			clienteDAO = new ClienteDAO(this);
		}
		clienteDAO.abrir();
		
		Cursor cursor = null;
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        cursor = pedidoDAO.pesquisar(query);
	    }else{
	    	cursor = pedidoDAO.listar();
	    }
		setListAdapter(new PedidoAdapter(this,cursor));
		registerForContextMenu(getListView());
		
		super.onResume();
	}

	@Override
	protected void onPause(){
		itemDAO.fechar();
		pedidoDAO.fechar();
		clienteDAO.fechar();
		super.onPause();
	}
	
	private class PedidoAdapter extends CursorAdapter {

		public PedidoAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final TextView numero = (TextView) view.findViewById(R.id.numero);
			final TextView data = (TextView) view.findViewById(R.id.data);
            final TextView cliente = (TextView) view.findViewById(R.id.cliente);
            numero.setText(cursor.getString(1));
            data.setText(Util.millisegundosDate(cursor.getLong(2)));
            try{
            	Cliente objeto = clienteDAO.pesquisar(cursor.getLong(4));
            	cliente.setText(objeto!=null?objeto.getNome():"");
            }catch(Exception e){
            	Util.dialogo(PedidoActivity.this, e.getMessage());
            }
            final View iconView = view.findViewById(android.R.id.icon1);
            LayerDrawable iconDrawable = (LayerDrawable) iconView.getBackground();
            iconDrawable.getDrawable(0).setColorFilter(cursor.getLong(5)==0?Constantes.COR_VERMELHO_1:Constantes.COR_AZUL_1, PorterDuff.Mode.SRC_ATOP);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.pedido_activity_lista, parent, false);
		}
		
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pedido_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.item_menu_sincronizar:
				this.idPedido = info.id;
				pedido = pedidoDAO.pesquisar(idPedido);
				pedido.setCliente(clienteDAO.pesquisar(pedido.getCliente().getId()));
				if(!pedido.getSincronizado()){
					progressDialog = ProgressDialog.show(this, "", getString(R.string.mensagem_1) , true);
					Thread thread = new Thread(this);
					thread.start();
				}else{
					Util.dialogo(PedidoActivity.this,getString(R.string.mensagem_pedido_sincronizado));
				}
				return true;
			case R.id.item_menu_visualizar:
				Intent intent = new Intent(this, PedidoItemEditarActivity.class);
		    	intent.putExtra(PedidoDAO.PEDIDO_CHAVE_ID, info.id);
		    	startActivity(intent);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	public void onListItemClick(ListView listView, View view, int position, long itemId){
		super.onListItemClick(listView, view, position, itemId);
		Intent intent = new Intent(this, PedidoItemEditarActivity.class);
    	intent.putExtra(PedidoDAO.PEDIDO_CHAVE_ID, itemId);
    	startActivity(intent);
	}
	
}
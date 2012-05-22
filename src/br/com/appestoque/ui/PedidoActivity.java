package br.com.appestoque.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
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
	
	protected static final int SUCESSO = 0;
	
	private Pedido pedido;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case SUCESSO:
					final View iconView = findViewById(android.R.id.icon1);
		            LayerDrawable iconDrawable = (LayerDrawable) iconView.getBackground();
		            iconDrawable.getDrawable(0).setColorFilter(Constantes.COR_AZUL_1, PorterDuff.Mode.SRC_ATOP);
					break;
				default:break;
			}
			progressDialog.dismiss();
		}
	};
	
	public void run() {
		
		SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
		this.uuid = preferencias.getString("UUID", UUID.randomUUID().toString());
		url = Constantes.SERVIDOR + Constantes.RESTFUL_PEDIDO;
		parametros = new ArrayList <NameValuePair>();
		parametros.add(new BasicNameValuePair("uuid",uuid));
		
		List<Item> itens = itemDAO.listar(pedido);
		JSONObject pedidoJSON = new JSONObject();
		JSONArray itms = new JSONArray();
		try {
			pedidoJSON.put("numero",pedido.getNumero());
			pedidoJSON.put("data",pedido.getData().getTime());
			pedidoJSON.put("idCliente",pedido.getCliente().getId());
			pedidoJSON.put("obs",pedido.getObs());
			for(Item itm :itens){
				JSONObject itemJSON = new JSONObject();
				itemJSON.put("idProduto", itm.getProduto().getId());
				itemJSON.put("quantidade", itm.getQuantidade());
				itemJSON.put("valor", itm.getValor());
				itms.put(itemJSON);
			}
			pedidoJSON.put("itens", itms);
		}catch(JSONException e){
			Util.dialogo(PedidoActivity.this, e.getMessage());
		}
		
		parametros.add(new BasicNameValuePair("json",pedidoJSON.toString()));
		
		JSONObject json = HttpCliente.SendHttpPost(url,parametros,PedidoActivity.this);
		
		if(json!=null&&!json.isNull("id")){
			pedido.setSincronizado(new Short("1"));
			long retorno = pedidoDAO.atualizar(pedido);
			handler.sendEmptyMessage(SUCESSO);
			//Util.dialogo(PedidoActivity.this,getString(R.string.mensagem_9));
		}else if(json!=null&&!json.isNull("erro")){
			handler.sendEmptyMessage(0);
//			try {
//				Util.dialogo(PedidoActivity.this,json.getString("erro"));
//			} catch (JSONException e) {
//				Util.dialogo(PedidoActivity.this,e.getMessage());
//			}
		}else{
			handler.sendEmptyMessage(0);
			//Util.dialogo(PedidoActivity.this,getString(R.string.mensagem_10));
		}
		
		
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		
		setContentView(R.layout.pedido_activity);
		
		if(itemDAO==null){
			itemDAO = new ItemDAO(this);
		}
		
		if(pedidoDAO==null){
			pedidoDAO = new PedidoDAO(this);
		}
		
		if(clienteDAO==null){
			clienteDAO = new ClienteDAO(this);
		}
		
		Cursor cursor = null;
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        cursor = pedidoDAO.pesquisar(query);
	    }else{
	    	cursor = pedidoDAO.listar();
	    }
	    startManagingCursor(cursor);	    
		setListAdapter(new PedidoAdapter(this,cursor));
		registerForContextMenu(getListView());
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
				if(!pedido.getSincronizado()){
					progressDialog = ProgressDialog.show(this, "", getString(R.string.mensagem_1) , true);
					Thread thread = new Thread(this);
					thread.start();
				}else{
					Util.dialogo(PedidoActivity.this,getString(R.string.mensagem_pedido_sincronizao));
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
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		pedidoDAO.fechar();
		itemDAO.fechar();
		clienteDAO.fechar();
	}
	
}
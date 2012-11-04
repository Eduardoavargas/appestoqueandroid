package br.com.appestoque.ui;

import br.com.appestoque.Constantes;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import br.com.appestoque.dominio.faturamento.Item;
import br.com.appestoque.dominio.faturamento.Pedido;
import br.com.appestoque.dominio.suprimento.Produto;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ItemActivity extends BaseListaAtividade{
	
	private ItemDAO itemDAO;
	private ProdutoDAO produtoDAO;
	
	private class ItensAdapter extends CursorAdapter {

		public ItensAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final TextView produto = (TextView) view.findViewById(R.id.produto);
            final TextView quantidade = (TextView) view.findViewById(R.id.quantidade);
            final TextView valor = (TextView) view.findViewById(R.id.valor);
            final TextView valorTotal = (TextView) view.findViewById(R.id.valorTotal);
            final TextView numero = (TextView) view.findViewById(R.id.numero);
            final TextView obs = (TextView) view.findViewById(R.id.obs);
            quantidade.setText(Util.doubleToString(cursor.getDouble(Item.ITEM_SEQUENCIA_QUANTIDADE),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            valor.setText(Util.doubleToString(cursor.getDouble(Item.ITEM_SEQUENCIA_VALOR),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            valorTotal.setText(Util.doubleToString(cursor.getDouble(Item.ITEM_SEQUENCIA_QUANTIDADE)*cursor.getDouble(Item.ITEM_SEQUENCIA_VALOR),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            numero.setText(cursor.getString(Item.ITEM_SEQUENCIA_NUMERO));
            obs.setText(cursor.getString(Item.ITEM_SEQUENCIA_OBS));
            Produto prd = produtoDAO.pesquisar(cursor.getLong(Item.ITEM_SEQUENCIA_PRODUTO));
            produto.setText(prd.getNome());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.item_activity_lista, parent, false);
		}
		
	}
	
	@Override
    protected void onResume() {
		setContentView(R.layout.item_activity);
		itemDAO = new ItemDAO(this);
		itemDAO.abrir();
		produtoDAO = new ProdutoDAO(this);
		produtoDAO.abrir();
		Cursor cursor = null;
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
	        cursor = itemDAO.listar(extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
	        PedidoDAO pedidoDAO = new PedidoDAO(this);
	        pedidoDAO.abrir();
	        Pedido pedido = pedidoDAO.pesquisar(extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
	        pedidoDAO.fechar();
	        ((Button) findViewById(R.id.btn_adicionar)).setVisibility(pedido.isSincronizado()?View.GONE:View.VISIBLE);
	    }
		setListAdapter(new ItensAdapter(this,cursor));
		registerForContextMenu(getListView());
		super.onResume();
	}

	@Override
	protected void onPause(){
		itemDAO.fechar();
		produtoDAO.fechar();
		super.onPause();
	}
	
	public void onAdicionarClick(View v) {
		Intent intent = new Intent(this, ItemEditarActivity.class);
		intent.putExtra(ItemDAO.ITEM_CHAVE_PEDIDO,getIntent().getExtras().getLong(PedidoDAO.PEDIDO_CHAVE_ID));
    	startActivity(intent);
    }
	
	public void onListItemClick(ListView listView, View view, int position, long itemId){
		super.onListItemClick(listView, view, position, itemId);
		PedidoDAO pedidoDAO = new PedidoDAO(this);
		pedidoDAO.abrir();
		Pedido pedido = pedidoDAO.pesquisar(getIntent().getExtras().getLong(PedidoDAO.PEDIDO_CHAVE_ID));
		pedidoDAO.fechar();
		if(!pedido.isSincronizado()){
			Intent intent = new Intent(this, ItemEditarActivity.class);
			intent.putExtra(ItemDAO.ITEM_CHAVE_PEDIDO,getIntent().getExtras().getLong(PedidoDAO.PEDIDO_CHAVE_ID));
	    	intent.putExtra(ItemDAO.ITEM_CHAVE_ID, itemId);
	    	startActivity(intent);
		}else{
			Util.dialogo(this,getString(R.string.mensagem_pedido_sincronizado));
		}
	}
    
}
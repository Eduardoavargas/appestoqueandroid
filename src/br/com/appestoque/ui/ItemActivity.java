package br.com.appestoque.ui;

import br.com.appestoque.Constantes;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import br.com.appestoque.dominio.faturamento.Item;
import br.com.appestoque.dominio.suprimento.Produto;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
            quantidade.setText(Util.doubleToString(cursor.getDouble(Item.ITEM_SEQUENCIA_QUANTIDADE),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            valor.setText(Util.doubleToString(cursor.getDouble(Item.ITEM_SEQUENCIA_VALOR),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            valorTotal.setText(Util.doubleToString(cursor.getDouble(Item.ITEM_SEQUENCIA_QUANTIDADE)*cursor.getDouble(Item.ITEM_SEQUENCIA_VALOR),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            numero.setText(cursor.getString(Item.ITEM_SEQUENCIA_NUMERO));
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
		intent.putExtras(getIntent().getExtras());
    	startActivity(intent);
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.item_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_menu_editar:
				Toast.makeText(getApplicationContext(), "Editar Item", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.item_menu_remover:
				Toast.makeText(getApplicationContext(), "Remover Item", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

    
}
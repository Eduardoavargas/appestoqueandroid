package br.com.appestoque.ui;

import br.com.appestoque.Constantes;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import br.com.appestoque.dominio.suprimento.Produto;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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
            quantidade.setText(Util.doubleToString(cursor.getDouble(1),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            valor.setText(Util.doubleToString(cursor.getDouble(2),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            valorTotal.setText(Util.doubleToString(cursor.getDouble(1)*cursor.getDouble(2),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
            Produto prd = produtoDAO.pesquisar(cursor.getLong(3));
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
		produtoDAO = new ProdutoDAO(this);
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
		produtoDAO.fechar();
		super.onPause();
	}
	
	public void onAdicionarClick(View v) {
		Intent intent = new Intent(this, ItemEditarActivity.class);
		intent.putExtras(getIntent().getExtras());
    	startActivity(intent);
    }
    
}
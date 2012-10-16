package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dominio.faturamento.Pedido;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class PedidoItemEditarActivity extends BaseTabAtividade {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pedido_item_editar_activity);
		
		Bundle extras = getIntent().getExtras();
		
	    TabHost tabHost = getTabHost(); 
	    TabHost.TabSpec spec;
	    Intent intent;
	    
	    View view1 = createTabView(tabHost.getContext(), "Pedido");
	    
	    intent = new Intent().setClass(this,PedidoEditarActivity.class);
	    intent.putExtra(PedidoDAO.PEDIDO_CHAVE_ID, extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
	    spec = tabHost.newTabSpec(getString(R.string.titulo_pedido))
	    		.setContent(intent)
	    		.setIndicator(view1);
	    tabHost.addTab(spec);
	    
	    View view2 = createTabView(tabHost.getContext(), "Item");

	    intent = new Intent().setClass(this, ItemActivity.class);
	    intent.putExtra(PedidoDAO.PEDIDO_CHAVE_ID, extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
	    spec = tabHost.newTabSpec(getString(R.string.titulo_item))
	    		.setContent(intent)
	    		.setIndicator(view2);
	    tabHost.addTab(spec);

	}
	
    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }
    
	public void onRemoverClick(View view) {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setMessage(R.string.msg_pedido_confirmar_exclusao);
		alerta.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   Bundle extras = getIntent().getExtras();
	        	   if(extras.containsKey(PedidoDAO.PEDIDO_CHAVE_ID)){
	        		   PedidoDAO pedidoDAO = new PedidoDAO(getApplication());
	        		   pedidoDAO.abrir();
	        		   Pedido pedido = pedidoDAO.pesquisar(extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
	        		   if(pedido!=null&&!pedido.isSincronizado()){
	        			   ItemDAO itemDAO = new ItemDAO(getApplication());
	        			   itemDAO.abrir();
	        			   itemDAO.remover(pedido);
	        			   itemDAO.fechar();
	        			   Util.dialogo(getApplication(), getString(R.string.msg_item_removido));
	        			   pedidoDAO.remover(pedido);
	        			   Util.dialogo(getApplication(), getString(R.string.msg_pedido_removido));
	        			   finish();
	        		   }else if(pedido==null){
	        			   Util.dialogo(getApplication(), getString(R.string.msg_pedido_nao_encontrado));
	        		   }else if(pedido.isSincronizado()){
	        			   Util.dialogo(getApplication(), getString(R.string.mensagem_pedido_sincronizado));
	        		   }
	        		   pedidoDAO.fechar();
	        	   }
	        	   
	           }
	    });
		alerta.setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	    });
		alerta.show();
	}
	
}
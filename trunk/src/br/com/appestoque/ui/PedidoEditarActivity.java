package br.com.appestoque.ui;

import br.com.appestoque.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class PedidoEditarActivity extends TabActivity {

//	private ClienteDAO clienteDAO;
//	private PedidoDAO pedidoDAO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Resources res = getResources(); 
	    TabHost tabHost = getTabHost(); 
	    TabHost.TabSpec spec;
	    Intent intent;

	    intent = new Intent().setClass(this,ProdutoActivity.class);
	    spec = tabHost.newTabSpec(getString(R.string.titulo_pedido))
	    		.setIndicator("Pedido",res.getDrawable(R.drawable.ic_pedido))
                .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, ClienteActivity.class);
	    spec = tabHost.newTabSpec(getString(R.string.titulo_item))
	    		.setIndicator("Item",res.getDrawable(R.drawable.ic_item))
	    		.setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(2);
	    
	}

	public void onSalvarClick(View view) {
//		final EditText id = (EditText) findViewById(R.id.edtId);
//		final EditText numero = (EditText) findViewById(R.id.edtNumero);
//		final DatePicker data = (DatePicker) findViewById(R.id.dtpData);
//		final EditText obs = (EditText) findViewById(R.id.edtObs);
//		pedidoDAO = new PedidoDAO(this);
//		pedidoDAO.criar(numero.getText().toString(), Util.dateMillisegundos(data.getYear(),data.getMonth(),data.getDayOfMonth()), obs.getText().toString(), new Long(id.getText().toString()) );
//		setResult(RESULT_OK);
//		this.finish();
	}
	
	public void onCancelarClick(View v) {
//		setResult(RESULT_CANCELED);
//		this.finish();
	}
	
}
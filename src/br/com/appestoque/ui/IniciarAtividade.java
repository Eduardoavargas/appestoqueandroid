package br.com.appestoque.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import br.com.appestoque.R;

public class IniciarAtividade extends BaseAtividade {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iniciar_atividade);
	}
	
	public void onClienteClick(View v) {
		startActivity(new Intent(this, ClienteActivity.class));
	}
	
	public void onProdutoClick(View v) {
		startActivity(new Intent(this, ProdutoActivity.class));
	}
	
	public void onPedidoClick(View v) {
		startActivity(new Intent(this, PedidoActivity.class));
	}
	
	public void onPreferenciaClick(View v) {
		startActivity(new Intent(this, PreferenciaActivity.class));
	}
	
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    }
	
}
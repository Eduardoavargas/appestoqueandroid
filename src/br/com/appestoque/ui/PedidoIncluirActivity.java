package br.com.appestoque.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dominio.cadastro.Cliente;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class PedidoIncluirActivity extends BaseAtividade {

	private ClienteDAO clienteDAO;
	private PedidoDAO pedidoDAO;
	
	@Override
	public void onResume() {
		setContentView(R.layout.pedido_incluir_activity);
		Bundle extras = getIntent().getExtras();
		if(pedidoDAO==null){
			pedidoDAO = new PedidoDAO(this);
		}
		pedidoDAO.abrir();
		
		if(clienteDAO==null){
			clienteDAO = new ClienteDAO(this);
		}
		clienteDAO.abrir();
		
		if(extras!=null){
			Cliente cliente = clienteDAO.pesquisar(extras.getLong(ClienteDAO.CLIENTE_CHAVE_ID));
			((TextView) findViewById(R.id.edtCliente)).setText(cliente.getNome());
			((TextView) findViewById(R.id.edtId)).setText(cliente.getId().toString());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'P'yyyyMMddhhmmss");
			((TextView) findViewById(R.id.edtNumero)).setText(simpleDateFormat.format(new Date()));
		}
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		pedidoDAO.fechar();
		clienteDAO.fechar();
		super.onPause();
	}
	
	public void onSalvarClick(View view) {
		final EditText id = (EditText) findViewById(R.id.edtId);
		final EditText numero = (EditText) findViewById(R.id.edtNumero);
		final DatePicker data = (DatePicker) findViewById(R.id.dtpData);
		final EditText obs = (EditText) findViewById(R.id.edtObs);
		
		long chave = pedidoDAO.criar(numero.getText().toString(), Util.dateMillisegundos(data.getYear(),data.getMonth(),data.getDayOfMonth()), obs.getText().toString(), Long.valueOf(id.getText().toString()) );
		setResult(RESULT_OK);
		Intent intent = new Intent(this, PedidoItemEditarActivity.class);
		intent.putExtra(PedidoDAO.PEDIDO_CHAVE_ID, chave);
    	startActivity(intent);
		this.finish();
	}
	
	public void onCancelarClick(View v) {
		setResult(RESULT_CANCELED);
		this.finish();
	}
	
}
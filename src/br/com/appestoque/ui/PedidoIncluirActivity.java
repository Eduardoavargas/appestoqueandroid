package br.com.appestoque.ui;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedido_incluir_activity);
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
	    	clienteDAO = new ClienteDAO(this);
			Cliente cliente = clienteDAO.pesquisar(extras.getLong(ClienteDAO.CLIENTE_CHAVE_ID));
			((TextView) findViewById(R.id.edtCliente)).setText(cliente.getNome());
			((TextView) findViewById(R.id.edtId)).setText(cliente.getId().toString());
		}
	}
	
	public void onSalvarClick(View view) {
		final EditText id = (EditText) findViewById(R.id.edtId);
		final EditText numero = (EditText) findViewById(R.id.edtNumero);
		final DatePicker data = (DatePicker) findViewById(R.id.dtpData);
		final EditText obs = (EditText) findViewById(R.id.edtObs);
		pedidoDAO = new PedidoDAO(this);
		long chave = pedidoDAO.criar(numero.getText().toString(), Util.dateMillisegundos(data.getYear(),data.getMonth(),data.getDayOfMonth()), obs.getText().toString(), new Long(id.getText().toString()) );
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
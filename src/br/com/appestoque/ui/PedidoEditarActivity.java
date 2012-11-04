package br.com.appestoque.ui;

import java.util.Calendar;

import br.com.appestoque.Constantes;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dominio.cadastro.Cliente;
import br.com.appestoque.dominio.faturamento.Pedido;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

public class PedidoEditarActivity extends BaseAtividade{

	private PedidoDAO pedidoDAO;
	private ItemDAO itemDAO;
	private ClienteDAO clienteDAO;

	@Override
	public void onResume() {
		setContentView(R.layout.pedido_editar_activity);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (pedidoDAO == null){
				pedidoDAO = new PedidoDAO(this);
			}	
			pedidoDAO.abrir();
			
			if (itemDAO == null){
				itemDAO = new ItemDAO(this);
			}	
			itemDAO.abrir();
			
			Pedido pedido = pedidoDAO.pesquisar(extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
			if(clienteDAO==null){
				clienteDAO = new ClienteDAO(this);
			}
			clienteDAO.abrir();
			Cliente cliente = clienteDAO.pesquisar(pedido.getCliente().getId());
			pedido.setCliente(cliente);
			
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(pedido.getData());
			((DatePicker)findViewById(R.id.dtpData)).init(	calendario.get(Calendar.YEAR), 
															calendario.get(Calendar.MONTH), 
															calendario.get(Calendar.DAY_OF_MONTH), null);
			((TextView) findViewById(R.id.edtNumero)).setText(pedido.getNumero().toString());
			((TextView) findViewById(R.id.edtCliente)).setText(pedido.getCliente().getNome());
			((TextView) findViewById(R.id.edtObs)).setText(pedido.getObs());
			((TextView) findViewById(R.id.edtTotal)).setText(Util.doubleToString(
					pedido.getTotal(),Constantes.MASCARA_VALOR_DUAS_CASAS_DECIMAIS));
			((LinearLayout) findViewById(R.id.linearLayoutPedido)).setVisibility(pedido.isSincronizado()?View.GONE:View.VISIBLE);
			((DatePicker)findViewById(R.id.dtpData)).setEnabled(!pedido.isSincronizado());
			((TextView) findViewById(R.id.edtObs)).setEnabled(!pedido.isSincronizado());
			((TextView) findViewById(R.id.edtNumero)).setEnabled(!pedido.isSincronizado());
		}
		super.onResume();
	}
	
	public void onSalvarClick(View view) {
		Bundle extras = getIntent().getExtras();
		Pedido pedido = pedidoDAO.pesquisar(extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
		if(pedido!=null&&!pedido.isSincronizado()){
			final EditText numero = (EditText) findViewById(R.id.edtNumero);
			final DatePicker data = (DatePicker) findViewById(R.id.dtpData);
			final EditText obs = (EditText) findViewById(R.id.edtObs);
			pedido.setNumero(numero.getText().toString());
			Calendar calendario = Calendar.getInstance();
			calendario.set(data.getYear(), data.getMonth(), data.getDayOfMonth());
			pedido.setData(calendario.getTime());
			pedido.setObs(obs.getText().toString());
			pedidoDAO.atualizar(pedido);
			this.finish();
		}else if(pedido==null){
			Util.dialogo(PedidoEditarActivity.this,getString(R.string.msg_pedido_nao_encontrado));
		}else if(pedido.isSincronizado()){
			Util.dialogo(PedidoEditarActivity.this,getString(R.string.mensagem_pedido_sincronizado));
		}
	}
	
	@Override
	protected void onPause(){
		pedidoDAO.fechar();
		itemDAO.fechar();
		clienteDAO.fechar();
		super.onPause();
	}

}
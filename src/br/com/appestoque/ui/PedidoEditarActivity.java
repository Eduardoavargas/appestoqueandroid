package br.com.appestoque.ui;

import br.com.appestoque.Constantes;
import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dominio.cadastro.Cliente;
import br.com.appestoque.dominio.faturamento.Pedido;
import android.os.Bundle;
import android.widget.TextView;

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
			((TextView) findViewById(R.id.edtNumero)).setText(pedido.getNumero().toString());
			((TextView) findViewById(R.id.edtData)).setText(Util.dateToStr(Constantes.MASCARA_DATA_DDMMYYYY,pedido.getData()));
			((TextView) findViewById(R.id.edtCliente)).setText(pedido.getCliente().getNome());
			((TextView) findViewById(R.id.edtObs)).setText(pedido.getObs());
			((TextView) findViewById(R.id.edtTotal)).setText(Util.doubleToString(
					pedido.getTotal(),Constantes.MASCARA_VALOR_DUAS_CASAS_DECIMAIS));
		}
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		pedidoDAO.fechar();
		itemDAO.fechar();
		clienteDAO.fechar();
		super.onPause();
	}

}
package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.dominio.cadastro.Cliente;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import android.os.Bundle;
import android.widget.TextView;

public class ClienteEditarActivity extends BaseAtividade {

	private ClienteDAO clienteDAO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente_editar_activity);
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			if(clienteDAO==null){
				clienteDAO = new ClienteDAO(this);
			}
			Cliente cliente = clienteDAO.pesquisar(extras.getLong(ClienteDAO.CLIENTE_CHAVE_ID));
			((TextView) findViewById(R.id.edtCnpj)).setText(cliente.getCnpj());
			((TextView) findViewById(R.id.edtNome)).setText(cliente.getNome());
			((TextView) findViewById(R.id.edtEndereco)).setText(cliente.getEndereco());
			((TextView) findViewById(R.id.edtNumero)).setText(cliente.getNumero().toString());
			((TextView) findViewById(R.id.edtCep)).setText(cliente.getCep());
			((TextView) findViewById(R.id.edtComplemento)).setText(cliente.getComplemento());
			((TextView) findViewById(R.id.edtBairro)).setText(cliente.getBairro());
			((TextView) findViewById(R.id.edtCidade)).setText(cliente.getCidade());
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		clienteDAO.fechar();
	}
	
}
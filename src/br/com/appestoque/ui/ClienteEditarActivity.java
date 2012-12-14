package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.dominio.cadastro.Cliente;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;

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
			clienteDAO.abrir();
			Cliente cliente = clienteDAO.pesquisar(extras.getLong(ClienteDAO.CLIENTE_CHAVE_ID));
			
			//((TextView) findViewById(R.id.edtCnpj)).setText(cliente.getCnpj());
			//final EditText cnpj = (EditText) findViewById(R.id.edtCnpj);
			//cnpj.setText(cliente.getCnpj());
			//((EditView)findViewById(R.id.edtCnpj));
			//((EditView) findViewById(R.id.edtCnpj)).setText(cliente.getCnpj());
			
			((EditText) findViewById(R.id.edtCnpj)).setText(cliente.getCnpj());
			((EditText) findViewById(R.id.edtNome)).setText(cliente.getNome());
			((EditText) findViewById(R.id.edtEndereco)).setText(cliente.getEndereco());
			((EditText) findViewById(R.id.edtNumero)).setText(cliente.getNumero().toString());
			((EditText) findViewById(R.id.edtCep)).setText(cliente.getCep());
			((EditText) findViewById(R.id.edtComplemento)).setText(cliente.getComplemento());
			((EditText) findViewById(R.id.edtBairro)).setText(cliente.getBairro());
			((EditText) findViewById(R.id.edtCidade)).setText(cliente.getCidade());
			
			//((TextView) findViewById(R.id.edtEndereco)).setText(cliente.getEndereco());
			//((TextView) findViewById(R.id.edtNumero)).setText(cliente.getNumero().toString());
			//((TextView) findViewById(R.id.edtCep)).setText(cliente.getCep());
			//((TextView) findViewById(R.id.edtComplemento)).setText(cliente.getComplemento());
			//((TextView) findViewById(R.id.edtBairro)).setText(cliente.getBairro());
			//((TextView) findViewById(R.id.edtCidade)).setText(cliente.getCidade());
		}
	}
	
	@Override
	protected void onPause(){
		clienteDAO.fechar();
		super.onPause();
	}
	
}
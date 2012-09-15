package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import br.com.appestoque.format.CNPJFormatter;
import br.com.appestoque.format.CepFormatter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class ClienteAdicionarActivity extends BaseAtividade {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente_adicionar_activity);
		ClienteDAO clienteDAO = null;
		try{
			clienteDAO = new ClienteDAO(this);
	        clienteDAO.abrir();
			AutoCompleteTextView txtBairro = (AutoCompleteTextView) findViewById(R.id.edtBairro);
			AutoCompleteTextView txtCidade = (AutoCompleteTextView) findViewById(R.id.edtCidade);
		    txtBairro.setAdapter(new ArrayAdapter<String>(this, R.layout.produto_listar, clienteDAO.listarBairros() ));
		    txtCidade.setAdapter(new ArrayAdapter<String>(this, R.layout.produto_listar, clienteDAO.listarCidades() ));
		}finally{
			clienteDAO.fechar();
		}
	}	
	
	static long idCliente;
	static Context context;
	
	public void onSalvarClick(View view) {
		ClienteDAO clienteDAO = null;
		try{
		
	        clienteDAO = new ClienteDAO(this);
	        clienteDAO.abrir();
	        
	        final EditText edtNome = (EditText) findViewById(R.id.edtNome);
	        if(edtNome.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_nome));}
	        
	        // TODO validar cnpj do cliente
	        final EditText edtCnpj = (EditText) findViewById(R.id.edtCnpj);
	        if(edtCnpj.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_cnpj));}
	        
	        final EditText edtEnd  = (EditText) findViewById(R.id.edtEnd);
	        if(edtEnd.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_endereco));}
	        
	        final EditText edtNum  = (EditText) findViewById(R.id.edtNum);
	        if(edtNum.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_numero));}
	        
	        // TODO validar cep do cliente
	        final EditText edtCep  = (EditText) findViewById(R.id.edtCep);
	        if(edtCep.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_cep));}
	        
	        final EditText edtCompl  = (EditText) findViewById(R.id.edtCompl);
	        if(edtCompl.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_complemento));}
	        
	        final EditText edtBairro = (EditText) findViewById(R.id.edtBairro);
	        if(edtBairro.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_bairro));}
	        
	        // TODO validar cidade do cliente
	        final EditText edtCidade = (EditText) findViewById(R.id.edtCidade);
	        if(edtCidade.getText().toString().equals("")){throw new Exception(getString(R.string.msg_validar_cidade));}
	        
	        idCliente = clienteDAO.contar();
	        
//	        String cnpj = new CNPJFormatter().format(edtCnpj.getText().toString());
//	        String cep = new CepFormatter().format(edtCep.getText().toString());
	        
	        clienteDAO.criar(++idCliente, 
	        		edtNome.getText().toString(), 
	        		new CNPJFormatter().format(edtCnpj.getText().toString()), 
	        		edtEnd.getText().toString(),
	        		Long.valueOf(edtNum.getText().toString()),
	        		new CepFormatter().format(edtCep.getText().toString()), 
	        		edtCompl.getText().toString(), 
	        		edtBairro.getText().toString(), 
	        		edtCidade.getText().toString(),
	        		false);
	
	        context = this;
	        
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.msg_criar_pedido_venda))
			       .setCancelable(false)
			       .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   Intent intent = new Intent(context, PedidoIncluirActivity.class);
						   intent.putExtra(ClienteDAO.CLIENTE_CHAVE_ID, idCliente);					       
			        	   finish();
			        	   startActivity(intent);
			           }
			       })
			       .setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                finish();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}catch(Exception e){
			Util.dialogo(this,e.getMessage());
		}finally{
			clienteDAO.fechar();
		}
		
	}

	public void onCancelarClick(View view) {
		finish();
	}

}
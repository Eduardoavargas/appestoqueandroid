package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ClienteAdicionarActivity extends BaseAtividade {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente_adicionar_activity);
	}	
	
	static long idCliente;
	static Context context;
	
	public void onSalvarClick(View view) {
		ClienteDAO clienteDAO = null;
		try{
		
	        clienteDAO = new ClienteDAO(this);
	        clienteDAO.abrir();
	        
	        // TODO validar nome do cliente	         
	        final EditText edtNome = (EditText) findViewById(R.id.edtNome);
	        
	        // TODO validar cnpj do cliente
	        // TODO formatar cnpj do cliente
	        final EditText edtCnpj = (EditText) findViewById(R.id.edtCnpj);
	        
	        // TODO validar endereço do cliente
	        final EditText edtEnd  = (EditText) findViewById(R.id.edtEnd);
	        
	        // TODO validar número do cliente
	        final EditText edtNum  = (EditText) findViewById(R.id.edtNum);
	        
	        // TODO validar cep do cliente
	        // TODO formatar cep do cliente
	        final EditText edtCep  = (EditText) findViewById(R.id.edtCep);
	        
	        // TODO validar complemento do cliente
	        final EditText edtCompl  = (EditText) findViewById(R.id.edtCompl);
	        
	        // TODO validar bairro do cliente
	        final EditText edtBairro  = (EditText) findViewById(R.id.edtBairro);
	        
	        // TODO validar cidade do cliente
	        final EditText edtCidade  = (EditText) findViewById(R.id.edtCidade);
	        
	        idCliente = clienteDAO.contar();
	        
	        clienteDAO.criar(++idCliente, 
	        		edtNome.getText().toString(), 
	        		edtCnpj.getText().toString(), 
	        		edtEnd.getText().toString(),
	        		Long.valueOf(edtNum.getText().toString()),
	        		edtCep.getText().toString(), 
	        		edtCompl.getText().toString(), 
	        		edtBairro.getText().toString(), 
	        		edtCidade.getText().toString());
	
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
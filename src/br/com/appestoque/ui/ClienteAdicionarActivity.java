package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.dao.cadastro.ClienteDAO;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClienteAdicionarActivity extends BaseAtividade {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente_adicionar_activity);
	}	
	
	public void onSalvarClick(View view) {
		
        ClienteDAO clienteDAO = new ClienteDAO(this);
        
        clienteDAO.criar(null, 
        		((TextView) view.findViewById(R.id.edtNome)).getText().toString(), 
        		null, ((TextView) view.findViewById(R.id.edtEnd)).getText().toString(),
        		Long.valueOf(((TextView) view.findViewById(R.id.edtNum)).getText().toString()),
        		((TextView) view.findViewById(R.id.edtCep)).getText().toString(), 
        		((TextView) view.findViewById(R.id.edtCompl)).getText().toString(), 
        		((TextView) view.findViewById(R.id.edtBairro)).getText().toString(), 
        		((TextView) view.findViewById(R.id.edtCidade)).getText().toString());

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.msg_criar_pedido_venda))
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
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
		
	}

	public void onCancelarClick(View view) {
		finish();
	}

}
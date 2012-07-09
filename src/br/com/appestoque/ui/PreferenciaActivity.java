package br.com.appestoque.ui;

import br.com.appestoque.Constantes;
import br.com.appestoque.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PreferenciaActivity extends BaseAtividade{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencia_editar_activity);
		SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
		((EditText) findViewById(R.id.edtEmail)).setText(preferencias.getString("email", null));
		((EditText) findViewById(R.id.edtSenha)).setText(preferencias.getString("senha", null));
	}
	
	public void onSalvarClick(View view) {
		final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
		final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);
		SharedPreferences preferencias = getSharedPreferences(Constantes.PREFERENCIAS, 0);
		SharedPreferences.Editor editor = preferencias.edit();
		editor.putString("email", edtEmail.getText().toString());
		editor.putString("senha", edtSenha.getText().toString());
		editor.commit();
		this.finish();
	}
	
	public void onCancelarClick(View v) {
		this.finish();
	}
	
}
package br.com.appestoque.ui;

import br.com.appestoque.R;
import br.com.appestoque.dominio.suprimento.Produto;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import android.os.Bundle;
import android.widget.TextView;

public class ProdutoEditarActivity extends BaseAtividade {

	private ProdutoDAO produtoDAO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.produto_editar_activity);
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			if(produtoDAO==null){
				produtoDAO = new ProdutoDAO(this);
			}
			produtoDAO.abrir();
			Produto produto = produtoDAO.pesquisar(extras.getLong(ProdutoDAO.PRODUTO_CHAVE_ID));
			((TextView) findViewById(R.id.edtNome)).setText(produto.getNome());
			((TextView) findViewById(R.id.edtNumero)).setText(produto.getNumero());
			((TextView) findViewById(R.id.edtPreco)).setText(produto.getValor().toString());
		}
	}

	@Override
	protected void onPause(){
		produtoDAO.fechar();
		super.onPause();
	}
	
}
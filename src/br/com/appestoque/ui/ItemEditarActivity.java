package br.com.appestoque.ui;

import java.util.List;

import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dominio.faturamento.Item;
import br.com.appestoque.dominio.faturamento.Pedido;
import br.com.appestoque.dominio.suprimento.Produto;
import br.com.appestoque.dao.faturamento.ItemDAO;
import br.com.appestoque.dao.faturamento.PedidoDAO;
import br.com.appestoque.dao.suprimento.ProdutoDAO;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class ItemEditarActivity extends BaseAtividade {

	private ItemDAO itemDAO;
	private ProdutoDAO produtoDAO;
	
	@Override
	public void onResume() {
		setContentView(R.layout.item_editar_activity);		
		if(itemDAO==null){
			itemDAO = new ItemDAO(this);
		}
		itemDAO.abrir();
		
		if(produtoDAO==null){
			produtoDAO = new ProdutoDAO(this);
		}
		produtoDAO.abrir();
		
		List<Produto> lista = produtoDAO.produtos();
		String[] produtos = new String[lista.size()];
		
		for(int i = 0; i<lista.size();++i){
			produtos[i] = lista.get(i).getNumero();
		}
		
		AutoCompleteTextView txtProduto = (AutoCompleteTextView) findViewById(R.id.edtProduto);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.produto_listar, produtos);
	    txtProduto.setAdapter(adapter);
	    super.onResume();
	}
	
	@Override
	protected void onPause(){
		produtoDAO.fechar();
		itemDAO.fechar();
		super.onPause();
	}
	
	public void onSalvarClick(View view) {
		Bundle extras = getIntent().getExtras();
		final AutoCompleteTextView numero = (AutoCompleteTextView) findViewById(R.id.edtProduto);
		final EditText qtd = (EditText) findViewById(R.id.edtQtd);
		final EditText valor = (EditText) findViewById(R.id.edtValor);
		Produto produto = produtoDAO.consultar(numero.getText().toString());
		if(produto!=null){
			
			if(!qtd.getText().toString().equals("")&&valor.getText().toString().equals("")){
				Item item = new Item();
				item.setQuantidade(new Double(qtd.getText().toString()));
				item.setValor(new Double(valor.getText().toString()));
				item.setProduto(produto);
				PedidoDAO pedidoDAO = new PedidoDAO(this);
				pedidoDAO.abrir();
				Pedido pedido = pedidoDAO.pesquisar(extras.getLong(PedidoDAO.PEDIDO_CHAVE_ID));
				pedidoDAO.fechar();
				item.setPedido(pedido);
				itemDAO.adicionar(item);
				finish();
			}else{
				Util.dialogo(this,"Desculpe, mas é necessário informar a quantidade e o valor do item.");
			}
		}else{
			Util.dialogo(this,getString(R.string.mensagem_8));
		}
	}
	
	public void onCancelarClick(View view) {
		finish();    	
	}
	
}
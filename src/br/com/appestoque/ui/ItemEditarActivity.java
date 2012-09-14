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
import br.com.appestoque.Constantes;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
		
		AutoCompleteTextView txtProduto = (AutoCompleteTextView) findViewById(R.id.edtProduto);
		
		List<Produto> lista = produtoDAO.produtos();
		String[] produtos = new String[lista.size()];
		for(int i = 0; i<lista.size();++i){
			produtos[i] = lista.get(i).getNumero();
		}
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.produto_listar, produtos );
	    txtProduto.setAdapter(adapter);
		
		Long mRowId = getIntent().getExtras().getLong(ItemDAO.ITEM_CHAVE_ID);
		
		if (mRowId != null) {
			ItemDAO dao = new ItemDAO(this);
			dao.abrir();
			Item item = dao.pesquisar(mRowId);
			if (item != null) {
				txtProduto.setText(item.getProduto().getNumero().toCharArray(),0,item.getProduto().getNumero().length());
				((TextView) findViewById(R.id.edtQtd)).setText(item.getQuantidade().toString());
				((TextView) findViewById(R.id.edtValor)).setText(item.getValor().toString());
				((TextView) findViewById(R.id.edtObs)).setText(item.getObs());
			}else{
				((TextView) findViewById(R.id.edtQtd)).setText(Constantes.VALOR_PADRAO_DUAS_CASAS_DECIMAIS);
				((TextView) findViewById(R.id.edtValor)).setText(Constantes.VALOR_PADRAO_DUAS_CASAS_DECIMAIS);
			}
			dao.fechar();
		}else{
			((TextView) findViewById(R.id.edtQtd)).setText(Constantes.VALOR_PADRAO_DUAS_CASAS_DECIMAIS);
			((TextView) findViewById(R.id.edtValor)).setText(Constantes.VALOR_PADRAO_DUAS_CASAS_DECIMAIS);
		}
		
		((ImageButton) findViewById(R.id.img_remover)).setEnabled(getIntent().getExtras().containsKey(ItemDAO.ITEM_CHAVE_ID));
	    
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
		
		final EditText obs = (EditText) findViewById(R.id.edtObs);
		
		Produto produto = produtoDAO.consultar(numero.getText().toString());
		if(produto!=null){
			if(!qtd.getText().toString().equals("")&&!valor.getText().toString().equals("")){
				if(Double.valueOf(valor.getText().toString())>=produto.getMinimo()){
					Item item = new Item();
					item.setQuantidade(Double.valueOf(qtd.getText().toString()));
					item.setValor(Double.valueOf(valor.getText().toString()));
					item.setObs(obs.getText().toString());
					item.setProduto(produto);
					
					PedidoDAO pedidoDAO = new PedidoDAO(this);
					pedidoDAO.abrir();
					Pedido pedido = pedidoDAO.pesquisar(extras.getLong(ItemDAO.ITEM_CHAVE_PEDIDO));
					pedidoDAO.fechar();
					
					item.setPedido(pedido);
					
					if(!extras.containsKey(ItemDAO.ITEM_CHAVE_ID)){
						if(itemDAO.adicionar(item)==0){
							Util.dialogo(this, getString(R.string.mensagem_atualizar_problema));
						}
					}else{
						item.setId(extras.getLong(ItemDAO.ITEM_CHAVE_ID));
						if(itemDAO.atualizar(item)==0){
							Util.dialogo(this, getString(R.string.mensagem_atualizar_problema));
						}
					}
					finish();
				}else{
					Util.dialogo(this,getString(R.string.msg_validar_produto_valor_minimo));
				}
			}else{
				Util.dialogo(this,"Desculpe, mas � necess�rio informar a quantidade e o valor do item.");
			}
		}else{
			Util.dialogo(this,getString(R.string.mensagem_8));
		}
	}
	
	public void onCancelarClick(View view) {
		finish();    	
	}
	
	public void onRemoverClick(View view) {
		Bundle extras = getIntent().getExtras();
		if(extras.containsKey(ItemDAO.ITEM_CHAVE_ID)&&itemDAO.remover(extras.getLong(ItemDAO.ITEM_CHAVE_ID))){
			Util.dialogo(this, getString(R.string.mensagem_remover_sucesso));
			finish();
		}
	}

	
}
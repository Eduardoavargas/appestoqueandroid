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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ItemEditarActivity extends BaseAtividade {

	private ItemDAO itemDAO;
	private ProdutoDAO produtoDAO;
	private PedidoDAO pedidoDAO;
	
	private Bundle extras = null;
	
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
		
		if(pedidoDAO==null){
			pedidoDAO = new PedidoDAO(this);
		}
		pedidoDAO.abrir();
		
		AutoCompleteTextView acNumeros = (AutoCompleteTextView) findViewById(R.id.edtNumero);
		
		AutoCompleteTextView acNomes = (AutoCompleteTextView) findViewById(R.id.edtNome);
		
		List<Produto> lista = produtoDAO.produtos();
		String[] numeros = new String[lista.size()];
		String[] nomes = new String[lista.size()];
		for(int i = 0; i<lista.size();++i){
			numeros[i] = lista.get(i).getNumero();
			nomes[i] = lista.get(i).getNome();
		}
		
	    ArrayAdapter<String> adapterNumeros = new ArrayAdapter<String>(this,R.layout.produto_listar,numeros);
	    ArrayAdapter<String> adapterNomes = new ArrayAdapter<String>(this,R.layout.produto_listar,nomes);
	    
	    acNumeros.setAdapter(adapterNumeros);
	    acNomes.setAdapter(adapterNomes);
	    
	    acNumeros.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ProdutoDAO dao = new ProdutoDAO(arg0.getContext());
				dao.abrir();
				Produto produto = dao.consultar(arg0.getItemAtPosition(arg2).toString());
				((TextView) findViewById(R.id.edtValor)).setText(Util.doubleToString(produto.getValor(),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
				((TextView) findViewById(R.id.edtQtd)).setText(Util.doubleToString(produto.getQuantidade(),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
				((AutoCompleteTextView) findViewById(R.id.edtNome)).setText(produto.getNome());
				dao.fechar();
			} 
	    });
	    
	    acNomes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ProdutoDAO dao = new ProdutoDAO(arg0.getContext());
				dao.abrir();
				Produto produto = dao.consultarNome(arg0.getItemAtPosition(arg2).toString());
				((TextView) findViewById(R.id.edtValor)).setText(Util.doubleToString(produto.getValor(),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
				((TextView) findViewById(R.id.edtQtd)).setText(Util.doubleToString(produto.getQuantidade(),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
				((AutoCompleteTextView) findViewById(R.id.edtNumero)).setText(produto.getNumero());
				dao.fechar();
			} 
	    });
		
		Long mRowId = getIntent().getExtras().getLong(ItemDAO.ITEM_CHAVE_ID);
		
		if (mRowId != null) {
			ItemDAO dao = new ItemDAO(this);
			dao.abrir();
			Item item = dao.pesquisar(mRowId);
			if (item != null) {
				((AutoCompleteTextView) findViewById(R.id.edtNome)).setText(item.getProduto().getNome());
				((AutoCompleteTextView) findViewById(R.id.edtNumero)).setText(item.getProduto().getNumero());
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
		pedidoDAO.fechar();
		super.onPause();
	}
	
	public void onSalvarClick(View view) {
		Bundle extras = getIntent().getExtras();
		final AutoCompleteTextView numero = (AutoCompleteTextView) findViewById(R.id.edtNumero);
		final EditText qtd = (EditText) findViewById(R.id.edtQtd);
		final EditText valor = (EditText) findViewById(R.id.edtValor);
		
		final EditText obs = (EditText) findViewById(R.id.edtObs);
		
		Produto produto = produtoDAO.consultar(numero.getText().toString());
		if(produto!=null){
			if(!qtd.getText().toString().equals("")&&!valor.getText().toString().equals("")){
				if ((Double.valueOf(valor.getText().toString())==0&&Double.valueOf(qtd.getText().toString())==0)||  
				   (Double.valueOf(valor.getText().toString())>=produto.getMinimo())){
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
							Util.dialogo(this, getString(R.string.mensagem_atualizar_informacao));
						}
					}else{
						item.setId(extras.getLong(ItemDAO.ITEM_CHAVE_ID));
						if(itemDAO.atualizar(item)==0){
							Util.dialogo(this, getString(R.string.mensagem_atualizar_informacao));
						}
					}
					finish();
				}else{					
					Util.dialogo(this,"Este produto não pode ser vendido porque o valor mínimo é " + 
							Util.doubleToString(produto.getMinimo(),Constantes.MASCARA_VALOR_TRES_CASAS_DECIMAIS));
				}
			}else{
				Util.dialogo(this,getString(R.string.msg_obrigatorio_quantidade_valor));
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
		Pedido pedido = pedidoDAO.pesquisar(extras.getLong(ItemDAO.ITEM_CHAVE_ID));
		if(pedido!=null){
			if(!pedido.getSincronizado()&&
					extras.containsKey(ItemDAO.ITEM_CHAVE_ID)&&
					itemDAO.remover(extras.getLong(ItemDAO.ITEM_CHAVE_ID))){
				Util.dialogo(this, getString(R.string.mensagem_remover_sucesso));
				finish();
			}else if(pedido.getSincronizado()){
				Util.dialogo(this, getString(R.string.msg_pedido_sincronizado_remover));
			}else if(extras.containsKey(ItemDAO.ITEM_CHAVE_ID)){
				Util.dialogo(this, getString(R.string.msg_item_chave_nao_localizada));
			}
		}else{
			Util.dialogo(this, getString(R.string.msg_pedido_nao_encontrado));
		}
	}
	
}
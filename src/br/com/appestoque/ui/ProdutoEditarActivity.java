package br.com.appestoque.ui;

import java.io.File;

import br.com.appestoque.R;
import br.com.appestoque.Util;
import br.com.appestoque.dominio.Produto;
import br.com.appestoque.dao.ProdutoDAO;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProdutoEditarActivity extends Activity {

	private ProdutoDAO produtoDAO;
	
	String[] imagens;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.produto_editar_activity);
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
	    	produtoDAO = new ProdutoDAO(this);
			Produto produto = produtoDAO.buscar(extras.getLong(ProdutoDAO.PRODUTO_CHAVE_ID));
			((TextView) findViewById(R.id.edtNome)).setText(produto.getNome());
			((TextView) findViewById(R.id.edtNumero)).setText(produto.getNumero());
			((TextView) findViewById(R.id.edtPreco)).setText(produto.getPreco().toString());
			((TextView) findViewById(R.id.edtEstoque)).setText(produto.getEstoque().toString());
			
			imagens = new String[4];
			imagens[0] = Util.armazenamentoExterno() + produto.getId().toString() + "_1.png";
			imagens[1] = Util.armazenamentoExterno() + produto.getId().toString() + "_2.png";
			imagens[2] = Util.armazenamentoExterno() + produto.getId().toString() + "_3.png";
			imagens[3] = Util.armazenamentoExterno() + produto.getId().toString() + "_4.png";
			
			Gallery gallery = (Gallery) findViewById(R.id.gallery);
		    gallery.setAdapter(new ImageAdapter(this,imagens));
		    
		    gallery.setOnItemClickListener(new OnItemClickListener() {
		    	@Override
		    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		        	ImageView fullView = (ImageView) findViewById(R.id.fullview);
		        	Bitmap bitmap = BitmapFactory.decodeFile(imagens[arg2]);
		        	fullView.setImageBitmap(bitmap);
		    	}
		    });
			
		}
	}
	
    @Override
    protected void onPause(){
    	super.onPause();
    	setResult(RESULT_CANCELED);
    	produtoDAO.fechar();
    	finish();
    }
    
    private class Adaptador extends BaseAdapter{

    	int mGalleryItemBackground;
        private Context context;
        private String[] imagens;
        
        public Adaptador(Context context, String[] imagens) {
        	this.imagens = imagens;
            this.context = context;
            TypedArray attr = context.obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = attr.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground,1);
            attr.recycle();
        }

    	
		@Override
		public int getCount() {
			return imagens.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			String imagem = imagens[position];
			File file = new  File(imagem);
			if(file.exists()){
				Bitmap bitmap = BitmapFactory.decodeFile(imagem);
				imageView.setImageBitmap(bitmap);
			}        
		    imageView.setLayoutParams(new Gallery.LayoutParams(100,100));
		    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		    imageView.setBackgroundResource(mGalleryItemBackground);
	        return imageView;
		}
    	
    }
    

}

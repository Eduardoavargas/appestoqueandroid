package br.com.appestoque;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class Util {

	public static void dialogo(Context context, String msg){		
		Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
	}
	
	public static String serial(Context context){
		return Settings.System.getString(context.getContentResolver(),Settings.System.ANDROID_ID);
	}
	
	public static void notificar(Context context, CharSequence mensagemBarraStatus, CharSequence titulo,CharSequence mensagem, Class<?> activity) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, mensagemBarraStatus, System.currentTimeMillis());
		PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(), 0);
		notification.setLatestEventInfo(context, titulo, mensagem, p);
		notification.vibrate = new long[] { 100, 250, 100, 500 };
		nm.notify(0, notification);
	}

	public static Bitmap downloadImagem(String urlImagem){
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlImagem);
			InputStream inputStream = url.openStream();		
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (MalformedURLException e) {
			e.printStackTrace();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	return bitmap;
	}
	
	public static String armazenamentoExterno(){
    	String raiz = Environment.getExternalStorageDirectory().toString();
    	String caminho = raiz + "/appestoque";
        File dir = new File(caminho);
        if(!dir.exists()){
        	dir.mkdirs();
        }        
        return caminho +"/";
    }
    
	public static boolean arquivoExiste(String arquivo){
        File dir = new File(arquivo);
        return dir.exists();
    }
	
	public static void salvar(Bitmap bitmap, String nome){
        try{
            String caminho = armazenamentoExterno() + nome;
            FileOutputStream stream = new FileOutputStream(caminho);
            bitmap.compress(CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        }catch(Exception e){
            Log.e("N�o foi poss�vel salvar", e.toString());
        }
    }
	
	public static String millisegundosDate( Long millisegundos ){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(new Date(millisegundos));
	}
	
	public static Long dateMillisegundos( int ano, int mes, int dia ){
		Calendar calendario = Calendar.getInstance();
		calendario.set(ano, mes, dia);
		return calendario.getTimeInMillis();
	}
	
	public static String dateToStr( String formato, Date data ){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
		return simpleDateFormat.format(data);
	}
	
	public static String doubleToString( double valor, String mask){
		DecimalFormat decimalFormat = new DecimalFormat(mask);
		return decimalFormat.format(valor);   
	}
	
	public static Double stringToDouble( String valor){
		return Double.valueOf(valor.replace(",", "."));   
	}
	
	public static String validarCNPJ( String cnpj) throws Exception{
		assert cnpj.length()==Constantes.TAMANHO_PADRAO_CNPJ;
		
		return cnpj;   
	}
	
}
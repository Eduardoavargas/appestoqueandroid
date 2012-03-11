package br.com.appestoque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class HttpCliente {

	private static final String TAG = "HttpClient";

	public static JSONObject SendHttpPost(String URL, JSONObject jsonObjSend) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			
			HttpPost httpPostRequest = new HttpPost(URL);
			httpPostRequest.setEntity(new StringEntity(jsonObjSend.toString()));
			
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like to use gzip compression

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				String resultString= convertStreamToString(instream);
				instream.close();
				resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				JSONObject jsonObjRecv = new JSONObject(resultString);

				return jsonObjRecv;
			} 

		}catch (Exception e){
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			e.printStackTrace();
		}
		return null;
	}


	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 * 
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static JSONArray ReceiveHttpPost(String URL, Context context) {
	
		JSONArray objetos = null;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			
			switch(httpResponse.getStatusLine().getStatusCode()){
				case HttpStatus.SC_ACCEPTED:
					Util.dialogo(context,"202 Accepted (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_BAD_GATEWAY:	
					Util.dialogo(context,"502 Bad Gateway (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_BAD_REQUEST:	
					Util.dialogo(context,"400 Bad Request (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_CONFLICT:	
					Util.dialogo(context,"409 Conflict (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_CONTINUE:	
					Util.dialogo(context,"100 Continue (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_CREATED:	
					Util.dialogo(context,"201 Created (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_EXPECTATION_FAILED:	
					Util.dialogo(context,"417 Expectation Failed (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_FAILED_DEPENDENCY:	
					Util.dialogo(context,"424 Failed Dependency (WebDAV - RFC 2518)");
					break;
				case HttpStatus.SC_FORBIDDEN:	
					Util.dialogo(context,"403 Forbidden (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_GATEWAY_TIMEOUT:	
					Util.dialogo(context,"504 Gateway Timeout (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_GONE:	
					Util.dialogo(context,"410 Gone (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:	
					Util.dialogo(context,"505 HTTP Version Not Supported (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE:	
					Util.dialogo(context,"Static constant for a 419 error.");
					break;
				case HttpStatus.SC_INSUFFICIENT_STORAGE:	
					Util.dialogo(context,"507 Insufficient Storage (WebDAV - RFC 2518)");
					break;
				case HttpStatus.SC_INTERNAL_SERVER_ERROR:	
					Util.dialogo(context,"500 Server Error (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_LENGTH_REQUIRED:	
					Util.dialogo(context,"411 Length Required (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_LOCKED:	
					Util.dialogo(context,"423 Locked (WebDAV - RFC 2518)");
					break;					
				case HttpStatus.SC_METHOD_FAILURE:	
					Util.dialogo(context,"Static constant for a 420 error.");
					break;
				case HttpStatus.SC_METHOD_NOT_ALLOWED:	
					Util.dialogo(context,"405 Method Not Allowed (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_MOVED_PERMANENTLY:	
					Util.dialogo(context,"301 Moved Permanently (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_MOVED_TEMPORARILY:	
					Util.dialogo(context,"302 Moved Temporarily (Sometimes Found) (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_MULTIPLE_CHOICES:	
					Util.dialogo(context,"300 Mutliple Choices (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_MULTI_STATUS:	
					Util.dialogo(context,"207 Multi-Status (WebDAV - RFC 2518) or 207 Partial Update OK (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?)");
					break;
				case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:	
					Util.dialogo(context,"203 Non Authoritative Information (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_NOT_ACCEPTABLE:
					Util.dialogo(context,"406 Not Acceptable (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_NOT_FOUND:	
					Util.dialogo(context,"404 Not Found (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_NOT_IMPLEMENTED:	
					Util.dialogo(context,"501 Not Implemented (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_NOT_MODIFIED:	
					Util.dialogo(context,"304 Not Modified (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_NO_CONTENT:	
					Util.dialogo(context,"204 No Content (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_OK:
					//200 OK (HTTP/1.0 - RFC 1945)
					HttpEntity httpEntity = httpResponse.getEntity();
					InputStream inputStream = httpEntity.getContent();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					String data = bufferedReader.readLine();
					objetos = new JSONArray(data);
					break;
				case HttpStatus.SC_PARTIAL_CONTENT:	
					Util.dialogo(context,"206 Partial Content (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_PAYMENT_REQUIRED:	
					Util.dialogo(context,"402 Payment Required (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_PRECONDITION_FAILED:	
					Util.dialogo(context,"412 Precondition Failed (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_PROCESSING:	
					Util.dialogo(context,"102 Processing (WebDAV - RFC 2518)");
					break;
				case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:	
					Util.dialogo(context,"407 Proxy Authentication Required (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE:	
					Util.dialogo(context,"416 Requested Range Not Satisfiable (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_REQUEST_TIMEOUT:	
					Util.dialogo(context,"408 Request Timeout (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_REQUEST_TOO_LONG:	
					Util.dialogo(context,"413 Request Entity Too Large (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_REQUEST_URI_TOO_LONG:	
					Util.dialogo(context,"414 Request-URI Too Long (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_RESET_CONTENT:	
					Util.dialogo(context,"205 Reset Content (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_SEE_OTHER:	
					Util.dialogo(context,"303 See Other (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_SERVICE_UNAVAILABLE:	
					Util.dialogo(context,"503 Service Unavailable (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_SWITCHING_PROTOCOLS:	
					Util.dialogo(context,"101 Switching Protocols (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_TEMPORARY_REDIRECT:	
					Util.dialogo(context,"307 Temporary Redirect (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_UNAUTHORIZED:	
					Util.dialogo(context,"401 Unauthorized (HTTP/1.0 - RFC 1945)");
					break;
				case HttpStatus.SC_UNPROCESSABLE_ENTITY:	
					Util.dialogo(context,"422 Unprocessable Entity (WebDAV - RFC 2518)");
					break;
				case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:	
					Util.dialogo(context,"415 Unsupported Media Type (HTTP/1.1 - RFC 2616)");
					break;
				case HttpStatus.SC_USE_PROXY:	
					Util.dialogo(context,"305 Use Proxy (HTTP/1.1 - RFC 2616)");
					break;
				default:
					Util.dialogo(context,"Status Code desconhecido: " + httpResponse.getStatusLine().getStatusCode());
					break;
			}
			
		} catch (ClientProtocolException e) {
			Util.dialogo(context, String.valueOf(R.string.mensagem_clientProtocolException));
		} catch (IOException e) {
			Util.dialogo(context,String.valueOf(R.string.mensagem_ioexception));
		} catch (JSONException e) {
			Util.dialogo(context,String.valueOf(R.string.mensagem_jsonexception));
		}
		return objetos;
		
	}
	
}
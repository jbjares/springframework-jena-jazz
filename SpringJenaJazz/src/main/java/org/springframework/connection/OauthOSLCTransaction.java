package org.springframework.connection;


import java.io.Serializable;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.security.Context;
import org.springframework.util.RestUtil;

public abstract class OauthOSLCTransaction implements ITransaction{

	private Context ctx;
	private DefaultHttpClient httpclient = null;
	private BasicCookieStore cookieStore = new BasicCookieStore();
	private HttpContext localContext = new BasicHttpContext();
	private List<NameValuePair> authFormParams = new ArrayList<NameValuePair>();
	
	public OauthOSLCTransaction(Context ctx){
		if(ctx==null){
			throw new RuntimeException("O Objeto Context nao deve estar nulo ao se tentar criar um objeto transacao!");
		}
		this.ctx=ctx;
	}

	public Boolean begin() {
		try{
			if(ctx.getCredenciais()==null || ctx.getCredenciais().getLogin()==null || "".equals(ctx.getCredenciais().getLogin()) || 
					ctx.getCredenciais().getSenha()==null || "".equals(ctx.getCredenciais().getSenha())){
				throw new RuntimeException("Credenciais invalidas: O Objeto credenciais pode estar nulo ou conter atributos vazios e/ou invalidos!");
			}

			authFormParams.add(new BasicNameValuePair(USER_NAME,ctx.getCredenciais().getLogin() ));
			authFormParams.add(new BasicNameValuePair(PASSWORD, ctx.getCredenciais().getSenha()));
			
			UrlEncodedFormEntity encodedentity = new UrlEncodedFormEntity(authFormParams,UTF_8);
			HttpPost httpPostAuth = getHttpPostAuth();
			httpPostAuth.setEntity(encodedentity);
			httpclient = RestUtil.httpClientTrustingAllSSLCerts();
			httpclient.execute(httpPostAuth, localContext);
			return Boolean.TRUE;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}

	}

	public abstract HttpPost getHttpPostAuth();
	
	public abstract HttpGet getHttpGetAuth();
	
	public Boolean close(CookieStore cookieStore) {
		Boolean retorno = Boolean.FALSE;
		try{
			open();
			begin();
			DefaultHttpClient httpclient = RestUtil.httpClientTrustingAllSSLCerts();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpPost httppost = getHttpPostClose();
			HttpResponse response = httpclient.execute(httppost, localContext);
			
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode == 200) {
	        	retorno = Boolean.TRUE;
	        } 
			
			httpclient.getConnectionManager().shutdown();
			httpclient.getCookieStore().clear();
			cookieStore.clear();
			authFormParams.clear();
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
		return retorno;
	}



	public abstract HttpPost getHttpPostClose();

	public Boolean open() {
		try{
			SSLContext mySSLContext = SSLContext.getInstance(SSL);
			mySSLContext.init(null,new TrustManager[]{new X509TrustManagerSemValidacoesAdapter()}, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(mySSLContext.getSocketFactory());
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpGet httpGetID = getHttpGetAuth();
			httpclient = RestUtil.httpClientTrustingAllSSLCerts();
			httpclient.execute(httpGetID, localContext);
			httpGetID.abort();
			return Boolean.TRUE;			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}

	}
	



	public BasicCookieStore getCookieStore() {
		return cookieStore;
	}

	public class X509TrustManagerSemValidacoesAdapter implements X509TrustManager,Serializable {

		private static final long serialVersionUID = -7678145874171110359L;

		public void checkClientTrusted(X509Certificate[] chain, String authType)throws CertificateException {}

		public void checkServerTrusted(X509Certificate[] chain, String authType)throws CertificateException {}

		public X509Certificate[] getAcceptedIssuers() {return null;}

	}


}



package org.springframework.query;




import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.security.Context;


public abstract class AbstractQueryHelper implements IQueryHelper{

	protected Context ctx;
	
	protected DefaultHttpClient httpclient;
	
	protected CookieStore cookieStore;
	
	protected HttpContext localContext;
	
	protected List<NameValuePair> authFormParams;
	
	protected StringBuilder criteria;
	
	protected String urlbase;
	
	public AbstractQueryHelper(Context ctx,DefaultHttpClient httpclient,HttpContext localContext,List<NameValuePair> authFormParams ) {
		if(ctx==null){
			throw new RuntimeException("Contexto nao configurado ou invalido.");
		}
		
		this.urlbase = ctx.getHost();
		if(urlbase==null || "".equals(urlbase)){
			throw new RuntimeException("URL nula e/ou invalida.");
		}
		this.criteria.append(urlbase.toString());
	}
	
	public abstract AbstractQueryHelper appendToURL(String continuacaoUrl);
	
	public abstract AbstractQueryHelper addCriteria(String chave, String valor);

	//public abstract <T> T get(String msgType,Class<T> classeRetorno, OauthOSLCConnection conn);

	
	public String getUrlbase() {
		return urlbase;
	}

	public void setUrlbase(String urlbase) {
		this.urlbase = urlbase;
	}
}

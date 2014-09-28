package org.springframework.query;



import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.security.Context;

public class QueryHelper extends AbstractQueryHelper{
	

	public QueryHelper(Context ctx,DefaultHttpClient httpclient,HttpContext localContext,List<NameValuePair> authFormParams) {
		super(ctx,httpclient,localContext,authFormParams);
	}

	@Override
	public AbstractQueryHelper addCriteria(String chave, String valor) {
		if(!this.criteria.toString().endsWith("?") && !this.criteria.toString().endsWith("&")){
			this.criteria.append("?");
		}
		this.criteria.append(chave);
		this.criteria.append("=");
		this.criteria.append(valor);
		this.criteria.append("&");	
		return this;
	}
	
	@Override
	public AbstractQueryHelper appendToURL(String url) {
		if(this.criteria.toString().endsWith("&")){
			throw new RuntimeException("Nao e possivel acrescentar nada mais na URL apos a adicao de Criteria!");
		}
		if(!this.criteria.toString().endsWith("/") && !url.startsWith("/")){
			this.criteria.append("/");
		}
		this.criteria.append(url);
		this.criteria.append("/");
		return this;
	}



//	@SuppressWarnings("unchecked")
//	@Override
//	public <T> T get(Class<T> classTipoRetornoEsperado, OauthOSLCConnection conn) throws URISyntaxException {
//		return (T) conn.resource(new URI(criteria.toString())).accept(msgType).get(classeRetorno);
//	}
	
}

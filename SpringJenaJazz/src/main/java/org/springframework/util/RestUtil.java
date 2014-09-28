package org.springframework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.security.CredenciaisAcesso;
import org.springframework.security.to.BasicClientCookieTO;
import org.springframework.security.to.SuperQuery;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RestUtil {
	
	public static BasicClientCookieTO[] parseCookieToRTCBasicCookieTO(List<Cookie> list) {
		int size = list.size();
		BasicClientCookieTO[] basicClientCookie = new BasicClientCookieTO[size];
		for(int i=0;i<size;i++){
			Cookie cookie = (Cookie) list.get(i);
			BasicClientCookieTO rtcBasicClientCookie = new BasicClientCookieTO();
			rtcBasicClientCookie.setName(cookie.getName());
			rtcBasicClientCookie.setValue(cookie.getValue());
			rtcBasicClientCookie.setDomain(cookie.getDomain());
			rtcBasicClientCookie.setPath(cookie.getPath());
			rtcBasicClientCookie.setSecure(cookie.isSecure());
			rtcBasicClientCookie.setCookieDomain(cookie.getDomain());
			rtcBasicClientCookie.setCookiePath(cookie.getPath());
			rtcBasicClientCookie.setVersion(cookie.getVersion());
			basicClientCookie[i] = rtcBasicClientCookie;
		}

		return basicClientCookie;
	}
	

	
	private static CredenciaisAcesso parseCredencialAcessoToCredencialAcessoServico(CredenciaisAcesso credenciaisAcesso) {
		CredenciaisAcesso ca = new CredenciaisAcesso();
		ca.setLogin(credenciaisAcesso.getLogin());
		ca.setSenha(credenciaisAcesso.getSenha());
		return ca;
	}



	
	public static List<BasicClientCookie> parseRTCBasicCookieToCookie(BasicClientCookieTO[] cookiesTO) {
		int size = cookiesTO.length;
		List<BasicClientCookie> basicClientCookieList = new ArrayList<BasicClientCookie>();
		for(int i=0;i<size;i++){
			BasicClientCookieTO cookieTO = (BasicClientCookieTO) cookiesTO[i];
			BasicClientCookie basicClientCookie = new BasicClientCookie(cookieTO.getName(),cookieTO.getValue());
			basicClientCookie.setDomain(cookieTO.getDomain());
			basicClientCookie.setPath(cookieTO.getPath());
			basicClientCookie.setSecure(cookieTO.isSecure());
			basicClientCookie.setVersion(cookieTO.getVersion());
			basicClientCookieList.add(basicClientCookie);
		}
		return basicClientCookieList;
	}
	
	public static BasicCookieStore parseRTCBasicClientCookieToCookieStore(BasicClientCookieTO[] cookies) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		for(BasicClientCookie cookie:RestUtil.parseRTCBasicCookieToCookie(cookies)){
			cookieStore.addCookie(cookie);
		}
		return cookieStore;
	}
	
	public final static String JNDI_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";


	private static InitialContext getInitialContext(String url)
			throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, url);

		// *************** UserName & Password for the Initial Context for JNDI
		// lookup *************************
		// ** Make sure that you create a user using
		// "$JBOSS_HOME/bin/add-user.sh" with
		// ** following userName & Password and the Rolename="guest"

		env.put(Context.SECURITY_PRINCIPAL, "user");
		env.put(Context.SECURITY_CREDENTIALS, "pass");
		InitialContext ic = new InitialContext(env);
		return ic;
	}

	public static BasicCookieStore parseRTCBasicClientCookieTOCookieStoreExpired(
			BasicClientCookieTO[] basicClientCookie) {
		try{
			BasicCookieStore cookieStore = new BasicCookieStore();
			for(BasicClientCookie cookie:RestUtil.parseRTCBasicCookieToCookie(basicClientCookie)){
				cookie.setValue("");
				cookieStore.addCookie(cookie);
			}
			return cookieStore;
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}

	public static String parseEntityToString(HttpEntity entityEntrega) throws IOException {
		StringBuilder retorno = new StringBuilder();
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(entityEntrega.getContent(),"UTF-8"));
		
		String line = reader.readLine();
		while (line != null) {
			retorno.append(line);
			line = reader.readLine();
		}
		reader.close();
		return retorno.toString();
	}
	
	
	public static HttpResponse executeHttpGetRdfXmlOSCL(String url, CookieStore cookieStore) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {
		DefaultHttpClient httpclient = httpClientTrustingAllSSLCerts();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE,cookieStore);
        
		httpclient.setCookieStore(cookieStore);
        HttpGet getEntrega = new HttpGet(url);
        getEntrega.addHeader("Accept", "application/rdf+xml");
        getEntrega.addHeader("OSLC-Core-Version", "2.0");

        HttpResponse responseEntrega = httpclient.execute(getEntrega);
		return responseEntrega;
	}

	public static HttpResponse getPutResponseRdfXmlOslc(String putQuery,CookieStore cookieStore, String xml) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {
		DefaultHttpClient httpclient = httpClientTrustingAllSSLCerts();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE,cookieStore);

        httpclient.setCookieStore(cookieStore);   
		HttpPut put = new HttpPut(putQuery);
		put.addHeader("Accept", "application/rdf+xml");
		put.addHeader("Content-Type", "application/rdf+xml");
		put.addHeader("OSLC-Core-Version", "2.0");
		put.setEntity(new StringEntity(xml.toString(),"UTF-8"));
        HttpResponse responseEntrega = httpclient.execute(put);
		return responseEntrega;
	}
	
	public static HttpResponse getPostResponseRdfXmlOslc(
			String postUrl, CookieStore cookieStore,
			String xml) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {
		DefaultHttpClient httpclient = httpClientTrustingAllSSLCerts();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE,cookieStore);

        httpclient.setCookieStore(cookieStore);   
		HttpPost post = new HttpPost(postUrl);
		post.addHeader("Accept", "application/rdf+xml");
		post.addHeader("Content-Type", "application/rdf+xml");
		post.addHeader("OSLC-Core-Version", "2.0");
		post.setEntity(new StringEntity(xml,"UTF-8"));
        HttpResponse responseEntrega = httpclient.execute(post);
		return responseEntrega;
	}
	
   public static DefaultHttpClient httpClientTrustingAllSSLCerts() throws NoSuchAlgorithmException, KeyManagementException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, getTrustingManager(), new java.security.SecureRandom());
        SSLSocketFactory socketFactory = new SSLSocketFactory(sc);
        socketFactory.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme sch = new Scheme("https", socketFactory, 443);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        return httpclient;
    }
    
    private static TrustManager[] getTrustingManager() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}



        } };
        return trustAllCerts;
    }

	public static ResultSet execSuperSelect(String queryNative,CookieStore cookieStore) {

		try{
			SuperQuery superQuery = new SuperQuery(queryNative);
			HttpResponse responseEntrega = RestUtil.executeHttpGetRdfXmlOSCL(superQuery.getQueryBase(),cookieStore);
			HttpEntity entityEntrega = responseEntrega.getEntity();
			
			int statusCode = responseEntrega.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new RuntimeException("Falha ao realizar comando execSuperSelect. statusCode: "+statusCode);
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(entityEntrega.getContent()));
			Model model = ModelFactory.createDefaultModel();
			model.read(reader,superQuery.getQueryContext());
			
			Query query = QueryFactory.create(superQuery.getQuerySPARQ());
			QueryExecution qe = QueryExecutionFactory.create(query, model);			
			
			ResultSet results = qe.execSelect();
			
			return results;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
		
	}

	public static List<String> getResultAsListOfJSON(ResultSet results,String... attributes) {
		List<String> retorno = new ArrayList<String>();
		while(results.hasNext()){
			StringBuilder sistemaurl = new StringBuilder();
			QuerySolution qs = results.next();
			sistemaurl.append("{");
			for(String attribute:attributes){
				if(retorno.contains(attribute)){continue;}
				sistemaurl.append("\""+attribute+"\":"+"\""+qs.get(attribute)+"\"");
				sistemaurl.append(",");
			}
			sistemaurl.append("}");
			String result = sistemaurl.toString();
			if(result.contains(",}")){
				result = result.replace(",}", "}");
			}
			retorno.add(result);
		}
		return retorno;
	}

    

}

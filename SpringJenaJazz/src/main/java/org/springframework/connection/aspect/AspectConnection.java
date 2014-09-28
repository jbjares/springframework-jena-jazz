package org.springframework.connection.aspect;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.connection.OauthOSLCConnection;
import org.springframework.connection.factory.ConnectionFactory;
import org.springframework.security.Context;
import org.springframework.security.to.BasicClientCookieTO;
import org.springframework.util.RestUtil;


@Aspect
public class AspectConnection {

	private Context context;
	
	@Before("execution(* *.*Delegate(..)")
	public void connectionBefore(JoinPoint joinPoint) {
		Signature sig = joinPoint.getSignature();
		Object target = joinPoint.getTarget();
		chamarServicoParaConfigurarCookieStore(sig,target);
	}
	
	
	private void chamarServicoParaConfigurarCookieStore(Signature sig, Object target) {
		try {
			Context ctx = null;
			ctx = getContext(sig,target);
			if(ctx==null){
				throw new RuntimeException("Context not definide!");
			}

			BasicCookieStore cookiestore = startOAuthConnection(context);
			setCookieStoreValue(sig,target,cookiestore);
			call(ctx,cookiestore);
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
	}

	public Boolean finalizarSessaoOauthComRTC(Context ctx,BasicClientCookieTO[] basicClientCookie) {
		Boolean isClosed = Boolean.FALSE;
		try{
			this.context = ctx;
			validarContext();
			OauthOSLCConnection oauthOSLCConnection = ConnectionFactory.getOauthOSLCConnection(context);
			BasicCookieStore cookieStore = RestUtil.parseRTCBasicClientCookieTOCookieStoreExpired(basicClientCookie);
			oauthOSLCConnection.getOauthOSLCTransaction().close(cookieStore);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
		return isClosed;
	}
	
	public BasicCookieStore startOAuthConnection(Context ctx) {
		this.context = ctx;
		validarContext();
		OauthOSLCConnection oauthOSLCConnection = ConnectionFactory.getOauthOSLCConnection(context);
		oauthOSLCConnection.getOauthOSLCTransaction().open();
		oauthOSLCConnection.getOauthOSLCTransaction().begin();
		BasicCookieStore basicCookieStore = oauthOSLCConnection.getOauthOSLCTransaction().getCookieStore();			
		return basicCookieStore;
	}
	
	private void validarContext() {
		if (context == null || context.getCredenciais() == null
				|| context.getHost() == null || "".equals(context.getHost())) {
			throw new RuntimeException("Credenciais de Acesso nao informadas!");
		}
		if(context.getCredenciais().getLogin()==null || "".equals(context.getCredenciais().getLogin())){
			throw new RuntimeException("Login nao informado!");
		}
		if(context.getCredenciais().getSenha()==null || "".equals(context.getCredenciais().getSenha())){
			throw new RuntimeException("Senha nao informado!");
		}
	}


//	private String getValorHashCookie(CookieStore basicCookieStoreAdapter) {
//		String cookieValor = null;
//		for(Cookie cookie:basicCookieStoreAdapter.getCookies()){
//			cookieValor = cookie.getValue();
//		}
//		return cookieValor;
//	}
//	
//	private String getValorHashCookie(List<BasicClientCookie> cookies) {
//		String cookieValor = null;
//		for(Cookie cookie:cookies){
//			cookieValor = cookie.getValue();
//		}
//		return cookieValor;
//	}


	private void call(Context ctx,CookieStore cookieStore) throws KeyManagementException, ClientProtocolException, NoSuchAlgorithmException, IOException {
		int statusCode = 200;
		if("org.springframework.connection.OauthOSLCTransactionCCM".equals(ctx.getClasseDeImplementacaoDeoauthOSLCTransaction())){
			HttpResponse responseInicio = RestUtil.executeHttpGetRdfXmlOSCL("https://"+ctx.getHost()+"/ccm/",cookieStore);				
			statusCode = responseInicio.getStatusLine().getStatusCode();
		}
		if("org.springframework.connection.OauthOSLCTransactionRAM".equals(ctx.getClasseDeImplementacaoDeoauthOSLCTransaction())){
			HttpResponse responseInicio = RestUtil.executeHttpGetRdfXmlOSCL("http://"+ctx.getHost()+"/ram/",cookieStore);
			statusCode = responseInicio.getStatusLine().getStatusCode();
		}
		if("org.springframework.connection.OauthOSLCTransactionJTS".equals(ctx.getClasseDeImplementacaoDeoauthOSLCTransaction())){
			HttpResponse responseInicio = RestUtil.executeHttpGetRdfXmlOSCL("https://"+ctx.getHost()+"/jts/",cookieStore);
			statusCode = responseInicio.getStatusLine().getStatusCode();
		}
		if(statusCode!=200){
			throw new RuntimeException("Falha ao realizar comonicacao com o server, para autenticacao. Status Code: ["+statusCode+"]");
		}
		
	}

//	after() : chamadaServico() {
//		try {
//			Context ctx = null;
//			Signature sig = thisJoinPointStaticPart.getSignature();
//			Object target = thisJoinPoint.getTarget();
//			ctx = getContext(sig,target);
//			if(ctx==null){
//				throw new RuntimeException("Contexto nao definido!");
//			}
//			ConnectionGatewayProxy proxy = new ConnectionGatewayProxy();
//			proxy.setEndpoint(ctx.getGatewayEndPoint());
//			CookieStore cookieStore = getCookieStore(sig, target);
//			if(cookieStore==null){
//				chamarServicoParaConfigurarCookieStore(sig,target);
//			}
//			RTCBasicClientCookieTO[] rtcBasicClientCookieTOArray = BNBRestUtil.parseCookieToRTCBasicCookieTO(cookieStore.getCookies());
//			proxy.finalizarSessaoOauthComRTC(BNBRestUtil.parseContextToContextServico(ctx),BNBRestUtil.parseRTCBasicClientCookieToRTCBasicClientCookieNegocioServico(rtcBasicClientCookieTOArray));
//			setCookieStoreValue(sig,target,cookieStore);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//
//	}
	

	private Context getContext(Signature sig, Object target) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Method metodo = sig.getDeclaringType().getDeclaredMethod("getContext");
		return (Context) metodo.invoke(target);
	}
	
	private CookieStore getCookieStore(Signature sig, Object target) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Method metodo = sig.getDeclaringType().getDeclaredMethod("getCookieStore");
		return (CookieStore) metodo.invoke(target);
	}
	private void setCookieStoreValue(Signature sig, Object target,CookieStore cookieStore)throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, SecurityException{
		@SuppressWarnings("unchecked")
		Method metodo = sig.getDeclaringType().getDeclaredMethod("setCookieStore",CookieStore.class);
		metodo.invoke(target,cookieStore);
	}
	@SuppressWarnings("unchecked")
	private void setCookieStoreJTSValue(Signature sig, Object target,CookieStore cookieStoreJTS) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method metodo  = null;
		try{
			metodo = sig.getDeclaringType().getDeclaredMethod("setCookieStoreJTS",CookieStore.class);
		}catch(NoSuchMethodException e){return;}
		metodo.invoke(target,cookieStoreJTS);
	}
	@SuppressWarnings("unchecked")
	private CookieStore getCookieStoreJTS(Signature sig, Object target) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method metodo  = null;
		try{
			metodo = sig.getDeclaringType().getDeclaredMethod("getCookieStoreJTS");
		}catch(NoSuchMethodException e){return null;}
		return (CookieStore) metodo.invoke(target);
	}
	
	
}

package org.springframework.connection;


import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.security.Context;

public class OauthOSLCTransactionCCM extends OauthOSLCTransaction{

	private Context ctx;
	
	public OauthOSLCTransactionCCM(Context ctx) {
		super(ctx);
		this.ctx=ctx;
	}

	@Override
	public HttpPost getHttpPostAuth() {
		HttpPost httpPostAuth = new HttpPost("https://"+ctx.getHost()+CHECAR_USUARIO_PRE_AUTORIXADO_URL_DEFAULT);
		return httpPostAuth;
	}

	@Override
	public HttpGet getHttpGetAuth() {
		String httpgetStr = HTTPS+ctx.getHost()+IDENTIFICACAO_AUTENTICACAO_URL_DEFAULT;
		HttpGet httpGetID = new HttpGet(httpgetStr);
		return httpGetID;
	}

	@Override
	public HttpPost getHttpPostClose() {
		HttpPost httppost = new HttpPost("https://"+ctx.getHost()+"/ccm");
		return httppost;
	}

}

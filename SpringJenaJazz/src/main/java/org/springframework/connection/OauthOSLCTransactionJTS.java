package org.springframework.connection;


import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.security.Context;

public class OauthOSLCTransactionJTS extends OauthOSLCTransaction{

	private Context ctx;
	
	public OauthOSLCTransactionJTS(Context ctx) {
		super(ctx);
		this.ctx=ctx;
	}

	@Override
	public HttpPost getHttpPostAuth() {
		HttpPost httpPostAuth = new HttpPost("https://"+ctx.getHost()+"/jts/authenticated/j_security_check");
		return httpPostAuth;
	}

	@Override
	public HttpGet getHttpGetAuth() {
		String httpgetStr = HTTPS+ctx.getHost()+"/jts/authenticated/identity";
		HttpGet httpGetID = new HttpGet(httpgetStr);
		return httpGetID;
	}

	@Override
	public HttpPost getHttpPostClose() {
		HttpPost httppost = new HttpPost("https://"+ctx.getHost()+"/jts");
		return httppost;
	}

}

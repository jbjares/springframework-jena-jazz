package org.springframework.connection;



import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.security.Context;

public class OauthOSLCTransactionRAM extends OauthOSLCTransaction{

	
	private Context ctx;
	
	public OauthOSLCTransactionRAM(Context ctx) {
		super(ctx);
		this.ctx=ctx;
	}

	@Override
	public HttpPost getHttpPostAuth() {
		HttpPost httpPostAuth = new HttpPost("http://"+ctx.getHost()+"/ram/authenticated/j_security_check");
		return httpPostAuth;
	}

	@Override
	public HttpGet getHttpGetAuth() {
		String httpgetStr = "http://"+ctx.getHost()+"/ram/secure/loginProxy.faces";
		HttpGet httpGetID = new HttpGet(httpgetStr);
		return httpGetID;
	}

	@Override
	public HttpPost getHttpPostClose() {
		HttpPost httppost = new HttpPost("http://"+ctx.getHost()+"/ram");
		return httppost;
	}
}

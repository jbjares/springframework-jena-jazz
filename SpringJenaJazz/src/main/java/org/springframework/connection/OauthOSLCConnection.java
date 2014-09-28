package org.springframework.connection;



import java.lang.reflect.Constructor;

import org.springframework.security.Context;


public class OauthOSLCConnection{

	private Context ctx;
	
	private OauthOSLCTransaction oauthOSLCTransaction;
	
	public OauthOSLCConnection(Context ctx){
		this.ctx=ctx;
	}

	@SuppressWarnings("unchecked")
	public OauthOSLCTransaction getOauthOSLCTransaction(){
		if(oauthOSLCTransaction!=null){return this.oauthOSLCTransaction;}
	try {		   
		Constructor constructor = Class.forName(ctx.getClasseDeImplementacaoDeoauthOSLCTransaction()).getConstructor(Context.class);		   
		this.oauthOSLCTransaction=(OauthOSLCTransaction) constructor.newInstance(ctx);  
		return  this.oauthOSLCTransaction;
	} 
	catch ( Exception e ) {
	   throw new RuntimeException(e.getMessage(), e);
	}
}

}

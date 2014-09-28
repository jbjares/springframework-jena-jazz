package org.springframework.connection.factory;



import java.lang.reflect.Constructor;

import org.springframework.connection.OauthOSLCConnection;
import org.springframework.security.Context;

public abstract class ConnectionFactory {

	@SuppressWarnings("unchecked")
	public static OauthOSLCConnection getOauthOSLCConnection(Context ctx){
		try {		   
			Constructor constructor = OauthOSLCConnection.class.getConstructor(Context.class);		   
			constructor.setAccessible(true);
			return  (OauthOSLCConnection) constructor.newInstance(ctx);   
		} 
		catch ( Exception e ) {
		   throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}

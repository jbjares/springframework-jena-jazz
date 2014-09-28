package org.springframework.connection;



import org.apache.http.client.CookieStore;

public interface ITransaction {
	
	String USER_NAME = "j_username";
	
	String PASSWORD = "j_password";
	
	String SSL  = "SSL";
	
	String HTTPS = "https://";
	
	String IDENTIFICACAO_AUTENTICACAO_URL_DEFAULT = "/ccm/authenticated/identity";
	
	String UTF_8 = "UTF-8";
	
	String CHECAR_USUARIO_PRE_AUTORIXADO_URL_DEFAULT = "/ccm/authenticated/j_security_check";
	
	public Boolean begin();
	
	public Boolean open();

	Boolean close(CookieStore cookieStore);


}

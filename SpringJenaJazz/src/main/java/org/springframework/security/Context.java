package org.springframework.security;



import java.io.Serializable;

public class Context implements Serializable{
	
	private static final long serialVersionUID = 8019458356385971239L;
	
	public Context(){}
	
	private CredenciaisAcesso credenciais;
	
	private String host;

	private String gatewayEndPoint;
	
	private String jmsCorrelationId;
	
	private String classeDeImplementacaoDeoauthOSLCTransaction;
	


	public String getClasseDeImplementacaoDeoauthOSLCTransaction() {
		return classeDeImplementacaoDeoauthOSLCTransaction;
	}

	public void setClasseDeImplementacaoDeoauthOSLCTransaction(
			String classeDeImplementacaoDeoauthOSLCTransaction) {
		this.classeDeImplementacaoDeoauthOSLCTransaction = classeDeImplementacaoDeoauthOSLCTransaction;
	}

	public String getJmsCorrelationId() {
		return jmsCorrelationId;
	}

	public void setJmsCorrelationId(String jmsCorrelationId) {
		this.jmsCorrelationId = jmsCorrelationId;
	}

	public CredenciaisAcesso getCredenciais() {
		return credenciais;
	}

	public void setCredenciais(CredenciaisAcesso credenciais) {
		this.credenciais = credenciais;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGatewayEndPoint() {
		return gatewayEndPoint;
	}

	public void setGatewayEndPoint(String gatewayEndPoint) {
		this.gatewayEndPoint = gatewayEndPoint;
	}


	
	

}

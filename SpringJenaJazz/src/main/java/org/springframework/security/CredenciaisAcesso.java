package org.springframework.security;


import java.io.Serializable;

public class CredenciaisAcesso implements Serializable {
	
	private static final long serialVersionUID = -6227147243716971556L;
	
	public CredenciaisAcesso(){}

	private String login;
	
	private String senha;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}

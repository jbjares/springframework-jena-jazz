package org.springframework.service.api;



import org.springframework.security.to.BasicClientCookieTO;

public interface IService {
	public String executar(BasicClientCookieTO[] rtcBasicClientCookieTO,String... argumentosEntradaArray) throws Exception;
}

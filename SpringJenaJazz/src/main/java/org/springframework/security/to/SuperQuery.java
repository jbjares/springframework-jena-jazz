package org.springframework.security.to;

import org.springframework.util.MapperQueryStringToQueryObject;


public class SuperQuery {
	
	private String queryBase;
	
	private String queryContext;
	
	private String queryNative;
	
	private String querySPARQ;


	public String getQuerySPARQ() {
		return querySPARQ;
	}

	public void setQuerySPARQ(String querySPARQ) {
		this.querySPARQ = querySPARQ;
	}

	public SuperQuery(String queryNative) {
		this.queryNative=queryNative;
		MapperQueryStringToQueryObject.parseNativeQueryToSuperQuery(this);
	}

	public String getQueryBase() {
		return queryBase;
	}

	public void setQueryBase(String queryBase) {
		this.queryBase = queryBase;
	}

	public String getQueryContext() {
		return queryContext;
	}

	public void setQueryContext(String queryContext) {
		this.queryContext = queryContext;
	}

	public String getQueryNative() {
		return queryNative;
	}

	public void setQueryNative(String queryNative) {
		this.queryNative = queryNative;
	}
	
	
	

}

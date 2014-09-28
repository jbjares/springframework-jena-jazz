package org.springframework.util;



import org.springframework.security.to.SuperQuery;

public class MapperQueryStringToQueryObject {

	public static void parseNativeQueryToSuperQuery(SuperQuery superQuery) {
		
		if(superQuery==null || "".equals(superQuery) || superQuery.getQueryNative()==null || "".equals(superQuery)){
			throw new RuntimeException("SPARQL Query Native vazia e/ou nula");
		}
		String queryNative = superQuery.getQueryNative();
		String queryBase = ""; 
		
		while(queryNative.contains("<url-base>")){
			queryBase = queryNative.substring(queryNative.indexOf("<url-base>")+10, queryNative.indexOf("</url-base>"));
			String queryBaseRemove = queryNative.substring(queryNative.indexOf("<url-base>"), queryNative.indexOf("</url-base>")+11);
			superQuery.setQueryBase(queryBase);
			queryNative = queryNative.replace(queryBaseRemove,"");
		}
		
		String queryContext = ""; 
		while(queryNative.contains("<url-context>")){
			queryContext = queryNative.substring(queryNative.indexOf("<url-context>")+13, queryNative.indexOf("</url-context>"));
			String queryContextRemove = queryNative.substring(queryNative.indexOf("<url-context>"), queryNative.indexOf("</url-context>")+14);
			superQuery.setQueryContext(queryContext);
			queryNative = queryNative.replace(queryContextRemove,"");
		}
		superQuery.setQuerySPARQ(queryNative);
		superQuery.setQueryNative(queryNative);

		
	}
	
	public static void main(String[] args) {
		
		StringBuilder queryStringSistemas = new StringBuilder();
		queryStringSistemas.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
		queryStringSistemas.append("PREFIX acc: <http://jazz.net/ns/acp#> ");
		queryStringSistemas.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
		queryStringSistemas.append("PREFIX ram_asset: <http://jazz.net/xmlns/prod/ram/2.0/> ");
		queryStringSistemas.append("PREFIX oslc: <http://open-services.net/ns/core#> ");
		queryStringSistemas.append("PREFIX oslc_asset: <http://open-services.net/ns/asset#> ");
		queryStringSistemas.append("PREFIX dcterms: <http://purl.org/dc/terms/> ");
		queryStringSistemas.append("<url-base>http://lalaia/ram/oslc/assets?pageSize=1000000&amp;startIndex=0</url-base>");
		queryStringSistemas.append("<url-context>http://lalaia/ram</url-context>");
		queryStringSistemas.append("SELECT  ?tipo ?uuid ?version");
		queryStringSistemas.append(" WHERE { ?item dcterms:type ?tipo . ");
		queryStringSistemas.append("  ?item oslc_asset:guid ?uuid  . ");
		queryStringSistemas.append("  ?item oslc_asset:version ?version  . ");
		queryStringSistemas.append("  ?item dcterms:type <http://s2ramd01:9080/ram/internal/types/classif/assetTypesSchema.xmi%23sistema>  . }");
		
		SuperQuery sq = new SuperQuery(queryStringSistemas.toString().trim());
		System.out.println(sq.getQueryBase());
		System.out.println(sq.getQueryContext());
	}

}

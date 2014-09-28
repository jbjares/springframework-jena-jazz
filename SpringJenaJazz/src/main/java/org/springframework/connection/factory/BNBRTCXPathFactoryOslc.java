package org.springframework.connection.factory;



import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class BNBRTCXPathFactoryOslc {

	public static XPath getXPath(final Map<String, String> namespaces) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				for (Map.Entry<String, String> entry : namespaces.entrySet()) {
					if(prefix.equals(entry.getKey())){
						return entry.getValue();
					}
				}
				throw new RuntimeException("Prefixo: "+prefix+" indefinido. Adicionar o valor para esta chave ao namespace Map.");
			}

			public String getPrefix(String namespaceURI) {
				return null; // we are not using this.
			}

			@SuppressWarnings("rawtypes")
			public Iterator getPrefixes(String namespaceURI) {
				return null; // we are not using this.
			}
		});
		return xpath;
	}

}

package org.springframework.security.to;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.SetCookie;

public class BasicClientCookieTO implements SetCookie, ClientCookie, Cloneable, Serializable {
	

		private static final long serialVersionUID = 7146707305662556194L;
		private String name;
		private Map attribs;
		private String value;
		private String cookieComment;
		private String cookieDomain;
		private Date cookieExpiryDate;
		private String cookiePath;
		private boolean isSecure;
		private int cookieVersion;
		
		public BasicClientCookieTO() {
				attribs = new HashMap();
		}
		

		public String getName() {
			return name;
		}

		public Map getAttribs() {
			return attribs;
		}

		public void setAttribs(Map attribs) {
			this.attribs = attribs;
		}

		public String getCookieComment() {
			return cookieComment;
		}

		public void setCookieComment(String cookieComment) {
			this.cookieComment = cookieComment;
		}

		public String getCookieDomain() {
			return cookieDomain;
		}

		public void setCookieDomain(String cookieDomain) {
			this.cookieDomain = cookieDomain;
		}

		public Date getCookieExpiryDate() {
			return cookieExpiryDate;
		}

		public void setCookieExpiryDate(Date cookieExpiryDate) {
			this.cookieExpiryDate = cookieExpiryDate;
		}

		public String getCookiePath() {
			return cookiePath;
		}

		public void setCookiePath(String cookiePath) {
			this.cookiePath = cookiePath;
		}

		public int getCookieVersion() {
			return cookieVersion;
		}

		public void setCookieVersion(int cookieVersion) {
			this.cookieVersion = cookieVersion;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getComment() {
			return cookieComment;
		}

		public void setComment(String comment) {
			cookieComment = comment;
		}

		public String getCommentURL() {
			return null;
		}

		public Date getExpiryDate() {
			return cookieExpiryDate;
		}

		public void setExpiryDate(Date expiryDate) {
			cookieExpiryDate = expiryDate;
		}

		public boolean isPersistent() {
			return null != cookieExpiryDate;
		}

		public String getDomain() {
			return cookieDomain;
		}

		public void setDomain(String domain) {
			if (domain != null)
				cookieDomain = domain.toLowerCase(Locale.ENGLISH);
			else
				cookieDomain = null;
		}

		public String getPath() {
			return cookiePath;
		}

		public void setPath(String path) {
			cookiePath = path;
		}

		public boolean isSecure() {
			return isSecure;
		}

		public void setSecure(boolean secure) {
			isSecure = secure;
		}

		public int[] getPorts() {
			return null;
		}

		public int getVersion() {
			return cookieVersion;
		}

		public void setVersion(int version) {
			cookieVersion = version;
		}

		public boolean isExpired(Date date) {
			if (date == null)
				throw new IllegalArgumentException("Date may not be null");
			else
				return cookieExpiryDate != null
						&& cookieExpiryDate.getTime() <= date.getTime();
		}

		public void setAttribute(String name, String value) {
			attribs.put(name, value);
		}

		public String getAttribute(String name) {
			return (String) attribs.get(name);
		}

		public boolean containsAttribute(String name) {
			return attribs.get(name) != null;
		}

		public Object clone() throws CloneNotSupportedException {
			BasicClientCookieTO clone = (BasicClientCookieTO) super.clone();
			clone.attribs = new HashMap(attribs);
			return clone;
		}

		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append("[version: ");
			buffer.append(Integer.toString(cookieVersion));
			buffer.append("]");
			buffer.append("[name: ");
			buffer.append(name);
			buffer.append("]");
			buffer.append("[value: ");
			buffer.append(value);
			buffer.append("]");
			buffer.append("[domain: ");
			buffer.append(cookieDomain);
			buffer.append("]");
			buffer.append("[path: ");
			buffer.append(cookiePath);
			buffer.append("]");
			buffer.append("[expiry: ");
			buffer.append(cookieExpiryDate);
			buffer.append("]");
			return buffer.toString();
		}



}

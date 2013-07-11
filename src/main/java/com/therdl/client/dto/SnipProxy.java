package com.therdl.client.dto;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.therdl.server.model.Snip;


@ProxyFor(value = Snip.class)
public interface SnipProxy extends EntityProxy {
	
	public Long getId();
		

	public void setId(Long id);
		

	public String getTitle();
		

	public void setTitle(String title);
		

	public String getAuthor();
		

	public void setAuthor(String author);
		

	public Date getDate();
		

	public void setDate(Date date);
		

	public void setContent(String content);
		

	public String getContent();
		

	public void setIsAlias(Boolean isAlias);
		

	public Boolean getIsAlias();
	
	EntityProxyId<SnipProxy> stableId();
		
}

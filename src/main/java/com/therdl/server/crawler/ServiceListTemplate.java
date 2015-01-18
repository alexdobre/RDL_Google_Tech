package com.therdl.server.crawler;

import com.therdl.shared.beans.SnipBean;

import java.util.List;

/**
 * Backing object for the snip list mustache template
 */
public class ServiceListTemplate {

	private List<SnipBean> servicesList;

	private String nextPageLink;
	private String moduleName;
	private boolean renderNextPage;

	public ServiceListTemplate(List<SnipBean> serviceBeans, String nextPageLink, String moduleName, boolean renderNextPage){
		this.servicesList = serviceBeans;
		this.nextPageLink = nextPageLink;
		this.renderNextPage = renderNextPage;
		this.moduleName = moduleName;
	}

	public List<SnipBean> getServicesList() {
		return servicesList;
	}

	public void setServicesList(List<SnipBean> servicesList) {
		this.servicesList = servicesList;
	}

	public String getNextPageLink() {
		return nextPageLink;
	}

	public void setNextPageLink(String nextPageLink) {
		this.nextPageLink = nextPageLink;
	}

	public boolean isRenderNextPage() {
		return renderNextPage;
	}

	public void setRenderNextPage(boolean renderNextPage) {
		this.renderNextPage = renderNextPage;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}

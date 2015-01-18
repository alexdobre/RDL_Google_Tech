package com.therdl.server.crawler;

import com.therdl.shared.beans.SnipBean;

import java.util.List;

/**
 *  Backing object for the snip view mustache template
 */
public class ServiceViewTemplate {

	private SnipBean snipBean;
	private List<SnipBean> repliesList;
	private String nextPageLink;
	private boolean renderNextPage;


	public ServiceViewTemplate(SnipBean snipBean, List<SnipBean> repliesList, String nextPageLink, boolean renderNextPage){
		this.snipBean = snipBean;
		this.repliesList = repliesList;
		this.nextPageLink = nextPageLink;
		this.renderNextPage = renderNextPage;
	}

	public SnipBean getSnipBean() {
		return snipBean;
	}

	public void setSnipBean(SnipBean snipBean) {
		this.snipBean = snipBean;
	}

	public List<SnipBean> getRepliesList() {
		return repliesList;
	}

	public void setRepliesList(List<SnipBean> repliesList) {
		this.repliesList = repliesList;
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
}


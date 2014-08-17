package com.therdl.server.crawler;

import com.therdl.shared.beans.SnipBean;

import java.util.List;

/**
 * Backing object for the snip list mustache template
 */
public class SnipListTemplate {

	private List<SnipBean> snipsList;

	private String nextPageLink;
	private boolean renderNextPage;

	public SnipListTemplate (List<SnipBean> snipBeans, String nextPageLink, boolean renderNextPage){
		this.snipsList = snipBeans;
		this.nextPageLink = nextPageLink;
		this.renderNextPage = renderNextPage;
	}

	public List<SnipBean> getSnipsList() {
		return snipsList;
	}

	public void setSnipsList(List<SnipBean> snipsList) {
		this.snipsList = snipsList;
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

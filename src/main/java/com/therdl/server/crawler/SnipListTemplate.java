package com.therdl.server.crawler;

import com.therdl.shared.beans.SnipBean;

import java.util.List;

/**
 * Backing object for the snip list mustache template
 */
public class SnipListTemplate {

	private List<SnipBean> snipsList;

	private String moduleName;
	private int nextPage;

	public SnipListTemplate (List<SnipBean> snipBeans, String moduleName, int nextPage){
		this.snipsList = snipBeans;
		this.moduleName = moduleName;
		this.nextPage = nextPage;
	}

	public List<SnipBean> getSnipsList() {
		return snipsList;
	}

	public void setSnipsList(List<SnipBean> snipsList) {
		this.snipsList = snipsList;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}

package com.therdl.server.crawler;

import com.therdl.shared.beans.SnipBean;

import java.util.List;

/**
 *  Backing object for the snip view mustache template
 */
public class SnipViewTemplate {

	private SnipBean snipBean;

	public SnipViewTemplate (SnipBean snipBean){
		this.snipBean = snipBean;
	}

	public SnipBean getSnipBean() {
		return snipBean;
	}

	public void setSnipBean(SnipBean snipBean) {
		this.snipBean = snipBean;
	}
}


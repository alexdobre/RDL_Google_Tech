package com.therdl.client.presenter.runt.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.FuncFactory;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.presenter.runt.ContentMgmtRunt;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;


public class ContentMgmtRuntImpl implements ContentMgmtRunt {

	private GrabSnipFunc grabSnipFunc;
	private Beanery beanery = GWT.create(Beanery.class);

	public ContentMgmtRuntImpl() {
		this.grabSnipFunc = FuncFactory.createGrabSnipFunc();
	}

	@Override
	public void grabAndDisplay(AutoBean<SnipBean> searchOptions, final Panel location) {
		grabSnipFunc.grabSnip(searchOptions, new SnipCallback() {
			@Override
			public void onSnipBeanReturned(AutoBean<SnipBean> snipBean) {
				location.clear();
				location.getElement().setInnerHTML(snipBean.as().getContent());
			}
		});
	}

	@Override
	public void displaySignInMsg(Panel location) {
		AutoBean<SnipBean> searchOptions = beanery.snipBean();
		ViewUtils.populateDefaultSearchOptions(searchOptions);
		searchOptions.as().setAuthor("RDL");
		searchOptions.as().setSnipType(SnipType.CONTENT_MGMT.getSnipType());
		searchOptions.as().setCoreCat(CoreCategory.GENERAL.getShortName());
		searchOptions.as().setTitle("SignUpMessage");

		grabAndDisplay(searchOptions, location);
	}
}

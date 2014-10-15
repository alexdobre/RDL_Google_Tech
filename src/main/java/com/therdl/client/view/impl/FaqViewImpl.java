package com.therdl.client.view.impl;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.FaqView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The FAQ view
 */
public class FaqViewImpl extends AppMenuView implements FaqView {

	private FaqView.Presenter presenter;
	private AutoBean<CurrentUserBean> currentUserBean;

	interface FaqViewImplUiBinder extends UiBinder<Widget, FaqViewImpl> {
	}

	private static FaqViewImplUiBinder uiBinder = GWT.create(FaqViewImplUiBinder.class);


	public FaqViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		this.currentUserBean = currentUserBean;
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = cUserBean;
	}

	@Override
	public void setPresenter(FaqView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void populateFaq(List<AutoBean<SnipBean>> faqList) {
		//TODO implement FaqViewImpl.populateFaq
	}
}
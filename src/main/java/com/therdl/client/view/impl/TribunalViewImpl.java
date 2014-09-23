package com.therdl.client.view.impl;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.TribunalView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * The tribunal view
 */
public class TribunalViewImpl extends AppMenuView implements TribunalView {

	private static Logger log = Logger.getLogger(TribunalViewImpl.class.getName());

	private static TribunalViewImplUiBinder uiBinder = GWT.create(TribunalViewImplUiBinder.class);

	@Override
	public void setPresenter(TribunalView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public TribunalView.Presenter getPresenter() {
		return presenter;
	}

	interface TribunalViewImplUiBinder extends UiBinder<Widget, TribunalViewImpl> {
	}

	private TribunalView.Presenter presenter;


	private AutoBean<CurrentUserBean> currentUserBean;
	private Beanery beanery = GWT.create(Beanery.class);

	public TribunalViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;
	}
}

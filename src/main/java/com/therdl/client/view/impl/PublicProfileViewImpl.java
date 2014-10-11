package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.PublicProfileView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;
import org.gwtbootstrap3.client.ui.PanelBody;

/**
 * The user's public profile
 */
public class PublicProfileViewImpl extends AppMenuView implements PublicProfileView {

	private PublicProfileView.Presenter presenter;
	private AutoBean<CurrentUserBean> currentUserBean;

	interface PublicProfileViewImplUiBinder extends UiBinder<Widget, PublicProfileViewImpl> {
	}

	private static PublicProfileViewImplUiBinder uiBinder = GWT.create(PublicProfileViewImplUiBinder.class);

	@UiField
	PanelBody profileDesc;


	public PublicProfileViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		this.currentUserBean = currentUserBean;
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = cUserBean;
	}

	@Override
	public void populateProfileDescription (String content) {
		profileDesc.getElement().setInnerHTML(content);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}

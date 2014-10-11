package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.LicenseView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.beans.CurrentUserBean;
import org.gwtbootstrap3.client.ui.PanelBody;

/**
 * The license view
 */
public class LicenseViewImpl extends AppMenuView implements LicenseView {

	private LicenseView.Presenter presenter;
	private AutoBean<CurrentUserBean> currentUserBean;

	interface LicenseViewImplUiBinder extends UiBinder<Widget, LicenseViewImpl> {
	}

	private static LicenseViewImplUiBinder uiBinder = GWT.create(LicenseViewImplUiBinder.class);

	@UiField
	PanelBody licenseDesc;


	public LicenseViewImpl(AutoBean<CurrentUserBean> cUserBean, AppMenu appMenu) {
		super(appMenu);
		this.currentUserBean = currentUserBean;
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = cUserBean;
	}

	@Override
	public void populateLicense(String content) {
		licenseDesc.getElement().setInnerHTML(content);
	}

	@Override
	public void setPresenter(LicenseView.Presenter presenter) {
		this.presenter = presenter;
	}
}

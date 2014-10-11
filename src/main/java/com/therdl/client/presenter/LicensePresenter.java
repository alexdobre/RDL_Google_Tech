package com.therdl.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipCallback;
import com.therdl.client.presenter.func.FuncFactory;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.view.LicenseView;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The license presenter
 */
public class LicensePresenter extends RdlAbstractPresenter<LicenseView> implements LicenseView.Presenter {

	private AutoBean<CurrentUserBean> currentUserBean;

	public LicensePresenter(LicenseView licenseView, AppController controller) {
		super(controller);
		this.view = licenseView;
		this.view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		this.currentUserBean = currentUserBean;
		container.clear();
		GrabSnipFunc grabSnipFunc = FuncFactory.createGrabSnipFunc();
		AutoBean<SnipBean> searchOptionsBean = buildLicenseSearchOptions();

		grabSnipFunc.grabSnip(searchOptionsBean, new SnipCallback() {
			@Override
			public void onSnipBeanReturned(AutoBean<SnipBean> snipBean) {
				if (snipBean == null) {
					view.populateLicense(RDL.getI18n().serverErrorC000());
				} else {
					view.populateLicense(snipBean.as().getContent());
				}
			}
		});

		container.add(view.asWidget());
	}

	private AutoBean<SnipBean> buildLicenseSearchOptions() {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		searchOptionsBean.as().setCoreCat(CoreCategory.GENERAL.getShortName());
		searchOptionsBean.as().setSnipType(SnipType.SNIP.getSnipType());
		searchOptionsBean.as().setTitle(RDLConstants.LICENSE_TITLE);
		return searchOptionsBean;
	}
}

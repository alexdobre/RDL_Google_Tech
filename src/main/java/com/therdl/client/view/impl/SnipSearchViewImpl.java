package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.SnipSearchView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.SnipListRowWidget;
import com.therdl.client.view.widget.WidgetHolder;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.logging.Logger;

public class SnipSearchViewImpl extends Composite implements SnipSearchView {

    private static Logger log = Logger.getLogger("");

	private static SnipSearchViewImplUiBinder uiBinder = GWT
			.create(SnipSearchViewImplUiBinder.class);

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface SnipSearchViewImplUiBinder extends
			UiBinder<Widget, SnipSearchViewImpl> {
	}
	
	private Presenter presenter;

    private AppMenu appMenuPanel;

    @UiField HTMLPanel appMenu;
    @UiField Widget snipSearchWidget;
	@UiField Widget leftMenuTree;
    @UiField
    FlowPanel snipListRow;

    private SnipListRowWidget snipListRowWidget;

	public SnipSearchViewImpl(AutoBean<CurrentUserBean> authnBean) {

		initWidget(uiBinder.createAndBindUi(this));

        appMenuPanel = (AppMenu) WidgetHolder.getInstance().getAppMenu();
        appMenu.add(appMenuPanel);
		snipSearchWidget = WidgetHolder.getInstance().getSnipSearchWidget();

		leftMenuTree = WidgetHolder.getInstance().getLeftMenuTree();

        log.info("SnipSearchViewImpl constructor");
	}

    /**
     * this is a temporary debug method to test server development, soon to be changed to refelt
     * evolution of the code base
     */

    @Override
    public void getSnipDemoResult(AutoBean<SnipBean> bean) {

        log.info("SnipSearchViewImpl getSnipDemoResult ");
        snipListRowWidget = new SnipListRowWidget(bean);
        snipListRow.clear();
        snipListRow.add(snipListRowWidget);

        // debug
        log.info("SnipSearchViewImpl getSnipDemoResult "+ bean.as().getTitle());

    }

    @Override
    public void setloginresult(String name, String email, boolean auth) {
        if (auth) {
            log.info("SnipSearchViewImpl setloginresult auth true "+name );

            this.appMenuPanel.setLogOutVisible(true);
            this.appMenuPanel.setSignUpVisible(false);
            this.appMenuPanel.setUserInfoVisible(true);
            this.appMenuPanel.setUser(name);
            this.appMenuPanel.setEmail(email);
        }

    }

    @Override
    public AppMenu getAppMenu() {
        return this.appMenuPanel;
    }




}

package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.FaqView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.FaqPanel;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.PanelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * The FAQ view
 */
public class FaqViewImpl extends AppMenuView implements FaqView {

	interface FaqViewImplUiBinder extends UiBinder<Widget, FaqViewImpl> {
	}

	private static FaqViewImplUiBinder uiBinder = GWT.create(FaqViewImplUiBinder.class);

	private FaqView.Presenter presenter;
	private AutoBean<CurrentUserBean> currentUserBean;
	private List<FaqPanel> faqPanelList;

	@UiField
	PanelGroup panelGroup;

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
		faqPanelList = new ArrayList<FaqPanel>(faqList.size());
		panelGroup.clear();
		for (AutoBean<SnipBean> faqBean : faqList) {
			FaqPanel faqPanel = new FaqPanel();
			faqPanel.populate(faqBean);
			faqPanelList.add(faqPanel);
			panelGroup.add(faqPanel);
		}
	}

	public boolean isPopulated() {
		if (faqPanelList != null && !faqPanelList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
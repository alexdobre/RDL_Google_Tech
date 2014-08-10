package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.UserBean;
import org.gwtbootstrap3.client.ui.Container;

import java.util.logging.Logger;

/**
 * This widget is a row in the titles list in the user's profile
 * Created by Alex on 07/02/14.
 */
public class TitleListRow extends Composite {
	private static Logger log = Logger.getLogger("");

	interface TitleListRowUiBinder extends UiBinder<Container, TitleListRow> {
	}

	private static TitleListRowUiBinder ourUiBinder = GWT.create(TitleListRowUiBinder.class);

	AutoBean<CurrentUserBean> currentUserBean;
	UserBean.TitleBean title;

	@UiField
	Label titleLabel;

	/**
	 * We supply the user bean and title to display to the row
	 *
	 * @param currentUserBean
	 * @param title
	 */
	public TitleListRow(AutoBean<CurrentUserBean> currentUserBean, UserBean.TitleBean title) {
		initWidget(ourUiBinder.createAndBindUi(this));
		this.title = title;
		this.currentUserBean = currentUserBean;
	}


	@Override
	protected void onLoad() {
		super.onLoad();
		titleLabel.setText(title.getTitleName());
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}
}

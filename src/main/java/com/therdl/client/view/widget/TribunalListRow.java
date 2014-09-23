package com.therdl.client.view.widget;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.impl.AbstractListRow;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The tribunal list row
 */
public class TribunalListRow extends AbstractListRow{

	private static Logger log = Logger.getLogger(TribunalListRow.class.getName());

	interface TribunalListRowUiBinder extends UiBinder<Widget, TribunalListRow> {
	}

	private static TribunalListRowUiBinder ourUiBinder = GWT.create(TribunalListRowUiBinder.class);

	public TribunalListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType,
			UIObject parent) {
		super(snipBean, currentUserBean, snipType, parent);
		initWidget(ourUiBinder.createAndBindUi(this));
		populate(snipBean, currentUserBean, snipType);
	}

	public TribunalListRow() {
		//used for a seed instance
	}

	@Override
	public void populate(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType) {
		log.info("TribunalListRow populate with title: " + snipBean.as().getTitle());
		this.snipBean = snipBean;
		this.currentUserBean = currentUserBean;
		this.snipType = snipType;

		doBackgroundColor();
//		doTexts();
//		doImages();
//		showHide();
	}

	@Override
	public AbstractListRow makeRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType, UIObject parent) {
		return null;
	}
}

package com.therdl.client.view.widget;

import com.therdl.client.view.cssbundles.Resources;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Holds common items between list rows
 */
public abstract class AbstractListRow extends Composite {

	protected AutoBean<SnipBean> snipBean;
	protected AutoBean<CurrentUserBean> currentUserBean;
	protected SnipType snipType;
	protected UIObject parent;

	@UiField
	public Anchor snipTitle;
	@UiField
	public Badge displayDate;
	@UiField
	public FlowPanel snipImgParent;
	@UiField
	public Image snipImg;

	public abstract void populate(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean, SnipType snipType);

	public abstract AbstractListRow makeRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean,
			SnipType snipType, UIObject parent);

	public UIObject getParentObject() {
		return parent;
	}

	public AbstractListRow(AutoBean<SnipBean> snipBean, AutoBean<CurrentUserBean> currentUserBean,
			SnipType snipType, UIObject parent) {
		this.parent = parent;
		this.snipBean = snipBean;
		this.currentUserBean = currentUserBean;
		this.snipType = snipType;
	}

	public AbstractListRow() {
	}

	protected void doBackgroundColor() {
		// sets background color for snip img and top color strip
		if (!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			for (CoreCategory item : CoreCategory.values()) {
				if (item.getShortName().equals(snipBean.as().getCoreCat())) {
					snipImgParent.getElement().getStyle().setProperty("backgroundColor", item.getColCode());
				}
			}
		} else {
			snipImgParent.getElement().getStyle().setProperty("backgroundColor", "#658cd9");
		}
	}

	protected void doImages(String snipType) {
		if (snipType.equals(SnipType.SERVICE.getSnipType()))
			snipImg.setUrl(Resources.INSTANCE.TagImageGif().getSafeUri().asString());
		if (snipType.equals(RDLConstants.SnipType.SNIP))
			snipImg.setUrl(Resources.INSTANCE.SnipImage().getSafeUri().asString());
		if (snipType.equals(RDLConstants.SnipType.FAST_CAP))
			snipImg.setUrl(Resources.INSTANCE.FastCapImage().getSafeUri().asString());
		if (snipType.equals(RDLConstants.SnipType.HABIT))
			snipImg.setUrl(Resources.INSTANCE.HabitImage().getSafeUri().asString());
		if (snipType.equals(RDLConstants.SnipType.MATERIAL))
			snipImg.setUrl(Resources.INSTANCE.MaterialImage().getSafeUri().asString());
		if (snipType.equals(RDLConstants.SnipType.THREAD))
			snipImg.setUrl(Resources.INSTANCE.ThreadImageGif().getSafeUri().asString());
		if (snipType.equals(RDLConstants.SnipType.PROPOSAL))
			snipImg.setUrl(Resources.INSTANCE.ProposalImageGif().getSafeUri().asString());
	}
}

package com.therdl.client.view.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Common methods used by views
 */
public class ViewUtils {

	/**
	 * Constructs a popup at the place of the event
	 *
	 * @param widget
	 * @param event
	 * @param width
	 * @return
	 */
	public static DecoratedPopupPanel constructPopup(Widget widget, ClickEvent event, int width) {
		final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
		simplePopup.setAnimationEnabled(false);
		simplePopup.setWidth("" + width + "px");
		simplePopup.setWidget(widget);
		positionRelative(event, simplePopup);
		return simplePopup;
	}

	private static void positionRelative(ClickEvent event, PopupPanel dialog) {
		// Reposition the popup relative to the button
		Widget source = (Widget) event.getSource();
		int left = source.getAbsoluteLeft() + 10;
		int top = source.getAbsoluteTop() + 10;
		dialog.setPopupPosition(left, top);
	}

	/**
	 * creates proposal type list
	 */
	public static void createProposalTypeList(ListBox proposalTypeList) {
		Iterator itHm = RDLConstants.ProposalType.proposalTypeHm.entrySet().iterator();
		int i = 0;
		while (itHm.hasNext()) {
			Map.Entry pairs = (Map.Entry) itHm.next();

			proposalTypeList.addItem((String) pairs.getValue());
			proposalTypeList.setValue(i, (String) pairs.getKey());
			i++;
		}
	}

	/**
	 * creates proposal state list
	 */
	public static void createProposalStateList(ListBox proposalStateList) {
		Iterator itHm = RDLConstants.ProposalState.proposalStateHm.entrySet().iterator();
		int i = 0;
		while (itHm.hasNext()) {
			Map.Entry pairs = (Map.Entry) itHm.next();

			proposalStateList.addItem((String) pairs.getValue());
			proposalStateList.setValue(i, (String) pairs.getKey());
			i++;
		}
	}

	/**
	 * checks which items are selected for the given multi select list box
	 *
	 * @return returns items names separated by comma
	 */
	public static String getSelectedItems(ListBox itemList) {
		String selectedValues = "";
		for (int i = 0; i < itemList.getItemCount(); i++) {
			if (itemList.isItemSelected(i)) {
				selectedValues += itemList.getValue(i) + ",";
			}
		}

		if (!selectedValues.equals("")) {
			selectedValues = selectedValues.substring(0, selectedValues.length() - 1);
		}

		return selectedValues;
	}

	/**
	 * returns checked values as concatenated string for the given checkbox array
	 *
	 * @param checkBoxList
	 * @return
	 */
	public static String getCheckedFlags(List<CheckBox> checkBoxList) {
		String checkedFlags = "";
		for (int j = 0; j < checkBoxList.size(); j++) {
			if (checkBoxList.get(j).getValue()) {
				checkedFlags += checkBoxList.get(j).getName() + ",";
			}
		}
		if (!checkedFlags.equals(""))
			checkedFlags = checkedFlags.substring(0, checkedFlags.length() - 1);

		return checkedFlags;
	}

	/**
	 * init checkboxes for the given key/value sets
	 *
	 * @param hm
	 * @return checkbox list
	 */
	public static List<CheckBox> createCheckBoxList(LinkedHashMap<String, String> hm, FlowPanel parentPanel) {
		List<CheckBox> checkBoxList = new ArrayList<CheckBox>(hm.keySet().size());
		Iterator itHm = hm.entrySet().iterator();
		int i = 0;
		while (itHm.hasNext()) {
			Map.Entry pairs = (Map.Entry) itHm.next();
			CheckBox checkBox = new CheckBox((String) pairs.getValue());
			checkBox.setStyleName("checkBoxBtn");
			checkBox.setValue(true);
			checkBox.setName((String) pairs.getKey());
			checkBoxList.add(checkBox);
			parentPanel.add(checkBox);
		}

		return checkBoxList;
	}

	public static void hide(UIObject uiObj) {
		uiObj.getElement().getStyle().setProperty("display", "none");
	}

	public static void show(UIObject uiObj) {
		uiObj.getElement().getStyle().setProperty("display", "block");
	}

	public static void showHide(Boolean show, UIObject uiObj) {
		if (show) {
			ViewUtils.show(uiObj);
		} else {
			ViewUtils.hide(uiObj);
		}
	}

	public static boolean isAuthor(AutoBean<CurrentUserBean> currentUserBean, AutoBean<SnipBean> snipBean) {
		if (snipBean != null && snipBean.as() != null &&
				currentUserBean != null && currentUserBean.as() != null && currentUserBean.as().getName() != null)  {
			return currentUserBean.as().getName().equals(snipBean.as().getAuthor());
		}
		return false;
	}

	/**
	 *
	 * @param userName a correctly formatted user Name which is alphanumeric with spaces, must not be null
	 * @return the avatar name replacing spaces with underscores
	 */
	public static String createAvatarName (String userName){
		return userName.replace(" ","_");
	}
}

package com.therdl.client.view.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.html.Span;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Emotion;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;

/**
 * Common methods used by views
 */
public class ViewUtils {

	private static List<String> pieceMealEmotions;

	public static List<String> getPieceMealEmotions() {
		if (pieceMealEmotions == null) {
			pieceMealEmotions = new ArrayList<>();
			for (Emotion emo : Emotion.values()) {
				String[] emoShards = EmotionTranslator.getMessage(emo).split(",");
				pieceMealEmotions.addAll(Arrays.asList(emoShards));
			}
		}
		return pieceMealEmotions;
	}

	/**
	 * Gets the expires date string from the title
	 *
	 * @param currentUserBean the current user bean
	 * @param titleName       the title name to search for
	 * @return the date string or null if not found
	 */
	public static String getTitleExpiryDate(AutoBean<CurrentUserBean> currentUserBean, String titleName) {
		for (UserBean.TitleBean titleBean : currentUserBean.as().getTitles()) {
			if (titleName.equals(titleBean.getTitleName())) {
				return titleBean.getExpires();
			}
		}
		return null;
	}

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
		Widget source = (Widget)event.getSource();
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
			Map.Entry pairs = (Map.Entry)itHm.next();

			proposalTypeList.addItem((String)pairs.getValue());
			proposalTypeList.setValue(i, (String)pairs.getKey());
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
			Map.Entry pairs = (Map.Entry)itHm.next();

			proposalStateList.addItem((String)pairs.getValue());
			proposalStateList.setValue(i, (String)pairs.getKey());
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
			Map.Entry pairs = (Map.Entry)itHm.next();
			CheckBox checkBox = new CheckBox((String)pairs.getValue());
			checkBox.setStyleName("checkBoxBtn");
			checkBox.setValue(true);
			checkBox.setName((String)pairs.getKey());
			checkBoxList.add(checkBox);
			parentPanel.add(checkBox);
		}

		return checkBoxList;
	}

	public static Span buildEmoSpan(Emotion emo) {
		Span emoSpan = new Span();
		emoSpan.addStyleName("label");
		emoSpan.getElement().getStyle().setProperty("backgroundColor", EmotionTranslator.getBackground(emo));
		emoSpan.setText(EmotionTranslator.getMessage(emo));
		//negative emotions have white text
		if (!Emotion.isPositive(emo)) {
			emoSpan.getElement().getStyle().setProperty("color", "#ffffff");
		} else {
			emoSpan.getElement().getStyle().setProperty("color", "#000000");
		}
		return emoSpan;
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
				currentUserBean != null && currentUserBean.as() != null && currentUserBean.as().getName() != null) {
			return currentUserBean.as().getName().equals(snipBean.as().getAuthor());
		}
		return false;
	}

	/**
	 * @param userName a correctly formatted user Name which is alphanumeric with spaces, must not be null
	 * @return the avatar name replacing spaces with underscores
	 */
	public static String createAvatarName(String userName) {
		return userName.replace(" ", "_");
	}

	/**
	 * Builds a user's avatar image URL
	 *
	 * @param userName the user name
	 * @return the avatar image URL
	 */
	public static String getAvatarImageUrl(String userName) {
		return "https://s3.amazonaws.com/RDL_Avatars/" +
				ViewUtils.createAvatarName(userName) + ".jpg";
	}

	/**
	 * Used when you just want to retrieve one or a few specific results
	 *
	 * @param searchOptionsBean the empty bean
	 */
	public static void populateDefaultSearchOptions(AutoBean<SnipBean> searchOptionsBean) {
		searchOptionsBean.as().setPageIndex(0);
		searchOptionsBean.as().setSortOrder(1);
		searchOptionsBean.as().setSortField("author");
		searchOptionsBean.as().setReturnSnipContent(true);
	}

	/**
	 * Calculates weather or not to show the abuse button
	 *
	 * @param currentUser the current logged in user
	 * @param currentSnip the snip
	 * @return false if the abuse button should not be shown, true otherwise
	 */
	public static boolean showReportAbuseLogic(AutoBean<CurrentUserBean> currentUser, AutoBean<SnipBean> currentSnip) {
		//user not logged in
		if (currentUser == null || currentUser.as() == null || !currentUser.as().isAuth()) {
			return false;
		}
		if (currentSnip == null || currentSnip.as() == null || currentSnip.as().getIsAbuseReportedByUser() == null ||
				currentSnip.as().getIsAbuseReportedByUser() == 1) {
			return false;
		}
		return isAllowedAbuseAction(currentUser);

	}

	/**
	 * user must have at least 3 rep and be at least 1 week old or be an RDL supporter
	 *
	 * @param currentUser the current user
	 * @return
	 */
	public static boolean isAllowedAbuseAction(AutoBean<CurrentUserBean> currentUser) {
		//user must have at least 3 rep and be at least 1 week old or be an RDL supporter
		if (currentUser.as().getIsRDLSupporter()) {
			return true;
		}
		if ((currentUser.as().getRep() != null && currentUser.as().getRep() > 2) ||
				isOneWeekOld(currentUser.as().getDateCreated())) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * parses the date string and sees if the date is at least on week old
	 *
	 * @param date the string representation
	 * @return true if the date is one week old
	 */
	public static boolean isOneWeekOld(String date) {
		DateTimeFormat dtf = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN);
		long time = dtf.parse(date).getTime();
		//7 days have passed
		long sevenDays = (7 * 24 * 60 * 60 * 1000);
		if ((new Date().getTime() - time) > sevenDays) {
			return true;
		}
		return false;
	}

	/**
	 * parses the date string and sees if the date is before or after today
	 *
	 * @param date the date to parse
	 * @return true if the date has past
	 */
	public static boolean isExpired(String date) {
		DateTimeFormat dtf = DateTimeFormat.getFormat(RDLConstants.DATE_PATTERN);
		Date parsed = dtf.parse(date);
		return parsed.before(new Date());
	}

	/**
	 * Displays emotions in a given panel
	 *
	 * @param emoListPanel    the panel to display in
	 * @param currentSnipBean the current bean with emotions
	 */
	public static void displayEmotions(Panel emoListPanel, AutoBean<SnipBean> currentSnipBean) {
		emoListPanel.clear();
		if (currentSnipBean != null && currentSnipBean.as().getEmotions() != null) {
			for (String emoStr : currentSnipBean.as().getEmotions()) {
				Span span = ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr));
				span.setText(EmotionTranslator.getMessage(Emotion.valueOf(emoStr)));
				emoListPanel.add(span);
			}
		}
	}

	/**
	 * creates category list for snips
	 */
	public static void createCategoryList(ListBox categoryList) {
		int i = 0;
		for (CoreCategory item : CoreCategory.values()) {
			categoryList.addItem(item.getShortName());
			categoryList.setValue(i, item.getShortName());
			i++;
		}
	}
}

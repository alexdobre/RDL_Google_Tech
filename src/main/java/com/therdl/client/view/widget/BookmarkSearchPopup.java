package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.SnipBean;

/**
 * Bookmark search popup, extends gwt PopupPanel panel
 * contains TextBox getLinkTextBox GUI element to show constructed url
 */
public class BookmarkSearchPopup extends PopupPanel {
	interface BookmarkSearchPopupUiBinder extends UiBinder<HTMLPanel, BookmarkSearchPopup> {
	}

	private static BookmarkSearchPopupUiBinder ourUiBinder = GWT.create(BookmarkSearchPopupUiBinder.class);

	@UiField
	TextBox getLinkTextBox;
	SearchFilterWidget searchFilterWidget;
	private AutoBean<SnipBean> currentSearchOptionsBean;

	/**
	 * constructor BookmarkSearchPopup
	 * forms url and sets to the text box
	 * url has the following format example: http://localhost:8080/#snips:title=aaa:coreCat=Compatibility:author=serine
	 *
	 * @param searchFilterWidget
	 */
	public BookmarkSearchPopup(SearchFilterWidget searchFilterWidget,AutoBean<SnipBean> currentSearchOptionsBean) {
		super(true);
		add(ourUiBinder.createAndBindUi(this));
		this.setStyleName("bookmarkSearchPopup");
		setModal(true);

		this.searchFilterWidget = searchFilterWidget;
		this.currentSearchOptionsBean = currentSearchOptionsBean;

		StringBuilder url = new StringBuilder() ;

		if (formURl().length() != 0) {
			url.append(':').append(formURl().toString().replace(" ", "+")).append("page=").
					append(currentSearchOptionsBean.as().getPageIndex());
		}

		String moduleToken = "";

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			moduleToken = RDLConstants.Tokens.SNIPS;
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			moduleToken = RDLConstants.Tokens.STORIES;
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			moduleToken = RDLConstants.Tokens.IMPROVEMENTS;

		getLinkTextBox.setText(GWT.getHostPageBaseURL() + "#" + moduleToken + url);
	}

	/**
	 * forms bookmark url from the values of the search widget's gui elements
	 *
	 * @return constructed url as a StringBuilder object
	 */
	public StringBuilder formURl() {
		StringBuilder stringBuilder = new StringBuilder();

		if (!searchFilterWidget.title.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.TITLE + "=" + searchFilterWidget.title.getText() + ":");
		}

		String selCategories = ViewUtils.getSelectedItems(searchFilterWidget.categoryList);
		if (!selCategories.equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.CORE_CAT + "=" + selCategories + ":");
		}

		if (!searchFilterWidget.posRef.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.POS_REF + "=" + searchFilterWidget.posRef.getText() + ":");
		}

		if (!searchFilterWidget.neutralRef.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.NEUTRAL_REF + "=" + searchFilterWidget.neutralRef.getText() + ":");
		}

		if (!searchFilterWidget.negativeRef.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.NEGATIVE_REF + "=" + searchFilterWidget.negativeRef.getText() + ":");
		}

		if (!searchFilterWidget.postCount.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.POSTS + "=" + searchFilterWidget.postCount.getText() + ":");
		}

		if (!searchFilterWidget.snipRep.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.REP + "=" + searchFilterWidget.snipRep.getText() + ":");
		}

		if (!searchFilterWidget.author.getText().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.AUTHOR + "=" + searchFilterWidget.author.getText() + ":");
		}

		if (!searchFilterWidget.dateFilterWidget.getDateFrom().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.DATE_FROM + "=" + searchFilterWidget.dateFilterWidget.getDateFrom() + ":");
		}

		if (!searchFilterWidget.dateFilterWidget.getDateTo().equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.DATE_TO + "=" + searchFilterWidget.dateFilterWidget.getDateTo() + ":");
		}

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS) && !searchFilterWidget.getCheckedSnipTypes().equals(""))
			stringBuilder.append(RDLConstants.BookmarkSearch.SNIP_TYPE + "=" + searchFilterWidget.getCheckedSnipTypes() + ":");

		String selProposalTypes = ViewUtils.getSelectedItems(searchFilterWidget.proposalTypeList);
		if (!selProposalTypes.equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.PROPOSAL_TYPE + "=" + selProposalTypes + ":");
		}

		String selProposalStates = ViewUtils.getSelectedItems(searchFilterWidget.proposalStateList);
		if (!selProposalStates.equals("")) {
			stringBuilder.append(RDLConstants.BookmarkSearch.PROPOSAL_STATE + "=" + selProposalStates + ":");
		}

		stringBuilder.append(RDLConstants.BookmarkSearch.SORT_FIELD + "=" + searchFilterWidget.getSortField() + ":");
		stringBuilder.append(RDLConstants.BookmarkSearch.SORT_ORDER + "=" + searchFilterWidget.getSortOrder() + ":");

		if (stringBuilder.length() != 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		return stringBuilder;
	}

	public SearchFilterWidget getSearchFilterWidget() {
		return searchFilterWidget;
	}

	public void setSearchFilterWidget(SearchFilterWidget searchFilterWidget) {
		this.searchFilterWidget = searchFilterWidget;
	}

	public AutoBean<SnipBean> getCurrentSearchOptionsBean() {
		return currentSearchOptionsBean;
	}

	public void setCurrentSearchOptionsBean(AutoBean<SnipBean> currentSearchOptionsBean) {
		this.currentSearchOptionsBean = currentSearchOptionsBean;
	}
}
package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.handler.LoginHandler;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.BecomeRdlSupporterEvent;
import com.therdl.shared.events.GuiEventBus;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Legend;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.*;

/**
 * GWT widget class for search filter
 * creates GUI elements and handlers for them
 */

public class SearchFilterWidget extends AbstractSearchFilter {
	interface SearchFilterWidgetUiBinder extends UiBinder<Widget, SearchFilterWidget> {
	}

	private static SearchFilterWidgetUiBinder uiBinder = GWT.create(SearchFilterWidgetUiBinder.class);

	@UiField
	Button submit, getLinkBtn, createNewButton;

	@UiField
	TextBox pledgesCount, countersCount, posRef, neutralRef, negativeRef, postCount, snipRep, author;

	@UiField
	ListBox proposalTypeList, proposalStateList;

	@UiField
	org.gwtbootstrap3.client.ui.ListBox categoryList;

	@UiField
	FormGroup typeFormGroup, proposalTypeFormGroup, proposalStateFormGroup, categoryFormGroup, pledgesFormGroup, countersFormGroup, refFormGroup, postsFormGroup, repFormGroup;

	@UiField
	Legend filterLabel;

	@UiField
	InsertPanel.ForIsWidget repPanel, authorPanel, datePanel, posRefPanel, neutralRefPanel, negativeRefPanel, postPanel, pledgesPanel, countersPanel, typePanel, refPanel;

	Image selectedArrow;

	// default sort order is descending by creation date
	private int sortOrder = -1;
	private String sortField = RDLConstants.SnipFields.CREATION_DATE;

	private CheckBox[] checkBoxArray = new CheckBox[4];
	private LinkedHashMap<String, String> snipTypeHm = new LinkedHashMap();

	public String getSortField() {
		return sortField;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	SearchView view;

	private String editPageToken;
	private Beanery beanery = GWT.create(Beanery.class);

	public SearchFilterWidget(SearchView snipSearchView) {
		initWidget(uiBinder.createAndBindUi(this));
		this.view = snipSearchView;

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			editPageToken = RDLConstants.Tokens.SNIP_EDIT;

			createTypeFilter();

			hideUnusedItems();

			filterLabel.setText(RDL.i18n.filterSnips());

			ViewUtils.createCategoryList(categoryList);

		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			editPageToken = RDLConstants.Tokens.THREAD_EDIT;

			hideUnusedItems();

			filterLabel.setText(RDL.i18n.filterThreads());

			ViewUtils.createCategoryList(categoryList);
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			editPageToken = RDLConstants.Tokens.PROPOSAL_EDIT;

			hideUnusedItems();

			filterLabel.setText(RDL.i18n.filterProposals());

			ViewUtils.createProposalTypeList(proposalTypeList);
			ViewUtils.createProposalStateList(proposalStateList);

		}

		createSortArrows();
	}

	/**
	 * create checkbox group for snip type (snip, fastcap, material, habit)
	 */
	private void createTypeFilter() {
		snipTypeHm.put(RDLConstants.SnipType.SNIP, RDL.i18n.snip());
		snipTypeHm.put(RDLConstants.SnipType.FAST_CAP, RDL.i18n.fastCap());
		snipTypeHm.put(RDLConstants.SnipType.MATERIAL, RDL.i18n.material());
		snipTypeHm.put(RDLConstants.SnipType.HABIT, RDL.i18n.habit());

		Iterator itHm = snipTypeHm.entrySet().iterator();
		int i = 0;
		while (itHm.hasNext()) {
			Map.Entry pairs = (Map.Entry)itHm.next();

			checkBoxArray[i] = new CheckBox((String)pairs.getValue());
			checkBoxArray[i].setValue(true);
			checkBoxArray[i].setStyleName("checkBoxBtn");
			if (pairs.getKey().equals(RDLConstants.SnipType.FAST_CAP)) {
				checkBoxArray[i].getElement().getStyle().setProperty("marginLeft", "36px");
			}

			typePanel.add(checkBoxArray[i]);
			i++;
		}

	}

	/**
	 * sets sort arrows for some search fields (views, rep, pos/neut/neg ref, author, date), down for descending order, up for ascending order
	 * default order is descending order by creation date
	 */
	private void createSortArrows() {
		InsertPanel.ForIsWidget[] flowPanels = { repPanel, authorPanel, datePanel, posRefPanel, neutralRefPanel,
				negativeRefPanel, postPanel, pledgesPanel, countersPanel };
		String[] keyNames = { RDLConstants.SnipFields.REP,
				RDLConstants.SnipFields.AUTHOR, RDLConstants.SnipFields.CREATION_DATE,
				RDLConstants.SnipFields.POS_REF, RDLConstants.SnipFields.NEUTRAL_REF,
				RDLConstants.SnipFields.NEGATIVE_REF, RDLConstants.SnipFields.POSTS,
				RDLConstants.SnipFields.PLEDGES, RDLConstants.SnipFields.COUNTERS
		};

		for (int i = 0; i < flowPanels.length; i++) {
			final String keyName = keyNames[i];
			FlowPanel arrowPanel = new FlowPanel();
			Image imgUp = new Image(Resources.INSTANCE.arrowUpGrey().getSafeUri().asString());
			imgUp.setWidth("15px");
			imgUp.setStyleName("arrowImg");
			imgUp.setTitle(RDL.i18n.sortAsc());
			arrowPanel.add(imgUp);

			imgUp.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {
					if (sortOrder == 1)
						selectedArrow.setUrl(Resources.INSTANCE.arrowUpGrey().getSafeUri().asString());
					else
						selectedArrow.setUrl(Resources.INSTANCE.arrowDownGrey().getSafeUri().asString());

					selectedArrow = (Image)clickEvent.getSource();
					sortOrder = 1;
					sortField = keyName;
					selectedArrow.setUrl(Resources.INSTANCE.arrowUpGreen().getSafeUri().asString());
				}
			});

			Image imgDown = new Image(Resources.INSTANCE.arrowDownGrey().getSafeUri().asString());
			imgDown.setWidth("15px");
			imgDown.setTitle(RDL.i18n.sortDesc());
			imgDown.setStyleName("arrowImg");
			arrowPanel.add(imgDown);

			imgDown.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {
					if (sortOrder == 1)
						selectedArrow.setUrl(Resources.INSTANCE.arrowUpGrey().getSafeUri().asString());
					else
						selectedArrow.setUrl(Resources.INSTANCE.arrowDownGrey().getSafeUri().asString());

					selectedArrow = (Image)clickEvent.getSource();
					sortOrder = -1;
					sortField = keyName;
					selectedArrow.setUrl(Resources.INSTANCE.arrowDownGreen().getSafeUri().asString());
				}
			});

			arrowPanel.getElement().getStyle().setProperty("float", "right");
			arrowPanel.getElement().getStyle().setProperty("marginTop", "2px");
			flowPanels[i].add(arrowPanel);

			if (flowPanels[i].equals(datePanel)) {
				selectedArrow = imgDown;
				selectedArrow.setUrl(Resources.INSTANCE.arrowDownGreen().getSafeUri().asString());

			}
		}

	}

	private void hideUnusedItems() {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			ViewUtils.hide(postsFormGroup);
			ViewUtils.hide(repFormGroup);
			hideProposalItems();

		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			ViewUtils.hide(typeFormGroup);
			ViewUtils.hide(refFormGroup);
			hideProposalItems();

		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			ViewUtils.hide(typeFormGroup);
			ViewUtils.hide(refFormGroup);
			ViewUtils.hide(repFormGroup);
			ViewUtils.hide(postsFormGroup);
			ViewUtils.hide(categoryFormGroup);
		}
	}

	private void hideProposalItems() {
		ViewUtils.hide(proposalStateFormGroup);
		ViewUtils.hide(proposalTypeFormGroup);
		ViewUtils.hide(pledgesFormGroup);
		ViewUtils.hide(countersFormGroup);
	}

	/**
	 * click handle for link button for bookmark search
	 * bookmark search popup is opened in the button side
	 *
	 * @param event
	 */

	@UiHandler("getLinkBtn")
	public void onLinkBtnClicked(ClickEvent event) {
		if (view.getCurrentSearchOptionsBean() == null) {
			formSearchOptionBean();
		}
		BookmarkSearchPopup bookmarkSearchPopup = new BookmarkSearchPopup(this, view.getCurrentSearchOptionsBean());
		int x = getLinkBtn.getAbsoluteLeft();
		int y = getLinkBtn.getAbsoluteTop();
		bookmarkSearchPopup.setPopupPosition(x + getLinkBtn.getOffsetWidth(), y);
		bookmarkSearchPopup.show();
	}

	/**
	 * construct snip bean object from the popup fields
	 * at the end when there is no any search option gets initial list, else do the filtered search
	 *
	 * @param event
	 */

	@UiHandler("submit")
	public void onSubmit(ClickEvent event) {
		formSearchOptionBean();
		view.doFilterSearch();
	}

	@UiHandler(value = { "title", "pledgesCount", "countersCount", "posRef", "neutralRef", "negativeRef", "postCount",
			"snipRep", "author" })
	public void onPassEnter(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			onSubmit(null);
		}
	}

	public void populateSearchOptions() {
		AutoBean<SnipBean> searchOptionsBean = view.getCurrentSearchOptionsBean();
		title.setText(searchOptionsBean.as().getTitle());
		author.setText(searchOptionsBean.as().getAuthor());
		dateFilterWidget.setDateFrom(searchOptionsBean.as().getDateFrom());
		dateFilterWidget.setDateTo(searchOptionsBean.as().getDateTo());

		if (!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			//String categories = ViewUtils.getSelectedItems(categoryList);
			//TODO populate categories list
		} else {
			//String proposalTypes = ViewUtils.getSelectedItems(proposalTypeList);
			//TODO populate proposalTypeList

			String proposalStates = ViewUtils.getSelectedItems(proposalStateList);
			//TODO populate proposalStateList
		}
		if (searchOptionsBean.as().getPosRef() != null)
			posRef.setText(searchOptionsBean.as().getPosRef().toString());
		if (searchOptionsBean.as().getNeutralRef() != null)
			neutralRef.setText(searchOptionsBean.as().getNeutralRef().toString());
		if (searchOptionsBean.as().getNegativeRef() != null)
			negativeRef.setText(searchOptionsBean.as().getNegativeRef().toString());
		if (searchOptionsBean.as().getPosts() != null)
			postCount.setText(searchOptionsBean.as().getPosts().toString());
		if (searchOptionsBean.as().getPledges() != null)
			pledgesCount.setText(searchOptionsBean.as().getPledges().toString());
		if (searchOptionsBean.as().getCounters() != null)
			countersCount.setText(searchOptionsBean.as().getCounters().toString());
		if (searchOptionsBean.as().getRep() != null)
			snipRep.setText(searchOptionsBean.as().getRep().toString());

		//TODO populate sort order
		//		searchOptionsBean.as().setSortOrder(sortOrder);
		//		searchOptionsBean.as().setSortField(sortField);
	}

	/**
	 * forms search option bean from filter form elements
	 *
	 * @return search option bean as SnipBean object
	 */
	private AutoBean<SnipBean> formSearchOptionBean() {
		AutoBean<SnipBean> searchOptionsBean = view.getCurrentSearchOptionsBean();
		String titleText = title.getText();
		if (!titleText.equals("")) {
			searchOptionsBean.as().setTitle(titleText);
		} else {
			searchOptionsBean.as().setTitle(null);
		}

		String authorText = author.getText();
		if (!authorText.equals("")) {
			searchOptionsBean.as().setAuthor(authorText);
		} else {
			searchOptionsBean.as().setAuthor(null);
		}

		String dateFromText = dateFilterWidget.getDateFrom();
		if (!dateFromText.equals("")) {
			searchOptionsBean.as().setDateFrom(dateFromText);
		} else {
			searchOptionsBean.as().setDateFrom(null);
		}

		String dateToText = dateFilterWidget.getDateTo();
		if (!dateToText.equals("")) {
			searchOptionsBean.as().setDateTo(dateToText);
		} else {
			searchOptionsBean.as().setDateTo(null);
		}

		if (!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			String categories = ViewUtils.getSelectedItems(categoryList);
			if (!categories.equals("")) {
				searchOptionsBean.as().setCoreCat(categories);
			} else {
				searchOptionsBean.as().setCoreCat(null);
			}
		} else {
			String proposalTypes = ViewUtils.getSelectedItems(proposalTypeList);
			if (!proposalTypes.equals("")) {
				searchOptionsBean.as().setProposalType(proposalTypes);
			} else {
				searchOptionsBean.as().setProposalType(null);
			}

			String proposalStates = ViewUtils.getSelectedItems(proposalStateList);
			if (!proposalStates.equals("")) {
				searchOptionsBean.as().setProposalState(proposalStates);
			} else {
				searchOptionsBean.as().setProposalState(null);
			}
		}

		String posRefText = posRef.getText();
		if (!posRefText.equals("")) {
			searchOptionsBean.as().setPosRef(Integer.parseInt(posRefText));
		} else {
			searchOptionsBean.as().setPosRef(null);
		}

		String neutralRefText = neutralRef.getText();
		if (!neutralRefText.equals("")) {
			searchOptionsBean.as().setNeutralRef(Integer.parseInt(neutralRefText));
		} else {
			searchOptionsBean.as().setNeutralRef(null);
		}

		String negativeRefText = negativeRef.getText();
		if (!negativeRefText.equals("")) {
			searchOptionsBean.as().setNegativeRef(Integer.parseInt(negativeRefText));
		} else {
			searchOptionsBean.as().setNegativeRef(null);
		}

		String postsText = postCount.getText();
		if (!postsText.equals("")) {
			searchOptionsBean.as().setPosts(Integer.parseInt(postsText));
		} else {
			searchOptionsBean.as().setPosts(null);
		}

		String pledgesText = pledgesCount.getText();
		if (!pledgesText.equals("")) {
			searchOptionsBean.as().setPledges(Integer.parseInt(pledgesText));
		} else {
			searchOptionsBean.as().setPledges(null);
		}

		String countersText = countersCount.getText();
		if (!countersText.equals("")) {
			searchOptionsBean.as().setCounters(Integer.parseInt(countersText));
		} else {
			searchOptionsBean.as().setCounters(null);
		}

		String snipRepText = snipRep.getText();
		if (!snipRepText.equals("")) {
			searchOptionsBean.as().setRep(Integer.parseInt(snipRepText));
		} else {
			searchOptionsBean.as().setRep(null);
		}

		searchOptionsBean.as().setSortOrder(sortOrder);
		searchOptionsBean.as().setSortField(sortField);

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			searchOptionsBean.as().setSnipType(getCheckedSnipTypes());
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			searchOptionsBean.as().setSnipType(RDLConstants.SnipType.THREAD);
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			searchOptionsBean.as().setSnipType(RDLConstants.SnipType.PROPOSAL);

		return searchOptionsBean;
	}

	/**
	 * checks which snip type checkboxes are checked
	 *
	 * @return returns snip type values separated by comma
	 */
	public String getCheckedSnipTypes() {
		List keys = new ArrayList(snipTypeHm.keySet());
		String checkedFlags = "";
		for (int j = 0; j < checkBoxArray.length; j++) {
			if (checkBoxArray[j].getValue()) {
				checkedFlags += keys.get(j) + ",";
			}
		}
		if (!checkedFlags.equals(""))
			checkedFlags = checkedFlags.substring(0, checkedFlags.length() - 1);

		return checkedFlags;
	}

	/**
	 * handler for the create new button
	 * opens create/edit snip view
	 *
	 * @param event
	 */
	@UiHandler("createNewButton") void onCreateNewButtonClick(ClickEvent event) {
		if (view.getPresenter().getController().getCurrentUserBean().as().isAuth()) {
			createNewBtnHandler(editPageToken, view.getCurrentUserBean());
		} else {
			final String pageToRedirect = editPageToken;
			view.getAppMenu().showLoginPopUp(new LoginHandler() {

				@Override
				public void onSuccess(AutoBean<CurrentUserBean> currentUserBean) {
					createNewBtnHandler(pageToRedirect, currentUserBean);

				}
			});
		}
	}

	private void createNewBtnHandler(String editPageToken, AutoBean<CurrentUserBean> userBean) {
		if ((Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS) ||
				Global.moduleName.equals(RDLConstants.Modules.IDEAS)) && !userBean.as().getIsRDLSupporter()) {
			GuiEventBus.EVENT_BUS
					.fireEvent(new BecomeRdlSupporterEvent(RDL.getI18n().rdlSupporterPopupTitleLeaveRef()));
		} else {
			History.newItem(editPageToken);
		}
	}

	/**
	 * sets filter's fields by given bean
	 *
	 * @param searchOptionsBean
	 */

	public void setSearchFilterFields(AutoBean<SnipBean> searchOptionsBean) {
		title.setText(searchOptionsBean.as().getTitle() != null ? searchOptionsBean.as().getTitle() : "");
		author.setText(searchOptionsBean.as().getAuthor() != null ? searchOptionsBean.as().getAuthor() : "");
		posRef.setText(searchOptionsBean.as().getPosRef() != null ? searchOptionsBean.as().getPosRef() + "" : "");
		neutralRef.setText(
				searchOptionsBean.as().getNeutralRef() != null ? searchOptionsBean.as().getNeutralRef() + "" : "");
		negativeRef.setText(
				searchOptionsBean.as().getNegativeRef() != null ? searchOptionsBean.as().getNegativeRef() + "" : "");
		postCount.setText(searchOptionsBean.as().getPosts() != null ? searchOptionsBean.as().getPosts() + "" : "");
		pledgesCount
				.setText(searchOptionsBean.as().getPledges() != null ? searchOptionsBean.as().getPledges() + "" : "");
		countersCount
				.setText(searchOptionsBean.as().getCounters() != null ? searchOptionsBean.as().getCounters() + "" : "");
		snipRep.setText(searchOptionsBean.as().getRep() != null ? searchOptionsBean.as().getRep() + "" : "");

		dateFilterWidget.setDateFrom(
				searchOptionsBean.as().getDateFrom() != null ? searchOptionsBean.as().getDateFrom() + "" : "");
		dateFilterWidget
				.setDateTo(searchOptionsBean.as().getDateTo() != null ? searchOptionsBean.as().getDateTo() + "" : "");

		if (searchOptionsBean.as().getCoreCat() != null) {
			for (int i = 0; i < categoryList.getItemCount(); i++) {
				if (categoryList.getItemText(i).equals(searchOptionsBean.as().getCoreCat())) {
					categoryList.setSelectedIndex(i);
					break;
				}
			}
		}

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			if (searchOptionsBean.as().getSnipType() != null && searchOptionsBean.as().getSnipType() != "") {
				String[] snipTypes = searchOptionsBean.as().getSnipType().split(",");
				for (int j = 0; j < checkBoxArray.length; j++) {
					checkBoxArray[j].setValue(false);
					for (int i = 0; i < snipTypes.length; i++) {
						if (checkBoxArray[j].getText().equals(snipTypeHm.get(snipTypes[i])))
							checkBoxArray[j].setValue(true);

					}
				}
			} else {
				for (int j = 0; j < checkBoxArray.length; j++) {
					checkBoxArray[j].setValue(true);
				}
			}
		}
	}

}

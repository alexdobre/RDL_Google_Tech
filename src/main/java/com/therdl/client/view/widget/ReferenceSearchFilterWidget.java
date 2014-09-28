package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.impl.SnipViewImpl;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Legend;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * gwt widget class for reference search filter, used in snip view page
 */
public class ReferenceSearchFilterWidget extends Composite {

	interface ReferenceSearchFilterWidgetUiBinder extends UiBinder<Widget, ReferenceSearchFilterWidget> {
	}

	private static ReferenceSearchFilterWidgetUiBinder ourUiBinder = GWT.create(ReferenceSearchFilterWidgetUiBinder.class);

	@UiField
	FlowPanel refTypePanel, repPanel, authorNamePanel, datePanel, proposalCheckboxPanel;

	@UiField
	TextBox rep, authorName;

	@UiField
	DateFilterWidget dateFilterWidget;

	@UiField
	FormGroup typeGroup, proposalGroup;

	@UiField
	Legend filterLabel;

	Image selectedArrow;

	// default sort order is descending by creation date
	private int sortOrder = -1;
	private String sortField = RDLConstants.SnipFields.CREATION_DATE;

	private List<CheckBox> checkBoxList;
	private List<CheckBox> checkBoxListProp;

	private LinkedHashMap<String, String> proposalHm = new LinkedHashMap();
	private LinkedHashMap<String, String> referenceTypeHm = new LinkedHashMap();

	private Beanery beanery = GWT.create(Beanery.class);
	private final SnipViewImpl view;

	public ReferenceSearchFilterWidget(SnipViewImpl viewArg) {
		initWidget(ourUiBinder.createAndBindUi(this));
		this.view = viewArg;

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			initRefTypeCheckboxes();
		} else {
			ViewUtils.hide(typeGroup);
			filterLabel.setText(RDL.i18n.filterPosts());
		}
		if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			initProposalCheckBoxes();
		} else {
			ViewUtils.hide(proposalGroup);
		}
		createSortArrows();
	}

	/**
	 * creates checkboxes for reference types
	 */
	private void initRefTypeCheckboxes() {
		referenceTypeHm.put(RDLConstants.ReferenceType.POSITIVE, RDL.i18n.positiveShort());
		referenceTypeHm.put(RDLConstants.ReferenceType.NEUTRAL, RDL.i18n.neutral());
		referenceTypeHm.put(RDLConstants.ReferenceType.NEGATIVE, RDL.i18n.negativeShort());

		checkBoxList = ViewUtils.createCheckBoxList(referenceTypeHm, refTypePanel);
	}

	/**
	 * creates check boxes to filter pledges or counters (by author title and proposal type)
	 */
	private void initProposalCheckBoxes() {
		proposalHm.put("dev", RDL.i18n.dev());
		proposalHm.put("user", RDL.i18n.user());
		proposalHm.put("pledge", RDL.i18n.pledge());
		proposalHm.put("counter", RDL.i18n.counter());

		checkBoxListProp = ViewUtils.createCheckBoxList(proposalHm, proposalCheckboxPanel);
	}

	/**
	 * sets sort arrows for some search fields (views, rep, pos/neut/neg ref, author, date), down for descending order, up for ascending order
	 * default order is descending order by creation date
	 */
	private void createSortArrows() {
		FlowPanel[] flowPanels = {repPanel, authorNamePanel, datePanel};
		String[] keyNames = {RDLConstants.SnipFields.REP, RDLConstants.SnipFields.AUTHOR, RDLConstants.SnipFields.CREATION_DATE};

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

					selectedArrow = (Image) clickEvent.getSource();
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

					selectedArrow = (Image) clickEvent.getSource();
					sortOrder = -1;
					sortField = keyName;
					selectedArrow.setUrl(Resources.INSTANCE.arrowDownGreen().getSafeUri().asString());
				}
			});

			arrowPanel.getElement().getStyle().setProperty("float", "left");
			arrowPanel.getElement().getStyle().setProperty("marginTop", "2px");
			flowPanels[i].add(arrowPanel);

			if (flowPanels[i].equals(datePanel)) {
				selectedArrow = imgDown;
				selectedArrow.setUrl(Resources.INSTANCE.arrowDownGreen().getSafeUri().asString());

			}
		}
	}

	@UiHandler("refFilter")
	public void filterReferences(ClickEvent event) {
		view.setSearchOptionsBean(formSearchOptionsBean());
		view.getSearchOptionsBean().as().setPageIndex(0);
		view.getPresenter().populateReplies(view.getSearchOptionsBean());
	}

	public void populateSearchOptions() {
		AutoBean<SnipBean> searchOptionsBean = view.getSearchOptionsBean();
		if (searchOptionsBean.as().getRep() != null) rep.setText(searchOptionsBean.as().getRep().toString());
		authorName.setText(searchOptionsBean.as().getAuthor());
		dateFilterWidget.setDateFrom(searchOptionsBean.as().getDateFrom());
		dateFilterWidget.setDateTo(searchOptionsBean.as().getDateTo());


		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			searchOptionsBean.as().setReferenceType(ViewUtils.getCheckedFlags(checkBoxList));
			//TODO populate reference types
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			//TODO populate check boxes
		}
		//TODO populate sort order
		//		searchOptionsBean.as().setSortOrder(sortOrder);
		//		searchOptionsBean.as().setSortField(sortField);
	}

	/**
	 * forms search option bean from filter form elements
	 *
	 * @return search option bean as SnipBean object
	 */
	private AutoBean<SnipBean> formSearchOptionsBean() {
		AutoBean<SnipBean> searchOptionsBean = view.getSearchOptionsBean();

		if (!rep.getText().equals(""))
			searchOptionsBean.as().setRep(Integer.parseInt(rep.getText()));
		else searchOptionsBean.as().setRep(null);

		if (!authorName.getText().equals(""))
			searchOptionsBean.as().setAuthor(authorName.getText());
		else searchOptionsBean.as().setAuthor(null);

		if (!dateFilterWidget.getDateFrom().equals(""))
			searchOptionsBean.as().setDateFrom(dateFilterWidget.getDateFrom());
		else searchOptionsBean.as().setDateFrom(null);

		if (!dateFilterWidget.getDateTo().equals(""))
			searchOptionsBean.as().setDateTo(dateFilterWidget.getDateTo());
		else searchOptionsBean.as().setDateTo(null);

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			searchOptionsBean.as().setReferenceType(ViewUtils.getCheckedFlags(checkBoxList));
			searchOptionsBean.as().setSnipType(RDLConstants.SnipType.REFERENCE);

		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			searchOptionsBean.as().setSnipType(RDLConstants.SnipType.POST);
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {

			if (checkBoxListProp.get(0).getValue() && !checkBoxListProp.get(1).getValue()) {
				searchOptionsBean.as().setAuthorTitle(RDLConstants.UserTitle.RDL_DEV);
			} else if (!checkBoxListProp.get(0).getValue() && checkBoxListProp.get(1).getValue()) {
				searchOptionsBean.as().setAuthorTitle(RDLConstants.UserTitle.RDL_USER);
			}

			String checkedTypes = "";
			if (checkBoxListProp.get(2).getValue())
				checkedTypes += RDLConstants.SnipType.PLEDGE + ",";
			if (checkBoxListProp.get(3).getValue())
				checkedTypes += RDLConstants.SnipType.COUNTER + ",";

			if (!checkedTypes.equals(""))
				checkedTypes = checkedTypes.substring(0, checkedTypes.length() - 1);

			searchOptionsBean.as().setSnipType(checkedTypes);

		}
		searchOptionsBean.as().setSortOrder(sortOrder);
		searchOptionsBean.as().setSortField(sortField);

		return searchOptionsBean;
	}
}
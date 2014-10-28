package com.therdl.client.view.widget.runtized;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.handler.LoginHandler;
import com.therdl.client.presenter.runt.ServiceFilterRunt;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.BecomeRdlSupporterEvent;
import com.therdl.shared.events.GuiEventBus;

/**
 * The services search filter
 */
public class ServicesFilter extends Composite {

	private ServiceFilterRunt serviceFilterRunt;

	interface ServicesFilterWidgetUiBinder extends UiBinder<Widget, ServicesFilter> {
	}

	private static ServicesFilterWidgetUiBinder uiBinder = GWT.create(ServicesFilterWidgetUiBinder.class);

	@UiField
	TextBox author, posRef, neutralRef, negativeRef;
	@UiField
	DateFromTo dateFromTo;
	@UiField
	Button filter, createNewButton;
	@UiField
	ListBox categoryList;
	@UiField
	SortBit authorSortBit, dateSortBit, posRefSortBit, neutrRefSortBit, negRefSortBit;

	private AutoBean<SnipBean> searchOptions;

	public ServicesFilter() {
		initWidget(uiBinder.createAndBindUi(this));
		ViewUtils.createCategoryList(categoryList);
	}

	public void populate(AutoBean<SnipBean> searchOptionsBean) {
		this.searchOptions = searchOptionsBean;
	}

	/**
	 * handler for the create new button
	 * opens create/edit snip view
	 *
	 * @param event
	 */
	@UiHandler("createNewButton")
	void onCreateNewButtonClick(ClickEvent event) {
		AutoBean<CurrentUserBean> currentUserBean = ViewUtils.getCurrentUserBean();
		if (currentUserBean != null && currentUserBean.as().isAuth()) {
			createNewBtnHandler(currentUserBean);
		} else {
			WidgetHolder.getHolder().getController().getAppMenu().showLoginPopUp(new LoginHandler() {

				@Override
				public void onSuccess(AutoBean<CurrentUserBean> currentUserBean) {
					createNewBtnHandler(currentUserBean);
				}
			});
		}
	}

	@UiHandler("filter")
	public void onSubmit(ClickEvent event) {
		serviceFilterRunt.filterSearch();
	}

	private void createNewBtnHandler(AutoBean<CurrentUserBean> userBean) {
		if (!userBean.as().getIsRDLSupporter()) {
			GuiEventBus.EVENT_BUS.fireEvent(new BecomeRdlSupporterEvent(RDL.getI18n().rdlSupporterPopupTitleLeaveRef()));
		} else {
			History.newItem(RDLConstants.Tokens.SERVICE_EDIT);
		}
	}

	public void setServiceFilterRunt(ServiceFilterRunt serviceFilterRunt) {
		this.serviceFilterRunt = serviceFilterRunt;
	}

	public AutoBean<SnipBean> getSearchOptions() {
		return searchOptions;
	}

	public TextBox getAuthor() {
		return author;
	}

	public TextBox getPosRef() {
		return posRef;
	}

	public TextBox getNeutralRef() {
		return neutralRef;
	}

	public TextBox getNegativeRef() {
		return negativeRef;
	}

	public DateFromTo getDateFromTo() {
		return dateFromTo;
	}

	public SortBit getAuthorSortBit() {
		return authorSortBit;
	}

	public SortBit getDateSortBit() {
		return dateSortBit;
	}

	public SortBit getPosRefSortBit() {
		return posRefSortBit;
	}

	public SortBit getNeutrRefSortBit() {
		return neutrRefSortBit;
	}

	public SortBit getNegRefSortBit() {
		return negRefSortBit;
	}

	public ListBox getCategoryList() {
		return categoryList;
	}
}

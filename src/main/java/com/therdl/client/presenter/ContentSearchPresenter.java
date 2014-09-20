package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.view.ContentSearchView;
import com.therdl.shared.beans.CurrentUserBean;

/**
 * Does the user search for site content
 */
public class ContentSearchPresenter extends RdlAbstractPresenter<ContentSearchView> implements ContentSearchView.Presenter {

	private String searchStr;

	public ContentSearchPresenter(ContentSearchView contentSearchView, AppController controller, String searchStr) {
		super(controller);
		this.view = contentSearchView;
		this.view.setPresenter(this);
		this.searchStr = searchStr;
	}

	/**
	 * standard runtime method for MVP architecture
	 *
	 * @param container       the view container
	 * @param currentUserBean the user state bean, mainly used for authorisation
	 *                        and to update the menu
	 */
	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		checkLogin();
		container.clear();
		getController().getAppMenu().getSearchSiteTextBox().setText(searchStr);
		view.getResultsContainer().clear();
		view.getResultsContainer().add(buildResultsFrame(searchStr));
		container.add(view.asWidget());
	}

	/**
	 * Shows site content search results
	 */
	public Frame buildResultsFrame(String searchStr) {
		Frame resultsFrame = new Frame();
		resultsFrame.setUrl(GWT.getModuleBaseURL() + "gcse?q=" + searchStr);
		resultsFrame.addStyleName("searchResultsFrame");
		return resultsFrame;
	}
}

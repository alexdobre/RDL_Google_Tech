package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.SearchView;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;

/**
 * SnipSearchPresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * this class will be extended to encapsulate all the snip related data in and out of the client
 * to be used for client to view a snip from the snip search view
 *
 * @ SearchView  searchView this presenter GUI component
 * @ Beanery  beanery the bean factory see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ String currentSnipId  used to retrieve the users correct snip
 * @ AutoBean<SnipBean>  currentBean determines the authorisation state of the searchView when
 * presented
 * @ArrayList<JSOModel> jSonList, List of  JSOModel objects, see com.therdl.shared.beans.JSOModel javadoc for this class
 * @ void getInitialSnipList() gets the initila list of snips when view first comes into focus
 * @ searchSnips(final AutoBean<SnipBean> searchOptionsBean) performs a snip search
 */


public class SnipSearchPresenter extends RdlAbstractPresenter<SearchView> implements SearchView.Presenter {

	public SnipSearchPresenter(SearchView searchView, AppController controller) {
		super(controller);
		this.view = searchView;
		this.view.setPresenter(this);
		log.info("SnipSearchPresenter constructor");
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		log.info("SnipSearchPresenter go adding view");
		checkLogin(view.getAppMenu(),currentUserBean);
		container.clear();
		container.add(view.asWidget());
	}

	/**
	 * Handles snips searching request | response
	 *
	 * @param searchOptionsBean : bean of the snip search options
	 */
	@Override
	public void searchSnips(final AutoBean<SnipBean> searchOptionsBean, final int pageIndex) {
		log.info("SnipSearchPresenter getSnipSearchResult");
		String updateUrl = GWT.getModuleBaseURL() + "getSnips";

		if (!Constants.DEPLOY) {
			updateUrl = updateUrl.replaceAll("/therdl", "");
		}

		log.info("SnipSearchPresenter getSnipDemoResult  updateUrl: " + updateUrl);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
		requestBuilder.setHeader("Content-Type", "application/json");

		searchOptionsBean.as().setPageIndex(pageIndex);
		searchOptionsBean.as().setAction("search");

		String json = AutoBeanCodex.encode(searchOptionsBean).getPayload();
		try {

			requestBuilder.sendRequest(json, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {

					log.info("SnipSearchPresenter  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
					log.info("SnipSearchPresenter onResponseReceived json" + response.getText());

					JsArray<JSOModel> data =
							JSOModel.arrayFromJson(response.getText());

					ArrayList<JSOModel> jSonList = new ArrayList<JSOModel>();
					ArrayList<AutoBean<SnipBean>> beanList = new ArrayList<AutoBean<SnipBean>>();

					for (int i = 0; i < data.length(); i++) {
						jSonList.add(data.get(i));
						beanList.add(AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(i).get(i + "")));

					}

					view.displaySnipList(beanList, pageIndex, calculateListRange(beanList.size(), pageIndex));
				}

				@Override
				public void onError(Request request, Throwable exception) {
					log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());
				}

			});
		} catch (RequestException e) {
			log.info(e.getLocalizedMessage());
		}
	}

}

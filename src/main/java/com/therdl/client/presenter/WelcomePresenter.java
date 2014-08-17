package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
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
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;


/**
 * WelcomePresenter class ia a presenter in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 *
 * @ AppController controller see  com.therdl.client.app.AppController javadoc header comments
 * @ WelcomeView  welcomeView this presenter GUI component
 * @ void doLogIn( String emailtxt, String passwordText ) user login to <URI base path>/getSession
 * calls com.therdl.server.restapi.SessionServlet class and updates the view depending on given/allowed
 * authorisation in the server callback method  onResponseReceived(Request request, Response response)
 */

public class WelcomePresenter extends RdlAbstractPresenter<WelcomeView> implements WelcomeView.Presenter {

	public WelcomePresenter(WelcomeView welcomeView, AppController controller) {
		super(controller);
		this.view = welcomeView;
		this.view.setPresenter(this);
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
		view.getAppMenu().setHomeActive();
		if (view.getSnipMap().get(CoreCategory.GENERAL) == null){
			grabWelcomeSnip(CoreCategory.GENERAL);
		}
		container.add(view.asWidget());
	}

	public void grabWelcomeSnip(final CoreCategory coreCat){
		AutoBean<SnipBean> searchOptionsBean = prepareSearchOptions(coreCat);
		super.searchSnips(searchOptionsBean, new SnipListCallback() {

			public  void onBeanListReturned (ArrayList<AutoBean<SnipBean>> beanList){
				view.showWelcomeSnip(beanList.get(0), coreCat);
			}
		});
	}

	private AutoBean<SnipBean> prepareSearchOptions (CoreCategory coreCategory ){
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		searchOptionsBean.as().setAuthor("RDL");
		searchOptionsBean.as().setCoreCat(coreCategory.getShortName());
		searchOptionsBean.as().setAction("search");
		searchOptionsBean.as().setPageIndex(0);
		searchOptionsBean.as().setSortField("creationDate");
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSnipType("snip");
		return searchOptionsBean;
	}
}

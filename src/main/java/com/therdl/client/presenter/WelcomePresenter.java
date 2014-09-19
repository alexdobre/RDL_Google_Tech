package com.therdl.client.presenter;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.view.WelcomeView;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;


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
		if (view.getSnipMap().get(CoreCategory.GENERAL) == null) {
			grabWelcomeSnip(CoreCategory.GENERAL, RDLConstants.ContentMgmt.RDL_WELCOME_TITLE);
		}
		container.add(view.asWidget());
	}

	public void grabWelcomeSnip(final CoreCategory coreCat, String title) {
		AutoBean<SnipBean> searchOptionsBean = prepareSearchOptions(coreCat, title);
		super.searchSnips(searchOptionsBean, new SnipListCallback() {

			public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
				view.showWelcomeSnip(beanList.get(0), coreCat);
			}
		});
	}

	private AutoBean<SnipBean> prepareSearchOptions(CoreCategory coreCategory, String title) {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		RDLUtils.buildDefaultWelcomeBean(searchOptionsBean);
		searchOptionsBean.as().setCoreCat(coreCategory.getShortName());
		if (title != null) {
			searchOptionsBean.as().setTitle(title);
		}

		return searchOptionsBean;
	}
}

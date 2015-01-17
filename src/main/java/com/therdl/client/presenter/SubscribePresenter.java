package com.therdl.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.app.AppController;
import com.therdl.client.callback.SnipListCallback;
import com.therdl.client.handler.LoginHandler;
import com.therdl.client.view.SubscribeView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;

/**
 * Presents the "become an RDL supporter" page
 *
 * @author alex
 */
public class SubscribePresenter extends RdlAbstractPresenter<SubscribeView> implements SubscribeView.Presenter {

	private AutoBean<CurrentUserBean> currentUserBean;

	public SubscribePresenter(SubscribeView subscribeView, AppController controller) {
		super(controller);
		this.view = subscribeView;
		this.view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		controller.getAppMenu().setHomeActive();
		this.currentUserBean = currentUserBean;
		container.clear();

		//the user can access this page in three states
		checkLogin(new LoginHandler() {
			@Override
			public void onSuccess(AutoBean<CurrentUserBean> currentUserBean) {
				Log.debug("Supporter login handler -BEGIN");
				//1. first check if the user is logged in
				if (!currentUserBean.as().isAuth()) {
					Log.debug("User not logged in - asking for log in");
					ViewUtils.hide(view.getFooter());
					view.getDescBody().clear();
					view.getDescBody().add(new Paragraph(RDL.getI18n().signUpOrLogin()));
					view.setPopulated(false);
				} else {
					//2. then check if the user is already supporter
					if (currentUserBean.as().getIsRDLSupporter()) {
						Log.debug("User logged in - and is already supporter");
						ViewUtils.hide(view.getFooter());
						view.getDescBody().clear();
						view.getDescBody().add(new Paragraph(RDL.getI18n().alreadySupporter()));
						view.setPopulated(false);

						view.getSubscribeFooter().setVisible(false);
					} else {
						//3. user is logged in and not supporter -> populate the description
						Log.debug("User logged in - and is NOT supporter");
						if (!view.isPopulated()) {
							grabRdlSupporterTitleDesc();
						}

						view.getSubscribeFooter().setVisible(true);
					}
				}
			Log.debug("Supporter login handler -END");
			}
		});
		container.add(view.asWidget());
	}

	private void grabRdlSupporterTitleDesc() {
		AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
		ViewUtils.populateDefaultSearchOptions(searchOptionsBean);
		searchOptionsBean.as().setCoreCat(CoreCategory.GENERAL.getShortName());
		searchOptionsBean.as().setTitle(RDLConstants.ContentMgmt.RDL_SUPP_TITLE);
		searchOptionsBean.as().setAuthor(RDLConstants.ContentMgmt.OFFICIAL_AUTHOR);
		searchOptionsBean.as().setSnipType(SnipType.CONTENT_MGMT.getSnipType());

		grabSnipFunc.searchSnips(searchOptionsBean, new SnipListCallback() {
					public void onBeanListReturned(ArrayList<AutoBean<SnipBean>> beanList) {
						if (beanList != null && !beanList.isEmpty()) {
							AutoBean<SnipBean> desc = beanList.get(0);
							view.setPopulated(true);
							view.getDescBody().clear();
							view.getDescBody().getElement().setInnerHTML(desc.as().getContent());
						}
					}
				});
	}

}

package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.app.AppController;
import com.therdl.client.view.SnipSearchView;
import com.therdl.shared.Constants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SnipSearchPresenter implements Presenter, SnipSearchView.Presenter {
    private static Logger log = Logger.getLogger("");
	private final SnipSearchView snipSearchView;
    private Beanery beanery = GWT.create(Beanery.class);
    private ArrayList<JSOModel> jSonList;
    private AutoBean<SnipBean>  currentBean;
    private final AppController controller;

    public SnipSearchPresenter(SnipSearchView snipSearchView, AppController controller){
        this.controller =controller;
        this.snipSearchView = snipSearchView;
        log.info("SnipSearchPresenter constructor");

    }
	
	@Override
	public void go(HasWidgets container) {
        log.info("SnipSearchPresenter go adding view");
		container.clear();
	    container.add(snipSearchView.asWidget());
        if(controller.getCurrentUserBean().as().isAuth() ) {
            log.info("SnipSearchPresenter go !controller.getCurrentUserBean().as().isAuth()  ");
            snipSearchView.getAppMenu().setLogOutVisible(true);
            snipSearchView.getAppMenu().setSignUpVisible(false);
            snipSearchView.getAppMenu().setUserInfoVisible(true);
            snipSearchView.setloginresult(controller.getCurrentUserBean().as().getName(),
                    controller.getCurrentUserBean().as().getEmail(), true  );
        }
        getSnipDemoResult();
	}

    private void getSnipDemoResult() {
        log.info("SnipSearchPresenter getSnipDemoResult");
        String updateUrl =GWT.getModuleBaseURL()+"getSnips";

        if(!Constants.DEPLOY){
            updateUrl = updateUrl.replaceAll("/therdl", "");
        }

        log.info("SnipSearchPresenter getSnipDemoResult  updateUrl: "+ updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,  URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        currentBean = beanery.snipBean();
        currentBean.as().setAction("getall");
        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                    log.info("SnipSearchPresenter  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
                    log.info("SnipSearchPresenter onResponseReceived json" + response.getText());

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());

                    if(data.length() == 0 )  return;

                    jSonList = new ArrayList<JSOModel>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));
                    }

                    log.info("SnipSearchPresenter  initialUpdate onResponseReceived json" + jSonList.get(0).get("0"));

                    AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class,  jSonList.get(0).get("0"));

                    log.info(""+ bean.as().getTitle() );
                    log.info(""+ bean.as().getAuthor() );
//                    log.info(""+ bean.as().getContentAsString() );
//                    log.info(""+ bean.as().getTimeStamp() );

                    snipSearchView.getSnipListDemoResult(data);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    log.info("UpdateServiceImpl initialUpdate onError)" + exception.getLocalizedMessage());
                }

            });
        } catch (RequestException e) { log.info(e.getLocalizedMessage());
        }
    }    // end initialUpdate method

}

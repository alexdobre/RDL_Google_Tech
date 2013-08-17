package com.therdl.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.therdl.client.view.SnipEditView;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.JSOModel;
import com.therdl.shared.beans.SnipBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SnipEditPresenter implements Presenter, SnipEditView.Presenter {

    private static Logger log = Logger.getLogger("");

    private final SnipEditView view;

    private Beanery beanery = GWT.create(Beanery.class);
    private List<JSOModel> jSonList;



    public SnipEditPresenter(SnipEditView view) {
        super();

        this.view = view;
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        fetchSnips();

    }

    private void fetchSnips() {

        log.info("SnipSearchPresenter getSnipDemoResult");

        String updateUrl = GWT.getModuleBaseURL() + "getSnips";

        updateUrl = updateUrl.replaceAll("/therdl", "");

        log.info("SnipSearchPresenter getSnipDemoResult  updateUrl: " + updateUrl);
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(updateUrl));
        requestBuilder.setHeader("Content-Type", "application/json");
        AutoBean<SnipBean>  currentBean = beanery.snipBean();
        currentBean.as().setAction("getall");
        String json = AutoBeanCodex.encode(currentBean).getPayload();
        try {

            requestBuilder.sendRequest(json, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {

                //    log.info("SnipEditPresenter  onResponseReceived response.getHeadersAsString)" + response.getHeadersAsString());
               //     log.info("SnipEditPresenter  onResponseReceived json" + response.getText());

                    JsArray<JSOModel> data =
                            JSOModel.arrayFromJson(response.getText());
                    if(data.length() == 0 )  return;

                    jSonList = new ArrayList<JSOModel>();

                    for (int i = 0; i < data.length(); i++) {
                        jSonList.add(data.get(i));

                    }

                    List<AutoBean<SnipBean>> beans = new ArrayList<AutoBean<SnipBean>>();

                 //   log.info("SnipEditPresenter onResponseReceived json" + jSonList.get(0).get("0"));

                    for (int k = 0; k < jSonList.size(); k++) {
                        String counter = ""+k;
                        log.info("SnipEditPresenter onResponseReceived loop iteration " + counter);
                        log.info("SSnipEditPresenter onResponseReceived making bean from json" + jSonList.get(k).get(counter));
                        AutoBean<SnipBean> bean = AutoBeanCodex.decode(beanery, SnipBean.class, jSonList.get(k).get(counter));

                        log.info("" + bean.as().getTitle());
                        log.info("" + bean.as().getAuthor());
                        log.info("" + bean.as().getContentAsString());
                        log.info("" + bean.as().getTimeStamp());
                        beans.add(bean);
                    }
                    log.info("SnipEditPresenter onResponseReceived passing thru this many beans " + beans.size());
                    view.setSnipDropDown(beans);
                    beans.clear();

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

    @Override
    public void onSaveButtonClicked() {

        log.info("SnipEditPresenter  onSaveButtonClicked BEGIN ");

    }

    @Override
    public void onCloseButtonClicked() {
        log.info("SnipEditPresenter  onCloseButtonClicked  ");

    }

    public SnipEditView getView() {
        return view;
    }


}

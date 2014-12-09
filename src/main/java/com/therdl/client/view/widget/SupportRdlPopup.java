package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.RadioButton;

/**
 * Popup shown to the user to become an RDL supporter
 */
public class SupportRdlPopup extends Composite {

    interface SupportRdlPopupUiBinder extends UiBinder<Widget, SupportRdlPopup> {
    }

    private static SupportRdlPopupUiBinder ourUiBinder = GWT.create(SupportRdlPopupUiBinder.class);

    @UiField
    Modal rdlSupporterModal;
    @UiField
    ModalBody modalBody;

    @UiField
    RadioButton radButUsd;

    @UiField
    Anchor ancSupporter;
    @UiField
    RadioButton radButGbp;
    @UiField
    RadioButton radButEur;

    private boolean populated = false;

    public SupportRdlPopup() {
        initWidget(ourUiBinder.createAndBindUi(this));

        radButUsd.setActive(true);
    }

    public void show() {
        rdlSupporterModal.show();
    }

    public void hide() {
        rdlSupporterModal.hide();
    }

    public void populateBody(AutoBean<SnipBean> contentBean) {
        if (!populated) {
            modalBody.clear();
            modalBody.getElement().setInnerHTML(contentBean.as().getContent());
        }
        populated = true;
    }

    public boolean isPopulated() {
        return populated;
    }

    public void setTitle(String title) {
        rdlSupporterModal.setTitle(title);
    }

    @UiHandler("radButUsd")
    void handleRadButUsd(ClickEvent e) {
        radButUsd.setActive(true);
        radButGbp.setActive(false);
        radButEur.setActive(false);

        ancSupporter.setHref("/rdl/paypalCheckout?cur=USD");
    }

    @UiHandler("radButGbp")
    void handleRadButGbp(ClickEvent e) {
        radButUsd.setActive(false);
        radButGbp.setActive(true);
        radButEur.setActive(false);
        ancSupporter.setHref("/rdl/paypalCheckout?cur=GBP");
    }

    @UiHandler("radButEur")
    void handleRadButEur(ClickEvent e) {
        radButUsd.setActive(false);
        radButGbp.setActive(false);
        radButEur.setActive(true);
        ancSupporter.setHref("/rdl/paypalCheckout?cur=EUR");
    }

//    @UiHandler("radButUsd")
//    void handleRadButUsd(ClickEvent e) {
//
//        ancSupporter.setHref("href=/rdl/paypalCheckout?cur=USD");
//    }

}

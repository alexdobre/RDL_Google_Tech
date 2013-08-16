package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SignInView;

public class SignInViewImpl extends Composite implements SignInView {

  private Presenter presenter;

  
  private boolean alreadyInit;

  private static SignInViewImplUiBinder uiBinder = GWT.create(SignInViewImplUiBinder.class);

  @UiField HTML htmlSignIn;

  interface SignInViewImplUiBinder extends UiBinder<Widget, SignInViewImpl> {
  }

  public SignInViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }



  public void start() {

  }


  
  private void setLoggedIn() {

  }



}

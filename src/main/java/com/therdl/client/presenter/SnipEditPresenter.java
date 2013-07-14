package com.therdl.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.therdl.client.dto.SnipProxy;
import com.therdl.client.view.SnipEditView;

import java.util.logging.Logger;

public class SnipEditPresenter implements Presenter, SnipEditView.Presenter<SnipProxy> {

    private static Logger log = Logger.getLogger("");
	private final EventBus eventBus;
	private final SnipEditView<SnipProxy> view;
	private SnipProxy snipProxy;



	public SnipEditPresenter( EventBus eventBus,SnipEditView<SnipProxy> view) {
		super();

		this.eventBus = eventBus;
		this.view = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
	    container.add(view.asWidget());
	 //   fetchSnip();

	}

	 private void fetchSnip() {
//		 rpcSnipService.get(null, new AsyncCallback<Snip>() {
//		      public void onSuccess(Snip result) {
//		    	  if (result == null){
//		    		  snip = new Snip();
//		    	  }else{
//		    		  snip = result;
//		    	  }
//		      }
//		      
//		      public void onFailure(Throwable caught) {
//		        Window.alert("Error fetching snip");
//		      }
//		    });
		  }

	@Override
	public void onSaveButtonClicked() {

        log.info("SnipEditPresenter  onSaveButtonClicked BEGIN ");

//		SnipRequest request = appReqFactory.getSnipRequest();
//
//		SnipProxy snip = null;
//		if (snip == null) { // insert|create
//			snip = request.create(SnipProxy.class);
//
//		} else { // update|edit
//			snip = request.edit(snip);
//		}
//
//		// set name
//		snip.setName(getName());
//
//		// set items
//		data.setItems(getItems(request));
//
//		request.persist().using(data).fire(new Receiver<WalletDataProxy>() {
//			public void onSuccess(WalletDataProxy walletData) {
//				process(walletData);
//			}
//
//			public void onFailure(ServerFailure error) {
//				super.onFailure(error);
//			}
//		});

	}

	@Override
	public void onCloseButtonClicked() {
        log.info("SnipEditPresenter  onCloseButtonClicked  ");

	}	

	public SnipEditView<SnipProxy> getView() {
		return view;
	}

	public void setSnipProxy(SnipProxy snipProxy) {
		this.snipProxy = snipProxy;
	}

	public SnipProxy getSnipProxy() {
		return snipProxy;
	}
	
}

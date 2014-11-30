package com.therdl.client.presenter.runt.impl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.app.FuncFactory;
import com.therdl.client.app.WidgetHolder;
import com.therdl.client.callback.StatusCallback;
import com.therdl.client.handler.ClickHandler;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.SnipActionWidget;
import com.therdl.client.view.widget.runtized.ReferenceListRow;
import com.therdl.client.view.widget.runtized.ReplyEditWidget;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Column;

/**
 * Deals with logic behind replies, references etc.
 */
public class ReplyRuntImpl implements ReplyRunt {
	private AutoBean<CurrentUserBean> currentUserBean;
	private GrabSnipFunc grabSnipFunc;
	private ValidatedView validatedView;
	private ReferenceListRow referenceListRow;
	private SnipActionWidget snipActionWidget;

	public ReplyRuntImpl(AutoBean<CurrentUserBean> currentUserBean, ValidatedView validatedView) {
		this.currentUserBean = currentUserBean;
		grabSnipFunc = FuncFactory.createGrabSnipFunc();
		this.validatedView = validatedView;
	}

	@Override
	public void decorateReference(final ReferenceListRow referenceListRow) {
		SimplePanel snipActionPanel = referenceListRow.getSnipActionPanel();
		final SnipActionWidget snipActionWidget = new SnipActionWidget();
		this.snipActionWidget = snipActionWidget;
		snipActionPanel.clear();
		snipActionPanel.add(snipActionWidget);
		snipActionLogic(currentUserBean, referenceListRow.getReferenceBean(), snipActionWidget,
				new ClickHandler() {
					@Override
					public void onClick(Object source) {
						editReference(referenceListRow, snipActionWidget);
					}
				},
				new RequestObserver() {
					@Override
					public void onSuccess(String response) {
						rebuildReferenceRow();
					}
				}
		);
	}

	@Override
	public void snipActionLogic(final AutoBean<CurrentUserBean> currentUserBean, final AutoBean<SnipBean> snipBean,
			final SnipActionWidget snipActionWidget, ClickHandler editHandler, final RequestObserver requestObserver) {
		boolean isAuthor = ViewUtils.isAuthor(currentUserBean, snipBean);
		boolean repGiven = snipBean.as().getIsRepGivenByUser() == 1;
		Log.info("snipActionLogic isAuthor: " + isAuthor + " repGiven: " + repGiven);
		//if the user is not logged in we do not show any action
		if (currentUserBean == null || !currentUserBean.as().isAuth()) {
			snipActionWidget.hide();
			return;
		}
		//the user is author so we show the edit button
		if (isAuthor) {
			showSnipAction(true, snipActionWidget, editHandler);
			//the user has not logged in or has not given rep so we show the give rep button
		} else if (!repGiven) {
			showSnipAction(false, snipActionWidget,
					new ClickHandler() {
						@Override
						public void onClick(Object source) {
							//we do not place validation here but we do place it on the server side
							grabSnipFunc.giveSnipReputation(snipBean.as().getId(), currentUserBean, validatedView,
									requestObserver);
							showSnipAction(null, snipActionWidget, null);
						}
					}
			);
			//we show the rep given icon
		} else {
			showSnipAction(null, snipActionWidget, null);
		}
	}

	@Override
	public void showSnipAction(Boolean isEdit, SnipActionWidget snipActionWidget, ClickHandler clickHandler) {
		if (isEdit == null) {
			snipActionWidget.showRepGiven();
		} else if (isEdit) {
			snipActionWidget.showEditBtn(clickHandler);
		} else {
			snipActionWidget.showRepBtn(clickHandler);
		}
	}

	@Override
	public void editReference(ReferenceListRow referenceListRow, SnipActionWidget snipActionWidget) {
		this.referenceListRow = referenceListRow;
		Column refContent = referenceListRow.getRefContent();
		ReplyEditWidget replyEditWidget = WidgetHolder.getHolder().getReplyEditWidget(this);
		replyEditWidget.populate(referenceListRow.getReferenceBean(), currentUserBean);
		snipActionWidget.hide();
		ViewUtils.hide(refContent);
		referenceListRow.getEditRefContent().add(replyEditWidget);
		ViewUtils.show(referenceListRow.getEditRefContent());
	}

	@Override
	public void cancelEditReference() {
		Log.info("Reply runt cancel edit reference");
		rebuildReferenceRow();
	}

	@Override
	public void saveEditedReference() {
		Log.info("Reply runt saveEditedReference");
		grabSnipFunc.updateSnip(referenceListRow.getReferenceBean(), currentUserBean.as().getToken(),
				new StatusCallback(validatedView) {
					@Override
					public void onSuccess(Request request, Response response) {
						rebuildReferenceRow();
					}
				});
	}

	private void rebuildReferenceRow() {
		referenceListRow.populate(referenceListRow.getReferenceBean(), currentUserBean, this);
		snipActionWidget.showEditBtn(new ClickHandler() {
			@Override
			public void onClick(Object source) {
				editReference(referenceListRow, snipActionWidget);
			}
		});
	}
}

package com.therdl.client.view.impl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.app.RuntFactory;
import com.therdl.client.handler.LoginHandler;
import com.therdl.client.presenter.runt.ReplyRunt;
import com.therdl.client.validation.SnipViewValidator;
import com.therdl.client.view.EmotionView;
import com.therdl.client.view.PaginatedView;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.SocialPanelView;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.EmotionTranslator;
import com.therdl.client.view.common.PaginationHelper;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.*;
import com.therdl.client.view.widget.runtized.ReferenceListRow;
import com.therdl.shared.*;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.events.BecomeRdlSupporterEvent;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.ReportAbuseEvent;
import com.therdl.shared.events.ShowAbuseCommentsEvent;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

import java.util.ArrayList;

/**
 * SnipViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI so user can view a Snip edit, is brought into focus
 * fired from a Closure  new Religion
 *
 * @ SnipViewPresenter.Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ Map<String, String> currentSnipView, this is a store for the parameters
 * to be extracted from the client side snip data, these parameters should be
 * used to call the backend to retrieve the full snip for this view, under construction
 * @ AppMenu appMenu the upper menu view
 */
public abstract class SnipViewImpl extends AbstractValidatedAppMenuView
		implements SnipView, PaginatedView, ValidatedView, EmotionView,SocialPanelView {

	private final AutoBean<CurrentUserBean> currentUserBean;

	interface SnipViewImplUiBinder extends UiBinder<Widget, SnipViewImpl> {
	}

	private static SnipViewImplUiBinder uiBinder = GWT.create(SnipViewImplUiBinder.class);

	protected SnipView.Presenter presenter;
	private Beanery beanery = GWT.create(Beanery.class);

	private AutoBean<SnipBean> currentSnipBean;
	protected AutoBean<SnipBean> replyBean;

	@UiField
	FlowPanel snipViewCont, radioBtnParent, radioBtnParentProp;
	@UiField
	Panel referenceCont;
	@UiField
	Column refFilterParent, referenceListCont;
	@UiField
	PanelBody richTextArea;
	@UiField(provided = true)
	Summernote editorWidgetReply;
	@UiField
	Button showRef, leaveRef, saveRef, closeRef, btnEmotionPicker, reportAbuse;
	@UiField
	RadioButton rb1, rb2, rb3, prb1, prb2;
	@UiField
	Column snipActionContainer;
	@UiField
	LoadingWidget loadingWidget;
	@UiField
	AnchorListItem listRange, nextPage, prevPage;
	@UiField
	LinkedGroup listGroup;
	@UiField
	FlowPanel emoListPanel, emoListPanelReply;
	@UiField
	Paragraph abuseWarning;
	@UiField
	Anchor abuseWarningAnchor;
	
	@UiField
	Button share;
	
	private SocialPanel socialPanel;

	private SnipListRow snipListRow;
	private ArrayList<ReferenceListRow> itemList;
	ReferenceSearchFilterWidget referenceSearchFilterWidget;
	private SnipActionWidget snipActionWidget;
	private EmotionPicker emotionPicker;
	

	private AutoBean<SnipBean> searchOptionsBean;
	String btnTextShow = RDL.i18n.showReferences();
	String btnTextHide = RDL.i18n.hideReferences();
	String snipType = RDLConstants.SnipType.REFERENCE;

	public SnipViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		editorWidgetReply = new Summernote();
		editorWidgetReply.setToolbar(new SummerCustomToolbar());
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;
		snipActionWidget = new SnipActionWidget();
		socialPanel = new SocialPanel();
//		socialPanelContainer.add(socialPanel);
		snipActionContainer.add(snipActionWidget);
		emoListPanel.addStyleName("labels");
		itemList = new ArrayList<ReferenceListRow>(Constants.DEFAULT_REFERENCE_PAGE_SIZE);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	protected void onLoad() {
		if (referenceSearchFilterWidget == null) {
			referenceSearchFilterWidget = new ReferenceSearchFilterWidget(this);
		}
		refFilterParent.add(referenceSearchFilterWidget);
		ViewUtils.hide(refFilterParent);
		ViewUtils.hide(referenceListCont);
	}

	@Override
	protected void onUnload() {
		snipViewCont.clear();
		refFilterParent.clear();
	}

	/**
	 * creates main gwt widgets of snip view page
	 *
	 * @param snipBean snip as SnipBean object
	 */
	public void populateSnip(AutoBean<SnipBean> snipBean) {
		this.currentSnipBean = snipBean;
		// this is the top widget, like in the list widget
		if (snipListRow == null) {
			snipListRow = new SnipListRow(snipBean, currentUserBean, SnipType.fromString(snipBean.as().getSnipType()),
					snipViewCont);
		} else {
			snipListRow.populate(snipBean, currentUserBean, SnipType.fromString(snipBean.as().getSnipType()));
		}
		snipViewCont.add(snipListRow);
		//display emotions
		emoListPanel.clear();
		for (String emoStr : currentSnipBean.as().getEmotions()) {
			emoListPanel.add(ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr)));
		}
		emoListPanelReply.clear();
		//display content
		richTextArea.getElement().setInnerHTML(snipBean.as().getContent());
		//references hidden
		showRef.setText(btnTextShow);
		ViewUtils.hide(referenceCont);
		ViewUtils.showHide(ViewUtils.showReportAbuseLogic(currentUserBean, currentSnipBean), reportAbuse);
		ViewUtils.showHide(currentSnipBean.as().getAbuseCount() != null && currentSnipBean.as().getAbuseCount() > 0,
				abuseWarning);
		hideMessages();
		// call implementations or NO OP
		onViewSnip();
	}

	public void onViewSnip() {
		//no op implementation
	}
	
	@UiHandler("share")
	public void showSocialNetworks() {
		ViewUtils.showHide(true, socialPanel);
	};
	
	
	

	/**
	 * handler of the leave reference button. Opens an editor for creating new reference
	 *
	 * @param event ClickEvent
	 */
	@UiHandler("leaveRef")
	public void onLeaveRefClicked(ClickEvent event) {
		if (currentUserBean.as().isAuth()) {
			//if the user is not RDL supporter prompted to become one
			if (!currentUserBean.as().getIsRDLSupporter() && !Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
				GuiEventBus.EVENT_BUS
						.fireEvent(new BecomeRdlSupporterEvent(RDL.getI18n().rdlSupporterPopupTitleLeaveRef()));
			} else {
				leaveRefHandler(currentUserBean);
			}
		} else {
			appMenu.showLoginPopUp(new LoginHandler() {
				@Override
				public void onSuccess(AutoBean<CurrentUserBean> userBean) {
					leaveRefHandler(userBean);
				}
			});
		}
	}

	@UiHandler("btnEmotionPicker") void onEmotionPicker(ClickEvent event) {
		if (emotionPicker == null) {
			emotionPicker = new EmotionPicker(this, replyBean);
		}
		emotionPicker.show();
	}

	@UiHandler("reportAbuse")
	public void onReportAbuseClicked(ClickEvent event) {
		GuiEventBus.EVENT_BUS.fireEvent(new ReportAbuseEvent(currentSnipBean.as().getId()));
	}

	private void leaveRefHandler(AutoBean<CurrentUserBean> userBean) {
		ViewUtils.show(referenceCont);
		ViewUtils.hide(refFilterParent);
		closeRef.getElement().getStyle().setProperty("marginLeft", "10px");
		ViewUtils.hide(referenceListCont);
		showRef.setText(btnTextShow);
		replyBean = beanery.snipBean();
		editorWidgetReply.setCode("");
		Log.info("Leave ref handler END with widget code: " + editorWidgetReply.getCode());
	}

	/**
	 * handler of the closeRef button
	 * closes editor
	 *
	 * @param event
	 */
	@UiHandler("closeRef")
	public void onCloseRefClicked(ClickEvent event) {
		ViewUtils.hide(referenceCont);
	}

	/**
	 * handler of the showRef button.
	 * Calls presenter's function to retrieve references for the current viewed snip
	 *
	 * @param event
	 */
	@UiHandler("showRef")
	public void onShowRefClicked(ClickEvent event) {
		if (showRef.getText().equals(RDL.i18n.showReferences()) || showRef.getText().equals(RDL.i18n.showPosts())) {
			ViewUtils.hide(loadingWidget);
			presenter.populateReplies(searchOptionsBean);

		} else {
			ViewUtils.hide(referenceListCont);
			ViewUtils.hide(refFilterParent);
			showRef.setText(btnTextShow);
		}
	}

	@UiHandler("abuseWarningAnchor")
	public void abuseWarningClick(ClickEvent event) {
		GuiEventBus.EVENT_BUS.fireEvent(new ShowAbuseCommentsEvent(currentSnipBean.as()));
	}

	@Override
	public void displayEmotions() {
		emoListPanelReply.clear();
		if (replyBean != null && replyBean.as().getEmotions() != null) {
			for (String emoStr : replyBean.as().getEmotions()) {
				Log.info("Displaying selected emotion: " + emoStr);
				Span span = ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr));
				span.setText(EmotionTranslator.getMessage(Emotion.valueOf(emoStr)));
				emoListPanelReply.add(span);
			}
		}
	}

	/**
	 * when user clicks save reference button, request is sent to save reference and also the counter by refType is incremented
	 * in response handler call this function to hide leave reference button and
	 * also increment ref counter in the view for the saved reference type
	 *
	 * @param refType  saved reference type
	 * @param snipType saved snip type, can be reference/post/pledge/counter
	 */
	public void saveReferenceResponseHandler(String refType, String snipType) {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)
				|| Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			ViewUtils.hide(leaveRef);
		}
		snipListRow.incrementRefCounterByRefType(refType, snipType);
		ViewUtils.hide(referenceCont);
	}

	public SnipActionWidget getSnipActionWidget() {
		return snipActionWidget;
	}

	@Override
	public void showHideReplyButton(Boolean show) {
		ViewUtils.showHide(show, leaveRef);
	}

	@UiHandler("saveRef")
	public void onSaveRefClicked(ClickEvent event) {
		formReplyBean();
		setReplyType();
		String validRes = SnipViewValidator.validateReply(replyBean, currentSnipBean);
		if (validRes != null) {
			setErrorMessage(validRes);
			return;
		}
		presenter.saveReference(replyBean);
	}

	protected void formReplyBean() {
		//this is a workaround for a strange UI bug where the code of the empty text editor is not empty
		if ("<p><br></p>".equals(editorWidgetReply.getCode())) {
			replyBean.as().setContent(null);
		} else {
			replyBean.as().setContent(editorWidgetReply.getCode());
		}
		replyBean.as().setAuthor(currentUserBean.as().getName());
		replyBean.as().setParentSnip(currentSnipBean.as().getId());
		replyBean.as().setRep(0);
		replyBean.as().setCoreCat(currentSnipBean.as().getCoreCat());
		replyBean.as().setTitle("Re: " + currentSnipBean.as().getTitle());
		replyBean.as().setToken(currentUserBean.as().getToken());
	}

	protected abstract void setReplyType();

	/**
	 * shows references in a tab panel with paging
	 *
	 * @param beanList list of references as bean objects
	 */
	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		Log.info("Showing references: " + beanList.size());
		this.searchOptionsBean.as().setPageIndex(pageIndex);
		ViewUtils.hide(loadingWidget);
		showRef.setText(btnTextHide);
		ViewUtils.show(referenceListCont);
		ViewUtils.show(refFilterParent);
		ViewUtils.hide(referenceCont);

		for (int j = 0; j < beanList.size(); j++) {
			//we first see is we already have an item created
			ReferenceListRow referenceListRow;
			if (itemList.size() >= j + 1) {
				//if yes we just populate the existing item
				referenceListRow = itemList.get(j);
				ReplyRunt replyRunt = RuntFactory.createReplyRunt(this.currentUserBean, referenceListRow);
				referenceListRow.populate(beanList.get(j), this.currentUserBean, replyRunt);
				ViewUtils.show(referenceListRow.getParentObject());
				replyRunt.decorateReference(referenceListRow);
			} else {
				//otherwise we create a new item
				LinkedGroupItem listItem = new LinkedGroupItem();
				referenceListRow = new ReferenceListRow(beanList.get(j), currentUserBean, listItem, null);
				ReplyRunt replyRunt = RuntFactory.createReplyRunt(this.currentUserBean, referenceListRow);
				referenceListRow.setReplyRunt(replyRunt);
				itemList.add(referenceListRow);
				listItem.setPaddingBottom(2);
				listItem.setPaddingTop(2);
				listItem.setPaddingLeft(2);
				listItem.setPaddingRight(2);
				listItem.add(referenceListRow);
				listGroup.add(listItem);
				replyRunt.decorateReference(referenceListRow);
			}
		}
		//finally we hide unused items
		hideUnusedItems(beanList);
	}

	public void hideUnusedItems(ArrayList<AutoBean<SnipBean>> beanList) {
		if (beanList.size() < Constants.DEFAULT_REFERENCE_PAGE_SIZE) {
			if (itemList.size() > beanList.size()) {
				for (int i = 0; i < itemList.size() - beanList.size(); i++) {
					ViewUtils.hide(itemList.get(beanList.size() + i).getParentObject());
				}
			}
		}
	}

	@UiHandler("nextPage")
	public void nextPageClicked(ClickEvent event) {
		Log.info("Firing next page event from pageIndex: " + searchOptionsBean.as().getPageIndex());
		PaginationHelper.doNextPage(searchOptionsBean.as().getPageIndex(), listRange.getText(), itemList.size(),
				Constants.DEFAULT_REFERENCE_PAGE_SIZE, presenter);
	}

	@UiHandler("prevPage")
	public void prevPageClicked(ClickEvent event) {
		Log.info("Firing previous page event from pageIndex: " + searchOptionsBean.as().getPageIndex());
		PaginationHelper.doPrevPage(searchOptionsBean.as().getPageIndex(), presenter);
	}

	@Override
	public void setListRange(String listRange) {
		this.listRange.setText(listRange);
	}

	@Override
	public void nextPageActive(boolean active) {
		if (active) {
			nextPage.removeStyleName("disabled");
		} else {
			nextPage.addStyleName("disabled");
		}
	}

	@Override
	public void prevPageActive(boolean active) {
		if (active) {
			prevPage.removeStyleName("disabled");
		} else {
			prevPage.addStyleName("disabled");
		}
	}

	@Override
	public ReferenceSearchFilterWidget getFilter() {
		return referenceSearchFilterWidget;
	}

	@Override
	public AutoBean<SnipBean> getSearchOptionsBean() {
		return searchOptionsBean;
	}

	@Override
	public void setSearchOptionsBean(AutoBean<SnipBean> searchOptionsBean) {
		this.searchOptionsBean = searchOptionsBean;
	}
}

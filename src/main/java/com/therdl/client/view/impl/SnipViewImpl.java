package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.common.SnipType;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.EditorWidget;
import com.therdl.client.view.widget.LoadingWidget;
import com.therdl.client.view.widget.ReferenceListRow;
import com.therdl.client.view.widget.ReferenceSearchFilterWidget;
import com.therdl.client.view.widget.SnipListRow;
import com.therdl.shared.Global;
import com.therdl.shared.LoginHandler;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RequestObserver;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;

import java.util.ArrayList;
import java.util.logging.Logger;

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
public class SnipViewImpl extends Composite implements SnipView {

	private static Logger log = Logger.getLogger("");

	private final AutoBean<CurrentUserBean> currentUserBean;

	interface SnipViewImpllUiBinder extends UiBinder<Widget, SnipViewImpl> {
	}

	private static SnipViewImpllUiBinder uiBinder = GWT.create(SnipViewImpllUiBinder.class);

	private SnipView.Presenter presenter;
	private Beanery beanery = GWT.create(Beanery.class);

	private AutoBean<SnipBean> currentSnipBean;
	private boolean showEditBtn = false;

	AppMenu appMenu;
	@UiField
	FlowPanel snipViewCont, referenceCont, radioBtnParent, radioBtnParentProp;
	@UiField
	Column refFilterParent, referenceListCont;
	@UiField
	RichTextArea richTextArea;
	@UiField
	EditorWidget editorWidget;
	@UiField
	Button showRef, leaveRef, saveRef, closeRef, editBtn, repBtn;
	@UiField
	RadioButton rb1, rb2, rb3, prb1, prb2;
	@UiField
	Icon repGivenIcon;
	@UiField
	LoadingWidget loadingWidget;
	@UiField
	AnchorListItem listRange, nextPage, prevPage;
	@UiField
	LinkedGroup listGroup;

	SnipListRow snipListRow;
	ReferenceSearchFilterWidget referenceSearchFilterWidget;

	AutoBean<SnipBean> searchOptionsBean = beanery.snipBean();
	String btnTextShow = RDL.i18n.showReferences();
	String btnTextHide = RDL.i18n.hideReferences();
	String snipType = RDLConstants.SnipType.REFERENCE;

	public SnipViewImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		initWidget(uiBinder.createAndBindUi(this));
		this.appMenu = appMenu;
		this.currentUserBean = currentUserBean;

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			ViewUtils.hide(radioBtnParentProp);
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			btnTextShow = RDL.i18n.showPosts();
			btnTextHide = RDL.i18n.hidePosts();
			snipType = RDLConstants.SnipType.POST;

			ViewUtils.hide(radioBtnParent);
			ViewUtils.hide(radioBtnParentProp);
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			btnTextShow = RDL.i18n.showPosts();
			btnTextHide = RDL.i18n.hidePosts();
			snipType = RDLConstants.SnipType.PLEDGE + "," + RDLConstants.SnipType.COUNTER;

			ViewUtils.hide(radioBtnParent);
		}

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
		referenceSearchFilterWidget = new ReferenceSearchFilterWidget(this);
		refFilterParent.add(referenceSearchFilterWidget);
		ViewUtils.hide(refFilterParent);
		ViewUtils.hide(referenceListCont);
		listGroup.clear();
	}

	@Override
	protected void onUnload() {
		listGroup.clear();
		snipViewCont.clear();
		refFilterParent.clear();
		//  checkboxBtnParent.clear();
	}

	/**
	 * creates main gwt widgets of snip view page
	 *
	 * @param snipBean snip as SnipBean object
	 */
	public void viewSnip(AutoBean<SnipBean> snipBean) {
		this.currentSnipBean = snipBean;

		// this is the top widget, like in the list widget
		snipListRow = new SnipListRow(snipBean, currentUserBean, SnipType.fromString(snipBean.as().getSnipType()));
		snipViewCont.add(snipListRow);
		richTextArea.setHTML(snipBean.as().getContent());
		richTextArea.setEnabled(false);
		showRef.setText(btnTextShow);
		ViewUtils.hide(referenceCont);

		if (Global.moduleName.equals(RDLConstants.Modules.STORIES) || Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			leaveRef.setText(RDL.i18n.reply());
			saveRef.setText(RDL.i18n.savePost());
		}

	}

	/**
	 * handler of the leave reference button. Opens an editor for creating new reference
	 *
	 * @param event ClickEvent
	 */
	@UiHandler("leaveRef")
	public void onLeaveRefClicked(ClickEvent event) {
		if (currentUserBean.as().isAuth()) {
			leaveRefHandler(currentUserBean);
		} else {
			presenter.getController().getWelcomeView().showLoginPopUp(leaveRef.getAbsoluteLeft() + 120, leaveRef.getAbsoluteTop() - 120, new LoginHandler() {
				@Override
				public void onSuccess(AutoBean<CurrentUserBean> userBean) {
					leaveRefHandler(userBean);
				}
			});
		}
	}

	private void leaveRefHandler(AutoBean<CurrentUserBean> userBean) {
		if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS) && !userBean.as().getIsRDLSupporter()) {
			Window.alert(RDL.i18n.pledgeCreateMsg());
		} else {
			ViewUtils.show(referenceCont);
			ViewUtils.hide(refFilterParent);
			closeRef.getElement().getStyle().setProperty("marginLeft", "10px");
			ViewUtils.hide(referenceListCont);
			editorWidget.setHTML("");
			showRef.setText(btnTextShow);
		}
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
			initSearchOptionsBean();

			ViewUtils.hide(loadingWidget);
			presenter.populateReplies(searchOptionsBean, 0);

		} else {
			ViewUtils.hide(referenceListCont);
			ViewUtils.hide(refFilterParent);
			showRef.setText(btnTextShow);
		}
	}

	private void initSearchOptionsBean() {
		searchOptionsBean = beanery.snipBean();
		searchOptionsBean.as().setSortOrder(-1);
		searchOptionsBean.as().setSortField(RDLConstants.SnipFields.CREATION_DATE);
		searchOptionsBean.as().setSnipType(snipType);
	}


	@UiHandler("repBtn")
	public void onRepBtnClicked(ClickEvent event) {
		presenter.giveSnipReputation(currentSnipBean.as().getId(), new RequestObserver() {
			@Override
			public void onSuccess(String response) {
				giveRepResponseHandler();
			}
		});
	}

	@UiHandler("editBtn")
	public void onEditBtnClicked(ClickEvent event) {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			History.newItem(RDLConstants.Tokens.SNIP_EDIT + ":" + currentSnipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			History.newItem(RDLConstants.Tokens.THREAD_EDIT + ":" + currentSnipBean.as().getId());
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			History.newItem(RDLConstants.Tokens.PROPOSAL_EDIT + ":" + currentSnipBean.as().getId());
	}

	/**
	 * when user clicks give reputation button, request is sent to increment reputation counter for the snip
	 * in response handler call this function to hide button and also increment rep counter in the view
	 */
	public void giveRepResponseHandler() {
		ViewUtils.hide(repBtn);
		ViewUtils.show(repGivenIcon);
		snipListRow.incrementRepCounter();
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

	@Override
	public void showHideLikeOrEditButton(Boolean isAuthor, Boolean repGiven) {
		ViewUtils.hide(editBtn);
		ViewUtils.hide(repBtn);
		ViewUtils.hide(repGivenIcon);
		if (isAuthor) {
			ViewUtils.show(editBtn);
		} else if (repGiven) {
			ViewUtils.show(repGivenIcon);
		} else {
			ViewUtils.show(repBtn);
		}
	}

	@Override
	public void showHideReplyButton(Boolean show) {
		ViewUtils.showHide(show, leaveRef);
	}

	/**
	 * saves a reference
	 * constructs bean object for reference and calls presenter's function to save created reference
	 *
	 * @param event
	 */
	@UiHandler("saveRef")
	public void onSaveRefClicked(ClickEvent event) {
		if (editorWidget.getHTML().equals("")) {
			Window.alert("Reference content cannot be empty.");
			return;
		}

		AutoBean<SnipBean> newBean = beanery.snipBean();
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			String referenceType = RDLConstants.ReferenceType.POSITIVE;

			if (rb2.getValue()) {
				referenceType = RDLConstants.ReferenceType.NEUTRAL;
			} else if (rb3.getValue()) {
				referenceType = RDLConstants.ReferenceType.NEGATIVE;
			}
			newBean.as().setReferenceType(referenceType);
			newBean.as().setSnipType(RDLConstants.SnipType.REFERENCE);
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			newBean.as().setSnipType(RDLConstants.SnipType.POST);
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			String proposalType = RDLConstants.SnipType.PLEDGE;
			if (prb2.getValue()) {
				proposalType = RDLConstants.SnipType.COUNTER;
			}
			newBean.as().setSnipType(proposalType);
		}
		// set data for reference object

		newBean.as().setContent(editorWidget.getHTML());
		newBean.as().setAuthor(currentUserBean.as().getName());
		newBean.as().setRep(0);

		// this is parent snip id
		newBean.as().setId(currentSnipBean.as().getId());

		presenter.saveReference(newBean);
	}

	/**
	 * shows references in a tab panel with paging
	 *
	 * @param beanList list of references as bean objects
	 */
	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex, String listRange) {
		ViewUtils.hide(loadingWidget);
		showRef.setText(btnTextHide);
		ViewUtils.show(referenceListCont);
		ViewUtils.show(refFilterParent);
		ViewUtils.hide(referenceCont);

		if (beanList.size() == 0) {
			listGroup.clear();
			listGroup.add(new Label(RDL.i18n.noDataToDisplay()));
		}


		for (int j = 0; j < beanList.size(); j++) {
			ReferenceListRow referenceListRow = new ReferenceListRow(beanList.get(j), currentUserBean, this);
			LinkedGroupItem listItem = new LinkedGroupItem();
			listItem.setPaddingBottom(2);
			listItem.setPaddingTop(2);
			listItem.setPaddingLeft(2);
			listItem.setPaddingRight(2);
			listItem.add(referenceListRow);
			listGroup.add(listItem);
		}

//		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
//			@Override
//			public void onBeforeSelection(BeforeSelectionEvent<Integer> integerBeforeSelectionEvent) {
//				loadingWidget.getElement().getStyle().setProperty("display", "block");
//				presenter.getSnipReferences(searchOptionsBean, integerBeforeSelectionEvent.getItem());
//			}
//		});

		this.listRange.setText(listRange);
	}

	public AutoBean<SnipBean> getSearchOptionsBean() {
		return searchOptionsBean;
	}

	public void setSearchOptionsBean(AutoBean<SnipBean> searchOptionsBean) {
		this.searchOptionsBean = searchOptionsBean;
	}

	public AppMenu getAppMenu(){
		return appMenu;
	}
}

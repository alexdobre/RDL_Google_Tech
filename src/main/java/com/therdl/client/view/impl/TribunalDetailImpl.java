package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.handler.ClickHandler;
import com.therdl.client.validation.SnipViewValidator;
import com.therdl.client.view.EmotionView;
import com.therdl.client.view.PaginatedView;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.TribunalDetail;
import com.therdl.client.view.ValidatedView;
import com.therdl.client.view.common.EmotionTranslator;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.EmotionPicker;
import com.therdl.client.view.widget.LoadingWidget;
import com.therdl.client.view.widget.ReferenceSearchFilterWidget;
import com.therdl.client.view.widget.TribunalCommentRow;
import com.therdl.client.view.widget.TribunalListRow;
import com.therdl.shared.Constants;
import com.therdl.shared.Emotion;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * User views the details of a tribunal item
 */
public class TribunalDetailImpl extends AbstractValidatedAppMenuView implements TribunalDetail, PaginatedView, ValidatedView, EmotionView {

	private static Logger log = Logger.getLogger(TribunalDetailImpl.class.getName());

	private final AutoBean<CurrentUserBean> currentUserBean;

	interface TribunalDetailImplUiBinder extends UiBinder<Widget, TribunalDetailImpl> {
	}

	private static TribunalDetailImplUiBinder uiBinder = GWT.create(TribunalDetailImplUiBinder.class);

	protected TribunalDetailImpl.Presenter tribunalPresenter;
	private Beanery beanery = GWT.create(Beanery.class);
	private TribunalListRow tribunalListRow;
	private AutoBean<SnipBean> currentSnipBean;
	protected AutoBean<SnipBean> replyBean;
	private AutoBean<SnipBean> searchOptionsBean;
	private ArrayList<TribunalCommentRow> itemList;
	private EmotionPicker emotionPicker;

	@UiField
	FlowPanel emoListPanel, emoListPanelReply, abuseCont;
	@UiField
	AnchorListItem listRange, nextPage, prevPage;
	@UiField
	LoadingWidget loadingWidget;
	@UiField
	Button showComments, leaveComment, saveComment, cancelComment, btnEmotionPicker;
	@UiField
	Panel leaveCommentCont;
	@UiField
	Column commentsCont;
	@UiField
	LinkedGroup listGroup;
	@UiField
	PanelBody textContentArea;
	@UiField
	TextArea commentTextArea;
	@UiField
	Button yesVoteAbuse, noVoteAbuse;
	@UiField
	Paragraph yesVotedAbuse, noVotedAbuse;

	public TribunalDetailImpl(AutoBean<CurrentUserBean> currentUserBean, AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;
		this.searchOptionsBean = beanery.snipBean();
		emoListPanel.addStyleName("labels");
		itemList = new ArrayList<TribunalCommentRow>(Constants.DEFAULT_REFERENCE_PAGE_SIZE);
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

	@UiHandler("btnEmotionPicker")
	void onEmotionPicker(ClickEvent event) {
		if (emotionPicker == null) {
			emotionPicker = new EmotionPicker(this, replyBean);
		}
		emotionPicker.show();
	}

	@UiHandler("leaveComment")
	public void onLeaveCommentClicked(ClickEvent event) {
		ViewUtils.show(leaveCommentCont);
		ViewUtils.hide(commentsCont);
		replyBean = beanery.snipBean();
	}

	@UiHandler("showComments")
	public void onShowCommentsClicked(ClickEvent event) {
		if (showComments.getText().equals(RDL.getI18n().showComments())) {
			tribunalPresenter.populateReplies(searchOptionsBean);
		} else {
			ViewUtils.hide(commentsCont);
			showComments.setText(RDL.getI18n().showComments());
		}
	}

	@UiHandler("saveComment")
	public void onSaveCommentClicked(ClickEvent event) {
		formReplyBean();
		String validRes = SnipViewValidator.validateReply(replyBean, currentSnipBean);
		if (validRes != null) {
			setErrorMessage(validRes);
			return;
		}
		tribunalPresenter.saveComment(replyBean);
	}

	@UiHandler("cancelComment")
	public void onCancelCommentClicked(ClickEvent event) {
		ViewUtils.hide(leaveCommentCont);
	}

	@UiHandler("yesVoteAbuse")
	public void onYesVoteAbuse(ClickEvent event) {
		log.info("onYesVoteAbuse clicked");
	}

	@UiHandler("noVoteAbuse")
	public void onNoVoteAbuse(ClickEvent event) {
		log.info("onNoVoteAbuse clicked");
	}


	protected void formReplyBean() {

		replyBean.as().setContent(commentTextArea.getText());
		replyBean.as().setAuthor(currentUserBean.as().getName());
		replyBean.as().setParentSnip(currentSnipBean.as().getId());
		replyBean.as().setRep(0);
		replyBean.as().setCoreCat(currentSnipBean.as().getCoreCat());
		replyBean.as().setTitle("Re: " + currentSnipBean.as().getTitle());
		replyBean.as().setToken(currentUserBean.as().getToken());
		replyBean.as().setSnipType(SnipType.ABUSE_REPORT.getSnipType());
	}

	/**
	 * shows references in a tab panel with paging
	 *
	 * @param beanList list of references as bean objects
	 */
	public void showComments(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		log.info("Showing references: " + beanList.size());
		this.searchOptionsBean.as().setPageIndex(pageIndex);
		ViewUtils.hide(loadingWidget);
		showComments.setText(RDL.getI18n().hideComments());
		ViewUtils.show(commentsCont);
		ViewUtils.hide(leaveCommentCont);

		for (int j = 0; j < beanList.size(); j++) {
			//we first see is we already have an item created
			TribunalCommentRow commentListRow;
			if (itemList.size() >= j + 1) {
				//if yes we just populate the existing item
				commentListRow = itemList.get(j);
				commentListRow.populate(beanList.get(j), this.currentUserBean, this);
				ViewUtils.show(commentListRow.getParentObject());
			} else {
				//otherwise we create a new item
				LinkedGroupItem listItem = new LinkedGroupItem();
				commentListRow = new TribunalCommentRow(beanList.get(j), currentUserBean, this, listItem);
				itemList.add(commentListRow);
				listItem.setPaddingBottom(2);
				listItem.setPaddingTop(2);
				listItem.setPaddingLeft(2);
				listItem.setPaddingRight(2);
				listItem.add(commentListRow);
				listGroup.add(listItem);
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

	@Override
	public void populateDetails(AutoBean<SnipBean> snipBean) {
		this.currentSnipBean = snipBean;
		// this is the top widget, like in the list widget
		if (tribunalListRow == null) {
			tribunalListRow = new TribunalListRow(snipBean, currentUserBean,
					SnipType.fromString(snipBean.as().getSnipType()), abuseCont);
		} else {
			tribunalListRow.populate(snipBean, currentUserBean, SnipType.fromString(snipBean.as().getSnipType()));
		}
		abuseCont.add(tribunalListRow);
		//display emotions
		emoListPanel.clear();
		for (String emoStr : currentSnipBean.as().getEmotions()) {
			emoListPanel.add(ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr)));
		}
		emoListPanelReply.clear();
		//display content
		textContentArea.getElement().setInnerHTML(snipBean.as().getContent());
		//references hidden
		showComments.setText(RDL.getI18n().showComments());
		ViewUtils.hide(leaveCommentCont);
		ViewUtils.hide(commentsCont);
		tribunalPresenter.abuseVoteDisplayLogic(yesVoteAbuse, noVoteAbuse, yesVotedAbuse, noVotedAbuse);
		hideMessages();
	}

	@Override
	public void displayEmotions() {
		emoListPanelReply.clear();
		if (replyBean != null && replyBean.as().getEmotions() != null) {
			for (String emoStr : replyBean.as().getEmotions()) {
				log.info("Displaying selected emotion: " + emoStr);
				Span span = ViewUtils.buildEmoSpan(Emotion.valueOf(emoStr));
				span.setText(EmotionTranslator.getMessage(Emotion.valueOf(emoStr)));
				emoListPanelReply.add(span);
			}
		}
	}

	@Override
	public void showHideReplyButton(Boolean show) {
		ViewUtils.showHide(show, leaveComment);
	}

	@Override
	public void populateSnip(AutoBean<SnipBean> snipBean) {
		populateDetails(snipBean);
	}

	@Override
	public void showReferences(ArrayList<AutoBean<SnipBean>> beanList, int pageIndex) {
		showComments(beanList, pageIndex);
	}

	public void setTribunalPresenter(TribunalDetail.Presenter presenter) {
		this.tribunalPresenter = presenter;
	}

	public TribunalDetail.Presenter getTribunalPresenter() {
		return tribunalPresenter;
	}

	@Override
	public AutoBean<SnipBean> getSearchOptionsBean() {
		return null;
	}

	@Override
	public void setSearchOptionsBean(AutoBean<SnipBean> searchOptionsBean) {
		this.searchOptionsBean = searchOptionsBean;
	}

	@Override
	public void setPresenter(SnipView.Presenter presenter) {
		//NOOP
	}

	public SnipView.Presenter getPresenter() {
		//NOOP
		return null;
	}

	@Override
	public void saveReferenceResponseHandler(String refType, String snipType) {
		//NOOP
	}

	@Override
	public ReferenceSearchFilterWidget getFilter() {
		//NOOP
		return null;
	}

	@Override
	public void showSnipAction(Boolean isEdit, ClickHandler clickHandler) {
		//NOOP
	}

	@Override
	public void hideSnipAction() {
		//NOOP
	}

	@Override
	public void setPresenter(Presenter presenter) {
		//NOOP
	}
}

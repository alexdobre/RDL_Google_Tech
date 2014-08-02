package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.client.view.cssbundles.Resources;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.client.view.widget.EditorWidget;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.ListBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ AppMenu appMenu the upper menu view
 * @ AutoBean<CurrentUserBean> currentUserBean contains user parameters like auth state
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ AppMenu getAppMenu() returns the Nav-bar header using the user authorisation status
 * this method sets the options in the header/nav bar AppMenu widget
 * @ setloginresult(String name, String email, boolean auth) sets the options in the header/nav-bar
 * using the user's authorisation status
 */
public class SnipEditViewImpl extends Composite implements SnipEditView {

	private static Logger log = Logger.getLogger("");
	private Beanery beanery = GWT.create(Beanery.class);

	@UiTemplate("SnipEditViewImpl.ui.xml")
	interface SnipEditViewUiBinder extends UiBinder<Widget, SnipEditViewImpl> {
	}

	private static SnipEditViewUiBinder uiBinder = GWT.create(SnipEditViewUiBinder.class);

	private Presenter presenter;

	@UiField
	AppMenu appMenu;


	@UiField
	FlowPanel snipTypeBar, categoryListPanel, improvementsPanel;

	@UiField
	ListBox categoryList, proposalTypeList, proposalStateList;

	private boolean isCategoryListInit = false;
	private boolean isProposalListInit = false;
	private boolean isSnipTypeBarInit = false;

	@UiField
	org.gwtbootstrap3.client.ui.TextBox title;

	@UiField
	org.gwtbootstrap3.client.ui.Button saveSnip, deleteSnip;

	@UiField
	EditorWidget editorWidget;

	private List<RadioButton> snipTypeRadioBtnList;

	private AutoBean<CurrentUserBean> currentUserBean;
	private AutoBean<SnipBean> currentSnipBean;
	private String pageToRedirect;

	public SnipEditViewImpl(AutoBean<CurrentUserBean> currentUserBean) {
		log.info("SnipEditViewImpl constructor");
		initWidget(uiBinder.createAndBindUi(this));
		this.currentUserBean = currentUserBean;
	}

	private void configureForImprovement() {
		pageToRedirect = RDLConstants.Tokens.IMPROVEMENTS;
		if (! isProposalListInit){
			ViewUtils.createProposalTypeList(proposalTypeList);
			ViewUtils.createProposalStateList(proposalStateList);
			isProposalListInit = true;
		}
		selectProposalTypeList();
		selectProposalStateList();
	}

	private void configureForStory() {
		pageToRedirect = RDLConstants.Tokens.STORIES;
		if (!isCategoryListInit){
			createCategoryList();
			isCategoryListInit = true;
		}
		selectCategory();
	}

	private void configureForIdea() {
		pageToRedirect = RDLConstants.Tokens.SNIPS;
		if (!isSnipTypeBarInit){
			createSnipTypeBar();
			isSnipTypeBarInit = true;
		}
		if (!isCategoryListInit){
			createCategoryList();
			isCategoryListInit = true;
		}
		selectCategory();
		selectSnipTypeRadio();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		ViewUtils.hide(deleteSnip);
		title.setFocus(true);
	}

	@Override
	protected void onUnload() {
		super.onUnload();

		title.setText("");
		editorWidget.setHTML("");
		ViewUtils.hide(deleteSnip);

		categoryList.setSelectedIndex(0);
	}

	/**
	 * creates snip type top menu with radio buttons
	 */
	public void createSnipTypeBar() {
		// labels for radio buttons
		HashMap radioBtnLabels = new HashMap();
		radioBtnLabels.put(RDLConstants.SnipType.SNIP, RDL.i18n.snip());
		radioBtnLabels.put(RDLConstants.SnipType.FAST_CAP, RDL.i18n.fastCap());
		radioBtnLabels.put(RDLConstants.SnipType.MATERIAL, RDL.i18n.material());
		radioBtnLabels.put(RDLConstants.SnipType.HABIT, RDL.i18n.habit());

		// image resources for radio buttons
		HashMap imageResources = new HashMap();
		imageResources.put(RDLConstants.SnipType.SNIP, Resources.INSTANCE.SnipImage());
		imageResources.put(RDLConstants.SnipType.FAST_CAP, Resources.INSTANCE.FastCapImage());
		imageResources.put(RDLConstants.SnipType.MATERIAL, Resources.INSTANCE.MaterialImage());
		imageResources.put(RDLConstants.SnipType.HABIT, Resources.INSTANCE.HabitImage());

		snipTypeRadioBtnList = new ArrayList<RadioButton>();

		Iterator iterator = radioBtnLabels.entrySet().iterator();
		// init radio button and corresponding image
		while (iterator.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iterator.next();

			Image img = new Image(((ImageResource) imageResources.get(mEntry.getKey())).getSafeUri().asString());
			img.setWidth("40px");
			img.setHeight("40px");
			img.setStyleName("snipTypeImg");

			snipTypeBar.add(img);

			RadioButton rdBtn = new RadioButton("snipType", mEntry.getValue().toString());
			rdBtn.setStyleName("radioBtn");
			rdBtn.getElement().setAttribute("name", mEntry.getKey().toString());
			snipTypeRadioBtnList.add(rdBtn);

			snipTypeBar.add(rdBtn);

			// check the first radio button
			if (snipTypeRadioBtnList.size() == 1)
				rdBtn.setValue(true);

		}

	}

	/**
	 * creates category list
	 */
	void createCategoryList() {
		categoryList.addItem("Select a category");
		for (CoreCategory item : CoreCategory.values()) {
			categoryList.addItem(item.getShortName());
		}
	}

	/**
	 * view edited snip, set title, content, snip type, category and subcategory values
	 *
	 * @param snipBean
	 */
	public void populate(AutoBean<SnipBean> snipBean) {
		if (snipBean == null ){
			this.currentSnipBean = beanery.snipBean();
			currentSnipBean.as().setTitle("");
			currentSnipBean.as().setAuthor(currentUserBean.as().getName());
			currentSnipBean.as().setContent("");
		}else {
			this.currentSnipBean = snipBean;
		}
		configureForModule();

		deleteSnip.getElement().getStyle().setProperty("display", "");
		deleteSnip.getElement().getStyle().setProperty("marginLeft", "10px");
		title.setText(currentSnipBean.as().getTitle());
		editorWidget.setHTML(currentSnipBean.as().getContent());
	}

	private void configureForModule() {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			configureForIdea();
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			configureForStory();
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			configureForImprovement();
		}
	}

	private void selectSnipTypeRadio() {
		if (currentSnipBean == null || currentSnipBean.as().getSnipType() == null ||
				currentSnipBean.as().getSnipType().equals("")){
			//select first radio button
			snipTypeRadioBtnList.get(0).setValue(true);
		}else {
			for (RadioButton radioBtn : snipTypeRadioBtnList) {
				radioBtn.setEnabled(false);
				if (currentSnipBean.as().getSnipType().equals(radioBtn.getElement().getAttribute("name")))
					radioBtn.setValue(true);
			}
		}
	}

	private void selectProposalStateList() {
		for (int i = 0; i < proposalStateList.getItemCount(); i++) {
			if (proposalStateList.getValue(i).equals(currentSnipBean.as().getProposalState())) {
				proposalStateList.setSelectedIndex(i);
				break;
			}
		}
	}

	private void selectProposalTypeList() {
		for (int i = 0; i < proposalTypeList.getItemCount(); i++) {
			if (proposalTypeList.getValue(i).equals(currentSnipBean.as().getProposalType())) {
				proposalTypeList.setSelectedIndex(i);
				break;
			}
		}
	}

	private void selectCategory() {
		for (int i = 0; i < categoryList.getItemCount(); i++) {
			if (categoryList.getItemText(i).equals(currentSnipBean.as().getCoreCat())) {
				categoryList.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void showHideCategories(Boolean show) {
		ViewUtils.showHide(show,categoryListPanel);
	}

	@Override
	public void showHideIdeaTypes(Boolean show) {
		ViewUtils.showHide(show,snipTypeBar);
	}

	@Override
	public void showHideImprovementPanels(Boolean show) {
		ViewUtils.showHide(show,improvementsPanel);
	}

	/**
	 * save button click handler, init snip bean and sets values from UI widgets
	 * this is called also for save/edit. Sets current snip id in this case.
	 *
	 * @param event
	 */

	@UiHandler("saveSnip")
	void onSaveSnip(ClickEvent event) {
		if (title.getText().equals("")) {
			Window.alert(RDL.i18n.titleWarningText());
			return;
		}

		if (!Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS) && categoryList.getSelectedIndex() == 0) {
			Window.alert(RDL.i18n.categoryWarningText());
			return;
		}

		AutoBean<SnipBean> newBean = beanery.snipBean();
		if (currentSnipBean != null) {
			newBean = currentSnipBean;
		}
		// put snip data into the bean
		newBean.as().setTitle(title.getText());
		newBean.as().setContent(editorWidget.getHTML());

		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			// check which snip type is sets
			for (RadioButton radioBtn : snipTypeRadioBtnList) {
				if (radioBtn.getValue())
					newBean.as().setSnipType(radioBtn.getElement().getAttribute("name"));
			}

			newBean.as().setCoreCat(categoryList.getItemText(categoryList.getSelectedIndex()));
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			newBean.as().setSnipType(RDLConstants.SnipType.THREAD);
			newBean.as().setCoreCat(categoryList.getItemText(categoryList.getSelectedIndex()));
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			newBean.as().setSnipType(RDLConstants.SnipType.PROPOSAL);

			newBean.as().setProposalType(proposalTypeList.getValue(proposalTypeList.getSelectedIndex()));
			newBean.as().setProposalState(proposalStateList.getValue(proposalStateList.getSelectedIndex()));
		}

		if (currentSnipBean == null) {
			// sets counters to 0
			newBean.as().setAuthor(currentUserBean.as().getName());
			newBean.as().setViews(0);
			newBean.as().setPosRef(0);
			newBean.as().setNeutralRef(0);
			newBean.as().setNegativeRef(0);
			newBean.as().setRep(0);
			newBean.as().setPosts(0);
			newBean.as().setPledges(0);
			newBean.as().setCounters(0);

			presenter.submitBean(newBean, pageToRedirect);
		} else {
			newBean.as().setId(currentSnipBean.as().getId());
			presenter.submitEditedBean(newBean, pageToRedirect);
		}

	}

	/**
	 * delete button click handler, calls presenter's function to delete snip with the given id
	 *
	 * @param event
	 */
	@UiHandler("deleteSnip")
	void onDeleteSnip(ClickEvent event) {
		if (currentSnipBean != null) {
			/**
			 * if snip has some reference o not allow to delete it. Show popup message instead
			 */
			if (hasLinks()) {
				if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
					Window.alert(RDL.i18n.deleteSnipMsg());
				else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
					Window.alert(RDL.i18n.deleteThreadMsg());
				else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
					Window.alert(RDL.i18n.deleteThreadMsg());

			} else {
				presenter.onDeleteSnip(currentSnipBean.as().getId(), pageToRedirect);
			}
		}
	}

	/**
	 * checks if current snip has any reference, thread has posts or proposal has pledges or counters
	 *
	 * @return true if has
	 */
	private boolean hasLinks() {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
			return currentSnipBean.as().getPosRef() > 0 || currentSnipBean.as().getNeutralRef() > 0 || currentSnipBean.as().getNegativeRef() > 0;
		else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
			return currentSnipBean.as().getPosts() > 0;
		else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
			return currentSnipBean.as().getPledges() > 0 || currentSnipBean.as().getCounters() > 0;
		else
			return false;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public AppMenu getAppMenu() {
		return this.appMenu;
	}

}

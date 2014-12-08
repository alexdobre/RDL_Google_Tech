package com.therdl.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;

import java.util.HashMap;
import java.util.Map;

/**
 * WelcomeViewImpl class ia a view in the Model View Presenter Design Pattern (MVP)
 * see http://www.gwtproject.org/articles/mvp-architecture.html#view
 * this class provides GUI for the landing page
 *
 * @ Presenter presenter the  presenter for this view
 * see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ SignInViewImpl signInView, login pop up widget
 * fields below are standard GWT UIBinder display elements
 * @ FocusPanel  IdeasButton, StoriesButton, VoteButton, ServicesButton,
 * FocusPanel widgets allow complex events such as 'hover'
 * @ Image logo, logo image, note java does not support transparency layers
 * @ AutoBean<CurrentUserBean> currentUser  see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * maintains client side state
 */
@Singleton
public class WelcomeViewImpl extends AppMenuView implements WelcomeView {

	interface WelcomeViewImplUiBinder extends UiBinder<Widget, WelcomeViewImpl> {
	}

	private static WelcomeViewImplUiBinder uiBinder = GWT.create(WelcomeViewImplUiBinder.class);

	private Presenter presenter;
	private Map<CoreCategory, AutoBean<SnipBean>> snipMap = new HashMap<CoreCategory, AutoBean<SnipBean>>();
	private CoreCategory delayedDisplay;

	@UiField
	Image logo;
	@UiField
	Button compatibilityCat, connectionCat, exteriorCat, eroticismCat, seductionCat, psyTendCat, affairsCat, abuseCat;
	@UiField
	Modal compatibilityModal, connectionModal, exteriorModal, eroticismModal, seductionModal, psyTendModal, affairsModal, abuseModal;
	@UiField
	ModalBody compatibilityModalBody, connectionModalBody, exteriorModalBody, eroticismModalBody, seductionModalBody, psyTendModalBody, affairsModalBody, abuseModalBody;
	@UiField
	Column welcomeMessage;

	@Inject
	public WelcomeViewImpl(AppMenu appMenu) {
		super(appMenu);
		initWidget(uiBinder.createAndBindUi(this));
		logo.setStyleName("splashLogo");

		//core category buttons
		compatibilityCat.addStyleName("btn-cat");
		compatibilityCat.addStyleName("btn-cat-compat");
		connectionCat.addStyleName("btn-cat");
		connectionCat.addStyleName("btn-cat-conn");
		exteriorCat.addStyleName("btn-cat");
		exteriorCat.addStyleName("btn-cat-ext");
		eroticismCat.addStyleName("btn-cat");
		eroticismCat.addStyleName("btn-cat-ero");
		seductionCat.addStyleName("btn-cat");
		seductionCat.addStyleName("btn-cat-sed");
		psyTendCat.addStyleName("btn-cat");
		psyTendCat.addStyleName("btn-cat-psy");
		affairsCat.addStyleName("btn-cat");
		affairsCat.addStyleName("btn-cat-aff");
		abuseCat.addStyleName("btn-cat");
		abuseCat.addStyleName("btn-cat-abu");
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (delayedDisplay != null) {
			if (CoreCategory.COMPATIBILITY.equals(delayedDisplay)) {
				onCompatibilityCattClick(null);
			} else if (CoreCategory.CONNECTION.equals(delayedDisplay)) {
				onConnectionCatClick(null);
			} else if (CoreCategory.EXTERIOR.equals(delayedDisplay)) {
				onExteriorCatClick(null);
			} else if (CoreCategory.EROTICISM.equals(delayedDisplay)) {
				onEroticismCatClick(null);
			} else if (CoreCategory.SEDUCTION.equals(delayedDisplay)) {
				onSeductionCatClick(null);
			} else if (CoreCategory.PSY_TEND.equals(delayedDisplay)) {
				onPsyTendCatClick(null);
			} else if (CoreCategory.AFFAIRS.equals(delayedDisplay)) {
				onAffairsCatClick(null);
			} else if (CoreCategory.ABUSE.equals(delayedDisplay)) {
				onAbuseCatClick(null);
			}
			delayedDisplay = null;
		}
	}

	@Override
	public void setDelayedDisplay(CoreCategory cat) {
		delayedDisplay = cat;
	}

	@Override
	public void showWelcomeSnip(AutoBean<SnipBean> welcomeSnip, CoreCategory coreCat) {
		snipMap.put(coreCat, welcomeSnip);
		if (coreCat.equals(CoreCategory.GENERAL)) {
			welcomeMessage.getElement().setInnerHTML(welcomeSnip.as().getContent());
		} else if (coreCat.equals(CoreCategory.COMPATIBILITY)) {
			compatibilityModal.setTitle(welcomeSnip.as().getTitle());
			compatibilityModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			compatibilityModal.show();
		} else if (coreCat.equals(CoreCategory.CONNECTION)) {
			connectionModal.setTitle(welcomeSnip.as().getTitle());
			connectionModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			connectionModal.show();
		} else if (coreCat.equals(CoreCategory.EXTERIOR)) {
			exteriorModal.setTitle(welcomeSnip.as().getTitle());
			exteriorModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			exteriorModal.show();
		} else if (coreCat.equals(CoreCategory.EROTICISM)) {
			eroticismModal.setTitle(welcomeSnip.as().getTitle());
			eroticismModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			eroticismModal.show();
		} else if (coreCat.equals(CoreCategory.SEDUCTION)) {
			seductionModal.setTitle(welcomeSnip.as().getTitle());
			seductionModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			seductionModal.show();
		} else if (coreCat.equals(CoreCategory.PSY_TEND)) {
			psyTendModal.setTitle(welcomeSnip.as().getTitle());
			psyTendModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			psyTendModal.show();
		} else if (coreCat.equals(CoreCategory.AFFAIRS)) {
			affairsModal.setTitle(welcomeSnip.as().getTitle());
			affairsModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			affairsModal.show();
		} else if (coreCat.equals(CoreCategory.ABUSE)) {
			abuseModal.setTitle(welcomeSnip.as().getTitle());
			abuseModalBody.getElement().setInnerHTML(welcomeSnip.as().getContent());
			abuseModal.show();
		}
	}

	/**
	 * displays the on click popup description
	 *
	 * @param event Standard GWT hover event
	 */
	@UiHandler("compatibilityCat")
	public void onCompatibilityCattClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.COMPATIBILITY)) {
			compatibilityModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.COMPATIBILITY, null);
		}
	}

	@UiHandler("connectionCat")
	public void onConnectionCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.CONNECTION)) {
			connectionModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.CONNECTION, null);
		}
	}

	@UiHandler("exteriorCat")
	public void onExteriorCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.EXTERIOR)) {
			exteriorModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.EXTERIOR, null);
		}
	}

	@UiHandler("eroticismCat")
	public void onEroticismCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.EROTICISM)) {
			eroticismModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.EROTICISM, null);
		}
	}

	@UiHandler("seductionCat")
	public void onSeductionCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.SEDUCTION)) {
			seductionModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.SEDUCTION, null);
		}
	}

	@UiHandler("psyTendCat")
	public void onPsyTendCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.PSY_TEND)) {
			psyTendModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.PSY_TEND, null);
		}
	}

	@UiHandler("affairsCat")
	public void onAffairsCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.AFFAIRS)) {
			affairsModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.AFFAIRS, null);
		}
	}

	@UiHandler("abuseCat")
	public void onAbuseCatClick(ClickEvent event) {
		if (snipMap.keySet().contains(CoreCategory.ABUSE)) {
			abuseModal.show();
		} else {
			presenter.grabWelcomeSnip(CoreCategory.ABUSE, null);
		}
	}

	public Map<CoreCategory, AutoBean<SnipBean>> getSnipMap() {
		return snipMap;
	}
}


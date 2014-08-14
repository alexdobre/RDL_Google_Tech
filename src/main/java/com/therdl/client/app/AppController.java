package com.therdl.client.app;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.Presenter;
import com.therdl.client.presenter.ProfilePresenter;
import com.therdl.client.presenter.RegisterPresenter;
import com.therdl.client.presenter.SnipEditPresenter;
import com.therdl.client.presenter.SnipPresenter;
import com.therdl.client.presenter.SnipSearchPresenter;
import com.therdl.client.presenter.WelcomePresenter;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.SnipView;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.impl.ImprovementsViewImpl;
import com.therdl.client.view.impl.ProfileViewImpl;
import com.therdl.client.view.impl.RegisterViewImpl;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.client.view.impl.SnipViewImpl;
import com.therdl.client.view.impl.StoriesViewImpl;
import com.therdl.client.view.impl.WelcomeViewImpl;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogOutEvent;
import com.therdl.shared.events.LogOutEventEventHandler;
import com.therdl.shared.events.SnipViewEvent;
import com.therdl.shared.events.SnipViewEventHandler;

/**
 * While the overall application follows the Model View Presenter(MVP) design pattern
 * and the particular schema for MVP here known as Activities and Places
 * see http://www.gwtproject.org/doc/latest/DevGuideMvpActivitiesAndPlaces.html
 * this can be simply expressed as
 * 1. place --> view and
 * 2. activity --> user state/interaction
 * this class manages both Places and Activities and acts as a Controller class in the
 * client/view layer
 * This class controls what view is presented and when it is presented. A particular
 * view is presented during user interaction principally based on  the state of 2 principal actors
 *
 * @ RDLConstants.Tokens.<history Token Type> identifies the view to be presented
 * @ AutoBean<Auth Type>   determines the authorisation state of the view presented
 * it has utility methods and objects
 * @ Beanery beanery, the bean factory
 * @ <XX>View client side views generally UiBinder GUI classes
 * important methods
 * @ bind() manages the client side event handlers for History and authorisation state client
 * side events
 * @ onValueChange(ValueChangeEvent<String> event) ValueChangeEvents are fired when the view is changed by a user
 * see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html
 * for any  ValueChangeEvent think (History event) the type of view is found, the presenter and view are
 * created and by examining the authorisation status of the user put in focus for the user if user is authorised
 * with the correct paramaters (eg menu options) for that users given  authorisation status (eg logged in)
 */
public class AppController implements Presenter, ValueChangeHandler<String> {

	private static Logger log = Logger.getLogger("");

	private HasWidgets container;
	private Beanery beanery = GWT.create(Beanery.class);
	private AppMenu appMenu = new AppMenu();

	/**
	 * Current authentication rules are anyone can view but only registered user can edit
	 * separate authorisation from user state with 2 user beans, current user and authorised user
	 * for example auth user bean will have initial password for sign up.
	 * The current user bean as a persistent client side bean will/must not contain password info
	 * and any time for client side security
	 */

	private AutoBean<AuthUserBean> authnBean = beanery.authBean();
	private AutoBean<CurrentUserBean> currentUserBean = beanery.currentUserBean();

	private WelcomeView welcomeView;
	private SnipEditView snipEditView;

	private SearchView searchView;
	private SearchView storiesView;
	private SearchView improvementsView;

	private RegisterView registerView;
	private ProfileView profileView;

	private SnipView snipView;
	private SnipView threadView;

	private SnipView proposalView;

	public AppController() {

		bind();
	}

	/**
	 * Binds the event handler instances to their specific events
	 *
	 * @ LogOutEvent  for user log out flow
	 * @ SnipViewEvent this event creates a new SnipView, is noteworthy as it is fired after
	 * a JSNI callback from the Closure SnipListWidget code
	 */
	private void bind() {

		History.addValueChangeHandler(this);

		log.info("AppController bind() addValueChangeHandler");

		// logout event handler
		GuiEventBus.EVENT_BUS.addHandler(LogOutEvent.TYPE, new LogOutEventEventHandler() {
			@Override
			public void onLogOutEvent(LogOutEvent onLogOutEvent) {
				currentUserBean = Validation.resetCurrentUserBeanFields(currentUserBean);
				Cookies.removeCookie("sid");
				History.newItem(RDLConstants.Tokens.LOG_OUT);
				History.fireCurrentHistoryState();
			}
		});

		// SnipView event handler
		GuiEventBus.EVENT_BUS.addHandler(SnipViewEvent.TYPE, new SnipViewEventHandler() {

			@Override
			public void onSnipSelectEvent(SnipViewEvent event) {
				if (Global.moduleName.equals(RDLConstants.Modules.IDEAS))
					History.newItem(RDLConstants.Tokens.SNIP_VIEW + ":" + event.getSnipId());
				else if (Global.moduleName.equals(RDLConstants.Modules.STORIES))
					History.newItem(RDLConstants.Tokens.THREAD_VIEW + ":" + event.getSnipId());
				else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS))
					History.newItem(RDLConstants.Tokens.PROPOSAL_VIEW + ":" + event.getSnipId());
			}
		});


	}

	/**
	 * present the Welcome landing page for a unAuthorised user
	 *
	 * @param rootContainer the Presenter View
	 */

	@Override
	public void go(final HasWidgets rootContainer) {
		this.container = rootContainer;
		this.currentUserBean.as().setAuth(false);
		this.currentUserBean.as().setRegistered(false);
		if ("".equals(History.getToken())) {
			History.newItem(RDLConstants.Tokens.STORIES);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		this.go(container);
	}

	/**
	 * returns WelcomeView, if is null, initialize
	 *
	 * @return WelcomeView
	 */
	public WelcomeView getWelcomeView() {
		if (welcomeView == null) {
			welcomeView = new WelcomeViewImpl(currentUserBean, appMenu);
			WelcomePresenter welcomePresenter = new WelcomePresenter(welcomeView, this);
		}
		return welcomeView;
	}

	/**
	 * This binds the history tokens with the different application states
	 *
	 * @param event ValueChangeEvent  see http://www.gwtproject.org/doc/latest/DevGuideCodingBasicsHistory.html
	 *              for ValueChangeEvent think HistoryEvent
	 *              String tokens are extracted from the HistoryEvents, this allows the correct view to be constructed and presented
	 *              this method provides the machinery
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		log.info("AppController onValueChange token before split is  " + token);
		String[] tokenSplit = token.split(":");

		if (tokenSplit.length != 0) {
			token = tokenSplit[0];
		}

		log.info("AppController onValueChange token is  " + token);
		if (token != null) {
			switch (token) {
			case RDLConstants.Tokens.WELCOME:
				showWelcomeView();
				break;
			case RDLConstants.Tokens.SNIP_VIEW:
				showSnipView(tokenSplit);
				break;
			case RDLConstants.Tokens.THREAD_VIEW:
				showThreadView(tokenSplit);
				break;
			case RDLConstants.Tokens.SNIPS:
				showSnips(event, tokenSplit);
				break;
			case RDLConstants.Tokens.STORIES:
				showStories(event, tokenSplit);
				break;
			case RDLConstants.Tokens.THREAD_EDIT:
				showThreadEdit(tokenSplit);
				break;
			case RDLConstants.Tokens.IMPROVEMENTS:
				showImprovements(event, tokenSplit);
				break;
			case RDLConstants.Tokens.PROPOSAL_EDIT:
				showProposalEdit(tokenSplit);
				break;
			case RDLConstants.Tokens.PROPOSAL_VIEW:
				showProposalView(tokenSplit);
				break;
			case RDLConstants.Tokens.SNIP_EDIT:
				showSnipEdit(tokenSplit);
				break;
			case RDLConstants.Tokens.SIGN_UP:
				showSignUp();
				break;
			case RDLConstants.Tokens.PROFILE:
				showProfile();
				break;
			case RDLConstants.Tokens.LOG_OUT:
				showLogOut();
				break;
			default:
				showWelcomeView();
				break;
			}
		}
	}

	private void showWelcomeView() {
		log.info("AppController Tokens.WELCOME");
		if (welcomeView == null) {
			welcomeView = new WelcomeViewImpl(currentUserBean, appMenu);
		}
		final WelcomePresenter welcomePresenter = new WelcomePresenter(welcomeView, this);
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				welcomePresenter.go(container, currentUserBean);
			}
		});
	}

	private void showSnipView(String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.IDEAS;
		log.info("AppController Tokens.SNIP_VIEW");

		String currentSnipId = "";
		if (tokenSplit.length == 2) {
			currentSnipId = tokenSplit[1];
		}

		if (snipView == null) {
			snipView = new SnipViewImpl(currentUserBean, appMenu);
		}

		final SnipPresenter snipPresenter = new SnipPresenter(snipView, currentSnipId, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.SNIPS);
			}

			public void onSuccess() {
				snipPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showThreadView(String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.STORIES;
		log.info("AppController Tokens.THREAD_VIEW");

		String currentSnipId = "";
		if (tokenSplit.length == 2) {
			currentSnipId = tokenSplit[1];
		}

		if (threadView == null) {
			threadView = new SnipViewImpl(currentUserBean, appMenu);
		}

		final SnipPresenter snipPresenter = new SnipPresenter(threadView, currentSnipId, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.THREAD_VIEW);
			}

			public void onSuccess() {
				snipPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showSnips(ValueChangeEvent<String> event, String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.IDEAS;

		log.info("AppController Tokens.SNIPS");

		if (searchView == null) {
			searchView = new SnipSearchViewImpl(currentUserBean, appMenu);
		}

		searchView.setToken(event.getValue());
		if (tokenSplit.length == 2)
			searchView.setAuthorName(tokenSplit[1]);

		final SnipSearchPresenter snipSearchPresenter = new SnipSearchPresenter(searchView, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.SNIPS);
			}

			public void onSuccess() {

				snipSearchPresenter.go(container, currentUserBean);

			}
		});
	}

	private void showStories(ValueChangeEvent<String> event, String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.STORIES;

		log.info("AppController Tokens.STORIES");

		if (storiesView == null) {
			storiesView = new StoriesViewImpl(currentUserBean, appMenu);
		}
		storiesView.setToken(event.getValue());
		if (tokenSplit.length == 2)
			storiesView.setAuthorName(tokenSplit[1]);

		final SnipSearchPresenter storiesPresenter = new SnipSearchPresenter(storiesView, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
			}

			public void onSuccess() {
				storiesPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showThreadEdit(String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.STORIES;
		String currentSnipId = "";
		if (tokenSplit.length == 2) {
			currentSnipId = tokenSplit[1];
		}

		log.info("AppController Tokens.THREAD_EDIT token=" + tokenSplit[0] + ";currentSnipId=" + currentSnipId +
				"Current user auth: " + currentUserBean.as().isAuth() + "SnipEditViewImpl " + snipEditView);

		if (snipEditView == null) {
			snipEditView = new SnipEditViewImpl(currentUserBean, appMenu);
		}
		final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, currentSnipId, this);
		log.info("Doing async call...");
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.THREAD_VIEW);
			}

			public void onSuccess() {
				if (currentUserBean.as().isAuth()) {
					log.info("Invoking snip edit presenter...");
					snipEditPresenter.go(container, currentUserBean);
				} else {
					History.newItem(RDLConstants.Tokens.ERROR);
				}
			}
		});
	}

	private void showImprovements(ValueChangeEvent<String> event, String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.IMPROVEMENTS;

		log.info("AppController Tokens.IMPROVEMENTS");

		if (improvementsView == null) {
			improvementsView = new ImprovementsViewImpl(currentUserBean, appMenu);
		}
		improvementsView.setToken(event.getValue());
		if (tokenSplit.length == 2)
			improvementsView.setAuthorName(tokenSplit[1]);

		final SnipSearchPresenter improvementsPresenter = new SnipSearchPresenter(improvementsView, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
			}

			public void onSuccess() {
				improvementsPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showProposalEdit(String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.IMPROVEMENTS;

		String currentSnipId = "";
		if (tokenSplit.length == 2) {
			currentSnipId = tokenSplit[1];
		}

		log.info("AppController Tokens.PROPOSAL_EDIT token=" + tokenSplit[0] + ";currentSnipId=" + currentSnipId);

		if (snipEditView == null) {
			snipEditView = new SnipEditViewImpl(currentUserBean, appMenu);
		}
		final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, currentSnipId, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				if (currentUserBean.as().isAuth()) {
					snipEditPresenter.go(container, currentUserBean);
				} else {
					History.newItem(RDLConstants.Tokens.WELCOME);
				}
			}
		});
	}

	private void showSnipEdit(String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.IDEAS;

		String currentSnipId = "";
		if (tokenSplit.length == 2) {
			currentSnipId = tokenSplit[1];
		}
		log.info("AppController Tokens.SNIP_EDIT token=" + tokenSplit[0] + ";currentSnipId=" + currentSnipId);

		if (snipEditView == null) {
			snipEditView = new SnipEditViewImpl(currentUserBean, appMenu);
		}
		final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, currentSnipId, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				if (currentUserBean.as().isAuth()) {
					snipEditPresenter.go(container, currentUserBean);
				}
			}
		});
	}

	private void showSignUp() {
		currentUserBean = Validation.resetCurrentUserBeanFields(currentUserBean);
		if (registerView == null) {
			registerView = new RegisterViewImpl(appMenu);

		}

		final RegisterPresenter registerPresenter = new RegisterPresenter(registerView, this);
		log.info("AppController Tokens.SIGN_UP ");
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				registerPresenter.go(container, currentUserBean);

			}
		});
	}

	private void showProfile() {
		if (profileView == null) {
			log.info("AppController profileView == null ");
			profileView = new ProfileViewImpl(currentUserBean, appMenu);
		}

		final ProfilePresenter profilePresenter = new ProfilePresenter(profileView, this);
		log.info("AppController Tokens.SERVICES ");
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				profilePresenter.go(container, currentUserBean);

			}
		});
	}

	private void showLogOut() {
		log.info("AppController Tokens.LOG_OUT ");

		if (welcomeView == null) {
			welcomeView = new WelcomeViewImpl(currentUserBean, appMenu);
		}

		final WelcomePresenter welcomePresenter = new WelcomePresenter(welcomeView, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {

				currentUserBean.as().setAuth(false);
				welcomePresenter.go(container, currentUserBean);
			}
		});
	}

	private void showProposalView(String[] tokenSplit) {
		Global.moduleName = RDLConstants.Modules.IMPROVEMENTS;
		log.info("AppController Tokens.PROPOSAL_VIEW");

		String currentSnipId = "";
		if (tokenSplit.length == 2) {
			currentSnipId = tokenSplit[1];
		}

		if (proposalView == null) {
			proposalView = new SnipViewImpl(currentUserBean, appMenu);
		}

		final SnipPresenter snipPresenter = new SnipPresenter(proposalView, currentSnipId, this);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.THREAD_VIEW);
			}

			public void onSuccess() {
				//     if (currentUserBean.as().isAuth()) {
				snipPresenter.go(container, currentUserBean);
				//    } else {
				//        History.newItem(RDLConstants.Tokens.WELCOME);
				//    }

			}
		});
	}

	public AutoBean<CurrentUserBean> getCurrentUserBean() {
		return currentUserBean;
	}


	/**
	 * sets the currentUserBean for the  view in the WelcomePresenter dologin method
	 * the values below are all strings extracted from a simple form input
	 *
	 * @param name       String  for user name
	 * @param email      String  for user email
	 * @param state      boolean for state information
	 * @param titleBeans user titles
	 */
	public void setCurrentUserBean(String name, String email, boolean state, List<UserBean.TitleBean> titleBeans, boolean isRDLSupporter) {
		this.currentUserBean.as().setAuth(state);
		this.currentUserBean.as().setName(name);
		this.currentUserBean.as().setEmail(email);
		this.currentUserBean.as().setTitles(titleBeans);
		this.currentUserBean.as().setIsRDLSupporter(isRDLSupporter);
	}


	/**
	 * set in the RegisterPresenter onResponseReceived for  the server callback 'submitNewUser'
	 * the values below are all strings extracted from a simple form input
	 *
	 * @param name  String for user name
	 * @param email String for user email
	 * @param state booleam for state information
	 */
	public void setCurrentUserBean(String name, String email, boolean state) {
		this.currentUserBean.as().setAuth(state);
		this.currentUserBean.as().setName(name);
		this.currentUserBean.as().setEmail(email);

	}


}

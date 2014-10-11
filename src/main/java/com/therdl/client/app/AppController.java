package com.therdl.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.presenter.ContentNotFoundPresenter;
import com.therdl.client.presenter.ContentSearchPresenter;
import com.therdl.client.presenter.IdeaViewPresenter;
import com.therdl.client.presenter.ImprovementViewPresenter;
import com.therdl.client.presenter.LicensePresenter;
import com.therdl.client.presenter.Presenter;
import com.therdl.client.presenter.ProfilePresenter;
import com.therdl.client.presenter.PublicProfilePresenter;
import com.therdl.client.presenter.RdlAbstractPresenter;
import com.therdl.client.presenter.RegisterPresenter;
import com.therdl.client.presenter.SnipEditPresenter;
import com.therdl.client.presenter.SnipSearchPresenter;
import com.therdl.client.presenter.ThreadViewPresenter;
import com.therdl.client.presenter.TribunalDetailPresenter;
import com.therdl.client.presenter.TribunalPresenter;
import com.therdl.client.presenter.WelcomePresenter;
import com.therdl.client.presenter.runt.impl.ProfileDescRuntImpl;
import com.therdl.client.view.ContentNotFound;
import com.therdl.client.view.ContentSearchView;
import com.therdl.client.view.IdeaView;
import com.therdl.client.view.ImprovementView;
import com.therdl.client.view.LicenseView;
import com.therdl.client.view.ProfileView;
import com.therdl.client.view.PublicProfileView;
import com.therdl.client.view.RegisterView;
import com.therdl.client.view.SearchView;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.ThreadView;
import com.therdl.client.view.TribunalDetail;
import com.therdl.client.view.TribunalView;
import com.therdl.client.view.WelcomeView;
import com.therdl.client.view.impl.ContentNotFoundImpl;
import com.therdl.client.view.impl.ContentSearchViewImpl;
import com.therdl.client.view.impl.IdeaViewImpl;
import com.therdl.client.view.impl.ImprovementViewImpl;
import com.therdl.client.view.impl.ImprovementsViewImpl;
import com.therdl.client.view.impl.LicenseViewImpl;
import com.therdl.client.view.impl.ProfileViewImpl;
import com.therdl.client.view.impl.PublicProfileViewImpl;
import com.therdl.client.view.impl.RegisterViewImpl;
import com.therdl.client.view.impl.SnipEditViewImpl;
import com.therdl.client.view.impl.SnipSearchViewImpl;
import com.therdl.client.view.impl.StoriesViewImpl;
import com.therdl.client.view.impl.ThreadViewImpl;
import com.therdl.client.view.impl.TribunalDetailImpl;
import com.therdl.client.view.impl.TribunalViewImpl;
import com.therdl.client.view.impl.WelcomeViewImpl;
import com.therdl.client.view.widget.AppMenu;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.LogOutEvent;
import com.therdl.shared.events.LogOutEventEventHandler;
import com.therdl.shared.events.SnipViewEvent;
import com.therdl.shared.events.SnipViewEventHandler;

import java.util.List;
import java.util.logging.Logger;

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

	private static Logger log = Logger.getLogger(AppController.class.getName());

	private HasWidgets container;
	private Beanery beanery = GWT.create(Beanery.class);
	private AppMenu appMenu;

	/**
	 * Current authentication rules are anyone can view but only registered user can edit
	 * separate authorisation from user state with 2 user beans, current user and authorised user
	 * for example auth user bean will have initial password for sign up.
	 * The current user bean as a persistent client side bean will/must not contain password info
	 * and any time for client side security
	 */

	private AutoBean<CurrentUserBean> currentUserBean = beanery.currentUserBean();

	private WelcomeView welcomeView;
	private ContentNotFound contentNotFound;
	private SnipEditView snipEditView;

	private SearchView searchView;
	private SearchView storiesView;
	private TribunalView tribunalView;
	private TribunalDetail tribunalDetail;
	private SearchView improvementsView;

	private RegisterView registerView;
	private ContentSearchView contentSearchView;
	private ProfileView profileView;
	private PublicProfileView publicProfileView;

	private IdeaView ideaView;
	private ThreadView threadView;
	private ImprovementView proposalView;
	private LicenseView licenseView;

	private RdlAbstractPresenter defaultPresenter;

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
				currentUserBean = RDLUtils.resetCurrentUserBeanFields(currentUserBean);
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
		if (defaultPresenter == null) {
			defaultPresenter = new RdlAbstractPresenter(this) {
				@Override
				public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
					//noop implementation
				}
			};
			appMenu = new AppMenu(defaultPresenter);
		}
		if ("".equals(History.getToken())) {
			History.newItem(RDLConstants.Tokens.WELCOME);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void go(HasWidgets container, AutoBean<CurrentUserBean> currentUserBean) {
		this.go(container);
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

		String moduleName = null;
		if (tokenSplit.length != 0) {
			moduleName = tokenSplit[0];
		}
		log.info("App controller module name: " + moduleName);
		if (moduleName != null) {
			switch (moduleName) {
				case RDLConstants.Tokens.WELCOME:
					showWelcomeView();
					break;
				case RDLConstants.Tokens.SNIP_VIEW:
					showSnipView(token);
					break;
				case RDLConstants.Tokens.THREAD_VIEW:
					showThreadView(token);
					break;
				case RDLConstants.Tokens.SNIPS:
					showSnips(token);
					break;
				case RDLConstants.Tokens.STORIES:
					showStories(token);
					break;
				case RDLConstants.Tokens.TRIBUNAL:
					showTribunal(token);
					break;
				case RDLConstants.Tokens.TRIBUNAL_DETAIL:
					showTribunalDetail(token);
					break;
				case RDLConstants.Tokens.THREAD_EDIT:
					showThreadEdit(token);
					break;
				case RDLConstants.Tokens.IMPROVEMENTS:
					showImprovements(token);
					break;
				case RDLConstants.Tokens.PROPOSAL_EDIT:
					showProposalEdit(token);
					break;
				case RDLConstants.Tokens.PROPOSAL_VIEW:
					showProposalView(token);
					break;
				case RDLConstants.Tokens.SNIP_EDIT:
					showSnipEdit(token);
					break;
				case RDLConstants.Tokens.SIGN_UP:
					showSignUp();
					break;
				case RDLConstants.Tokens.PROFILE:
					showProfile();
					break;
				case RDLConstants.Tokens.PUBLIC_PROFILE:
					showPublicProfile(tokenSplit);
					break;
				case RDLConstants.Tokens.LICENSE:
					showLicense();
					break;
				case RDLConstants.Tokens.CONTENT_SEARCH:
					showContentSearch(tokenSplit);
					break;
				case RDLConstants.Tokens.ERROR:
					showContentNotFound();
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

	private void showSnipView(String token) {
		Global.moduleName = RDLConstants.Modules.IDEAS;
		log.info("AppController Tokens.SNIP_VIEW");
		if (ideaView == null) {
			ideaView = new IdeaViewImpl(currentUserBean, appMenu);
		}
		final IdeaViewPresenter ideaViewPresenter = new IdeaViewPresenter(ideaView, this, token);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.SNIPS);
			}

			public void onSuccess() {
				ideaViewPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showThreadView(String token) {
		Global.moduleName = RDLConstants.Modules.STORIES;
		log.info("AppController Tokens.THREAD_VIEW token: " + token);
		if (threadView == null) {
			threadView = new ThreadViewImpl(currentUserBean, appMenu);
		}
		final ThreadViewPresenter threadViewPresenter = new ThreadViewPresenter(threadView, this, token);

		GWT.runAsync(new RunAsyncCallback() {
			public void onSuccess() {
				threadViewPresenter.go(container, currentUserBean);
			}

			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.THREAD_VIEW);
			}
		});
	}

	private void showSnips(String token) {
		Global.moduleName = RDLConstants.Modules.IDEAS;
		log.info("AppController Tokens.SNIPS");
		if (searchView == null) {
			searchView = new SnipSearchViewImpl(currentUserBean, appMenu);
		}

		final SnipSearchPresenter snipSearchPresenter = new SnipSearchPresenter(searchView, this, token);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.SNIPS);
			}

			public void onSuccess() {

				snipSearchPresenter.go(container, currentUserBean);

			}
		});
	}

	private void showStories(String token) {
		Global.moduleName = RDLConstants.Modules.STORIES;
		log.info("AppController Tokens.STORIES");
		if (storiesView == null) {
			storiesView = new StoriesViewImpl(currentUserBean, appMenu);
		}
		final SnipSearchPresenter storiesPresenter = new SnipSearchPresenter(storiesView, this, token);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
			}

			public void onSuccess() {
				storiesPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showTribunal(String token) {
		Global.moduleName = RDLConstants.Modules.TRIBUNAL;
		log.info("AppController Tokens.TRIBUNAL");
		if (tribunalView == null) {
			tribunalView = new TribunalViewImpl(currentUserBean, appMenu);
		}
		final TribunalPresenter tribunalPresenter = new TribunalPresenter(tribunalView, this);
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
			}

			public void onSuccess() {
				tribunalPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showTribunalDetail(String token) {
		Global.moduleName = RDLConstants.Modules.TRIBUNAL;
		log.info("AppController Tokens.TRIBUNAL_DETAIL");
		if (tribunalDetail == null) {
			tribunalDetail = new TribunalDetailImpl(currentUserBean, appMenu);
		}
		final TribunalDetailPresenter tribunalDetailPresenter = new TribunalDetailPresenter(tribunalDetail, this, token);
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
			}

			public void onSuccess() {
				tribunalDetailPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showThreadEdit(String token) {
		Global.moduleName = RDLConstants.Modules.STORIES;
		if (snipEditView == null) {
			snipEditView = new SnipEditViewImpl(currentUserBean, appMenu);
		}
		final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, this, token);
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

	private void showImprovements(String token) {
		Global.moduleName = RDLConstants.Modules.IMPROVEMENTS;
		log.info("AppController Tokens.IMPROVEMENTS");
		if (improvementsView == null) {
			improvementsView = new ImprovementsViewImpl(currentUserBean, appMenu);
		}
		final SnipSearchPresenter improvementsPresenter = new SnipSearchPresenter(improvementsView, this, token);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.WELCOME);
			}

			public void onSuccess() {
				improvementsPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showProposalEdit(String token) {
		Global.moduleName = RDLConstants.Modules.IMPROVEMENTS;
		if (snipEditView == null) {
			snipEditView = new SnipEditViewImpl(currentUserBean, appMenu);
		}
		final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, this, token);

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

	private void showSnipEdit(String token) {
		Global.moduleName = RDLConstants.Modules.IDEAS;
		if (snipEditView == null) {
			snipEditView = new SnipEditViewImpl(currentUserBean, appMenu);
		}
		final SnipEditPresenter snipEditPresenter = new SnipEditPresenter(snipEditView, this, token);

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
		currentUserBean = RDLUtils.resetCurrentUserBeanFields(currentUserBean);
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

		final ProfilePresenter profilePresenter = new ProfilePresenter(profileView, this, new ProfileDescRuntImpl());
		log.info("AppController Tokens.PROFILE ");
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				profilePresenter.go(container, currentUserBean);

			}
		});
	}

	private void showPublicProfile(String[] tokenSplit) {
		if (publicProfileView == null) {
			publicProfileView = new PublicProfileViewImpl(currentUserBean, appMenu);
		}
		final PublicProfilePresenter publicProfilePresenter = new PublicProfilePresenter(publicProfileView, this,
				tokenSplit.length > 1 ? tokenSplit[1] : null);
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				publicProfilePresenter.go(container, currentUserBean);
			}
		});
	}

	private void showLicense() {
		if (licenseView == null) {
			licenseView = new LicenseViewImpl(currentUserBean, appMenu);
		}
		final LicensePresenter licensePresenter = new LicensePresenter(licenseView, this);
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				licensePresenter.go(container, currentUserBean);
			}
		});
	}

	private void showContentSearch(String[] tokenSplit) {
		if (contentSearchView == null) {
			log.info("AppController contentSearchView == null ");
			contentSearchView = new ContentSearchViewImpl(appMenu);
		}
		final ContentSearchPresenter contentSearchPresenter = new ContentSearchPresenter(contentSearchView, this,
				tokenSplit.length > 1 ? tokenSplit[1] : null);
		log.info("AppController Tokens.CONTENT_SEARCH ");
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				contentSearchPresenter.go(container, currentUserBean);
			}
		});
	}

	private void showContentNotFound() {
		log.info("AppController Tokens.ERROR ");
		if (contentNotFound == null) {
			contentNotFound = new ContentNotFoundImpl(appMenu);
		}
		final ContentNotFoundPresenter contentNotFoundPresenter = new ContentNotFoundPresenter(contentNotFound, this);
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				contentNotFoundPresenter.go(container, currentUserBean);
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
				welcomePresenter.go(container, currentUserBean);
			}
		});
	}

	private void showProposalView(String token) {
		Global.moduleName = RDLConstants.Modules.IMPROVEMENTS;
		log.info("AppController Tokens.PROPOSAL_VIEW");
		if (proposalView == null) {
			proposalView = new ImprovementViewImpl(currentUserBean, appMenu);
		}
		final ImprovementViewPresenter improvementViewPresenter = new ImprovementViewPresenter(proposalView, this, token);

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				log.info("AppController GWT.runAsync onFailure " + RDLConstants.Tokens.THREAD_VIEW);
			}

			public void onSuccess() {
				improvementViewPresenter.go(container, currentUserBean);
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
	public void setCurrentUserBean(String name, String email, boolean state, List<UserBean.TitleBean> titleBeans,
	                               boolean isRDLSupporter, String token, Integer rep, String dateCreated) {
		this.currentUserBean.as().setAuth(state);
		this.currentUserBean.as().setName(name);
		this.currentUserBean.as().setEmail(email);
		this.currentUserBean.as().setTitles(titleBeans);
		this.currentUserBean.as().setIsRDLSupporter(isRDLSupporter);
		this.currentUserBean.as().setToken(token);
		this.currentUserBean.as().setRep(rep);
		this.currentUserBean.as().setDateCreated(dateCreated);
	}


	/**
	 * set in the RegisterPresenter onResponseReceived for  the server callback 'submitNewUser'
	 * the values below are all strings extracted from a simple form input
	 *
	 * @param name  String for user name
	 * @param email String for user email
	 * @param state booleam for state information
	 */
	public void setCurrentUserBean(String name, String email, boolean state, String token) {
		this.currentUserBean.as().setAuth(state);
		this.currentUserBean.as().setName(name);
		this.currentUserBean.as().setEmail(email);
		this.currentUserBean.as().setToken(token);
	}

	public HasWidgets getRootContainer() {
		return container;
	}

	public AppMenu getAppMenu() {
		return appMenu;
	}

	public RdlAbstractPresenter getDefaultPresenter() {
		return defaultPresenter;
	}
}

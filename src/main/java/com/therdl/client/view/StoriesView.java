package com.therdl.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.widget.AppMenu;

/**
 * see com.therdl.client.view.impl.StoriesViewImpl javadoc for the
 * implementation of these methods.
 *
 * @ Presenter,  a presenter type see http://www.gwtproject.org/articles/mvp-architecture.html#presenter
 * @ void setPresenter(Presenter presenter)  sets the presenter for the view,
 * as the presenter handles all the strictly non view related code (server calls for instance) a view
 * can use a instance of its presenter
 * @ AppMenu getAppMenu() returns the Nav-bar header using the user authorisation status
 */
public interface StoriesView extends RdlView{

	public interface Presenter {

	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	AppMenu getAppMenu();

}

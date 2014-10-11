package com.therdl.client.view;

/**
 * The user's public profile description
 */
public interface PublicProfileView extends RdlView{

	public interface Presenter {

	}

	public void populateProfileDescription (String content);

	void setPresenter(Presenter presenter);
}

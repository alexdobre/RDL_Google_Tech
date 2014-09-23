package com.therdl.client.view;

/**
 * The tribunal view
 */
public interface TribunalView extends RdlView {

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	Presenter getPresenter();
}

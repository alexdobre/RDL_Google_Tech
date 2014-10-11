package com.therdl.client.view;

/**
 * The license view
 */
public interface LicenseView extends RdlView{

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	public void populateLicense (String content);
}

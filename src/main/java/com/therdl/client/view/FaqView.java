package com.therdl.client.view;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;

/**
 * The FAQ view
 */
public interface FaqView extends RdlView{

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	public void populateFaq (List<AutoBean<SnipBean>> faqList);

	/**
	 * Is the FAQ list already populated
	 *
	 * @return true if the list has already been populated
	 */
	public boolean isPopulated ();
}

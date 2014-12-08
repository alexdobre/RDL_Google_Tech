package com.therdl.client.view;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.PanelBody;

/**
 * The Become an RDLSupporter view
 *
 * @author alex
 */
public interface SubscribeView extends RdlView {

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);

	/**
	 * Has the description been populated
	 *
	 * @return
	 */
	public boolean isPopulated();

	public void setPopulated(boolean populated);

	public Column getFooter();

	public PanelBody getDescBody();

}

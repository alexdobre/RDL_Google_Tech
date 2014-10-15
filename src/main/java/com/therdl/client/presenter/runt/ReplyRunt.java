package com.therdl.client.presenter.runt;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.handler.ClickHandler;
import com.therdl.client.handler.RequestObserver;
import com.therdl.client.view.widget.SnipActionWidget;
import com.therdl.client.view.widget.runtized.ReferenceListRow;
import com.therdl.shared.beans.CurrentUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Deals with logic behind replies, references etc.
 */
public interface ReplyRunt {

	/**
	 * Decorates a reference with it's appropriate possible snip action
	 *
	 * @param referenceListRow the reference
	 */
	public void decorateReference(ReferenceListRow referenceListRow);

	/**
	 * User edits a reference on the spot
	 *
	 * @param referenceListRow the reference
	 */
	public void editReference(ReferenceListRow referenceListRow, SnipActionWidget snipActionWidget);

	/**
	 * User cancels the edit reference - redo the view
	 */
	public void cancelEditReference();

	/**
	 * Save a reference after editing full with adjustment of the parent
	 * pos/neg reference count if necessary
	 */
	public void saveEditedReference();

	/**
	 * The logic behind the actions a user can take on a snip
	 *
	 * @param currentUserBean  the current user
	 * @param snipBean         the snip to be acted upon
	 * @param snipActionWidget the snip action widget
	 * @param editHandler      what happens when the user clicks the edit snip button
	 * @param requestObserver  what happens when the edit server request is successful or has an error
	 */
	public void snipActionLogic(final AutoBean<CurrentUserBean> currentUserBean, final AutoBean<SnipBean> snipBean,
			SnipActionWidget snipActionWidget, ClickHandler editHandler, final RequestObserver requestObserver);

	/**
	 * Logic behind the different ways of showing the snip action widget
	 *
	 * @param isEdit           the user can edit the snip
	 * @param snipActionWidget the widget
	 * @param clickHandler     what happens when the button is clicked (edit or give rep)
	 */
	public void showSnipAction(Boolean isEdit, SnipActionWidget snipActionWidget, ClickHandler clickHandler);
}

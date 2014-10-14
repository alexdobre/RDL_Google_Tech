package com.therdl.client.presenter.runt;

import com.therdl.client.view.widget.runtized.ReferenceListRow;

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
	public void editReference (ReferenceListRow referenceListRow);

	/**
	 * User cancels the edit reference - redo the view
	 * @param referenceListRow the reference
	 */
	public void cancelEditReference (ReferenceListRow referenceListRow);

	/**
	 * Save a reference after editing full with adjustment of the parent
	 * pos/neg reference count if necessary
	 *
	 * @param referenceListRow
	 */
	public void saveEditedReference (ReferenceListRow referenceListRow);
}

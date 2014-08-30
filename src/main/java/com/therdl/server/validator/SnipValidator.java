package com.therdl.server.validator;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.exceptions.SnipValidationException;
import com.therdl.shared.exceptions.TokenInvalidException;

/**
 * Holds snips validation
 */
public interface SnipValidator {

	public void validateSnipBean(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException;

	public void validateCanDelete(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException;

	public void validateCanSaveRef(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException;

	public void validateCanGiveRep (AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException;
}

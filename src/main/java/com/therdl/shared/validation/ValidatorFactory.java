package com.therdl.shared.validation;

import javax.validation.Validator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.SnipBean;

/**
 * Validator marker for the RDL project. Only the classes and groups listed
 * in the {@link com.google.gwt.validation.client.GwtValidation} annotation can be validated.
 */
public class ValidatorFactory extends AbstractGwtValidatorFactory {

	@GwtValidation({AuthUserBean.class, SnipBean.class})
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}
}

package com.therdl.client.validation;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.SnipBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Validation methods for the snip
 */
public class SnipViewValidator {
	private static Logger log = Logger.getLogger(SnipViewValidator.class.getName());

	private static Map<String, String> errorMessageMap;

	static {
		errorMessageMap = new HashMap<String, String>();
		errorMessageMap.put("title", RDL.getI18n().formErrorTitleFormat());
		errorMessageMap.put("content", RDL.getI18n().formErrorContentFormat());
		errorMessageMap.put("coreCat", RDL.getI18n().formErrorCoreCat());
	}

	public static String validateSnipBean(AutoBean<SnipBean> bean) {
		log.info("Validating SnipBean: " + bean.as().getTitle());
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<SnipBean>> violations = validator.validate(bean.as());
		if (violations.iterator().hasNext()) {
			ConstraintViolation violation = violations.iterator().next();
			log.info("Bean invalid: " + violation.getPropertyPath() + " " + violation.getMessage());
			return errorMessageMap.get(violation.getPropertyPath().toString());
		} else {
			//see if category is valid
			if (!CoreCategory.stringIsCateg(bean.as().getCoreCat())){
				log.info("Core cat found invalid: "+bean.as().getCoreCat());
				return errorMessageMap.get("coreCat");
			}
			//see if type is valid
			if (!SnipType.stringIsType(bean.as().getSnipType())){
				log.info("Snip type found invalid: "+bean.as().getSnipType());
				return RDL.getI18n().formErrorSnipType();
			}
		}
		log.info("Bean valid");
		return null;
	}

	/**
	 * This provides superficial client side validation (only checks author name)
	 * Full validation is provided on the server side
	 * @param bean
	 * @param authorName
	 * @return
	 */
	public static String validateCanDelete (AutoBean<SnipBean> bean, String authorName){
		if (bean.as()!= null){
			if (!bean.as().getAuthor().equals(authorName)){
				return RDL.getI18n().serverErrorC000();
			}
		}
		return null;
	}

}

package com.therdl.client.validation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.RDL;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.Global;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.SnipBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Validation methods for the snip
 */
public class SnipViewValidator {

	private static Map<String, String> errorMessageMap;

	static {
		errorMessageMap = new HashMap<String, String>();
		errorMessageMap.put("title", RDL.getI18n().formErrorTitleFormat());
		errorMessageMap.put("content", RDL.getI18n().formErrorContentFormat());
		errorMessageMap.put("coreCat", RDL.getI18n().formErrorCoreCat());
	}

	public static String validateSnipBean(AutoBean<SnipBean> bean) {
		Log.info("Validating SnipBean: " + bean.as().getTitle());
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<SnipBean>> violations = validator.validate(bean.as());
		if (violations.iterator().hasNext()) {
			ConstraintViolation violation = violations.iterator().next();
			Log.info("Bean invalid: " + violation.getPropertyPath() + " " + violation.getMessage());
			return errorMessageMap.get(violation.getPropertyPath().toString());
		} else {
			//see if category is valid
			if (!CoreCategory.stringIsCateg(bean.as().getCoreCat())) {
				Log.info("Core cat found invalid: " + bean.as().getCoreCat());
				return errorMessageMap.get("coreCat");
			}
			//see if type is valid
			if (!SnipType.stringIsType(bean.as().getSnipType())) {
				Log.info("Snip type found invalid: " + bean.as().getSnipType());
				return RDL.getI18n().formErrorSnipType();
			}
			//must have at least one emotion
			if (bean.as().getEmotions() == null || bean.as().getEmotions().isEmpty()) {
				Log.info("Must select at least one emotion");
				return RDL.getI18n().formErrorNoEmotion();
			}
			//length of content 5-200k char
			int contentLength = bean.as().getContent().length();
			Log.info("Content length: "+contentLength);
			if (contentLength < 5 || contentLength > 200000) {
				return RDL.getI18n().formErrorContentFormat();
			}
		}
		Log.info("Bean valid");
		return null;
	}

	public static String validateReply(AutoBean<SnipBean> reply, AutoBean<SnipBean> parent) {
		Log.info("Validating reply to parent: " + parent.as().getTitle() + " content: " + reply.as().getContent());
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<SnipBean>> violations = validator.validate(reply.as());
		if (violations.iterator().hasNext()) {
			ConstraintViolation violation = violations.iterator().next();
			Log.info("Bean invalid: " + violation.getPropertyPath() + " " + violation.getMessage());
			return errorMessageMap.get(violation.getPropertyPath().toString());
		} else {
			//reply must have the proper parent set
			if (reply.as().getParentSnip() == null || reply.as().getParentSnip().isEmpty() ||
					!parent.as().getId().equals(reply.as().getParentSnip())) {
				return RDL.getI18n().formErrorProperReply();
			}

			//see if type is valid
			if (!checkReplyType(reply)) {
				return RDL.getI18n().formErrorReplyType();
			}

			//must have at least one emotion
			if (reply.as().getEmotions() == null || reply.as().getEmotions().isEmpty()) {
				Log.info("Must select at least one emotion");
				return RDL.getI18n().formErrorNoEmotion();
			}
		}
		return null;
	}

	private static boolean checkReplyType(AutoBean<SnipBean> reply) {
		if (Global.moduleName.equals(RDLConstants.Modules.IDEAS)) {
			if (!reply.as().getSnipType().equals(RDLConstants.SnipType.REFERENCE)) {
				return false;
			}
		} else if (Global.moduleName.equals(RDLConstants.Modules.STORIES)) {
			if (!reply.as().getSnipType().equals(RDLConstants.SnipType.POST)) {
				return false;
			}
		} else if (Global.moduleName.equals(RDLConstants.Modules.IMPROVEMENTS)) {
			if (!(reply.as().getSnipType().equals(RDLConstants.SnipType.COUNTER) ||
					reply.as().getSnipType().equals(RDLConstants.SnipType.PLEDGE))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This provides superficial client side validation (only checks author name)
	 * Full validation is provided on the server side
	 *
	 * @param bean
	 * @param authorName
	 * @return
	 */
	public static String validateCanDelete(AutoBean<SnipBean> bean, String authorName) {
		if (bean.as() != null) {
			if (!bean.as().getAuthor().equals(authorName)) {
				return RDL.getI18n().serverErrorC000();
			}
		}
		return null;
	}
}

package com.therdl.server.validator.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.client.RDL;
import com.therdl.server.api.RepService;
import com.therdl.server.api.SnipsService;
import com.therdl.server.api.UserService;
import com.therdl.server.util.ServerUtils;
import com.therdl.server.validator.SnipValidator;
import com.therdl.server.validator.TokenValidator;
import com.therdl.shared.CoreCategory;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.SnipType;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.RepBean;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.SnipValidationException;
import com.therdl.shared.exceptions.TokenInvalidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Holds snips validation
 */

@Singleton
public class SnipValidatorImpl implements SnipValidator {

	private TokenValidator tokenValidator;
	private RepService repService;
	private SnipsService snipsService;
	private Beanery beanery;

	@Inject
	public SnipValidatorImpl(TokenValidator tokenValidator, RepService repService, SnipsService snipsService) {
		this.tokenValidator = tokenValidator;
		this.repService = repService;
		this.snipsService = snipsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	@Override
	public void validateSnipBean(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException {
		UserBean userBean = tokenValidator.validateTokenViaUsername(snipBean.as().getAuthor(), snipBean.as().getToken());
		boolean isValid = true;
		if (snipBean != null && snipBean.as() != null) {
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<SnipBean>> violations = validator.validate(snipBean.as());
			if (!violations.isEmpty()) {
				isValid = false;
				// see if category is valid
			} else if (!CoreCategory.stringIsCateg(snipBean.as().getCoreCat())){
				isValid = false;
				//see if snip type is valid
			} else if (!SnipType.stringIsType(snipBean.as().getSnipType())){
				isValid = false;
				//see if there is at least one emotion
			} else if (snipBean.as().getEmotions() == null || snipBean.as().getEmotions().isEmpty()){
				isValid = false;
				//finally see if user is RDL supporter trying to modify
			} else if (SnipType.isSpecialAccess(snipBean.as().getSnipType())) {
				if (!ServerUtils.isRdlSupporter(userBean)) {
					isValid = false;
				}
			}
		} else {
			isValid = false;
		}
		if (!isValid) {
			throw new SnipValidationException(RDLConstants.ErrorCodes.GENERIC);
		}
	}

	@Override
	public void validateCanDelete(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException {
		//on top of snip bean validation we also check that the snip has no children
		validateSnipBean(snipBean);

		//we build a search options bean with the ID only
		AutoBean<SnipBean> searchOptions = beanery.snipBean();
		searchOptions.as().setId(snipBean.as().getId());
		//do the search
		List<SnipBean> snipList = snipsService.getReferences(searchOptions.as(), null);
		if (snipList != null && snipList.size() >0){
			throw new SnipValidationException(RDLConstants.ErrorCodes.C007);
		}
	}

	@Override
	public void validateCanSaveRef(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException {
		validateSnipBean(snipBean);
		//must have a parent set and the parent must exist
		boolean parentValid = true;
		SnipBean parent = null;
		if (snipBean.as().getParentSnip() == null || snipBean.as().getParentSnip().equals("")){
			parentValid = false;
		} else {
			parent = snipsService.getSnip(snipBean.as().getParentSnip(), null);
			if (parent == null) parentValid = false;
		}
		if (!parentValid) throw new SnipValidationException(RDLConstants.ErrorCodes.C008);
		//unless is post
		if (!snipBean.as().getSnipType().equals(RDLConstants.SnipType.POST)){
			//must not have already given a ref for the same parent (search for snips with parent ID and author)
			AutoBean<SnipBean> query = beanery.snipBean();
			query.as().setParentSnip(parent.getId());
			query.as().setAuthor(snipBean.as().getAuthor());
			query.as().setPageIndex(0);
			query.as().setSortField("author");
			query.as().setSortOrder(1);
			List<SnipBean> resList= snipsService.searchSnipsWith(query.as(), null);
			if (resList != null && !resList.isEmpty()){
				throw new SnipValidationException(RDLConstants.ErrorCodes.C010);
			}
		}
	}

	@Override
	public void validateCanGiveRep(AutoBean<SnipBean> snipBean) throws SnipValidationException, TokenInvalidException {
		//has not given rep before to the same snip
		UserBean userBean = tokenValidator.validateTokenViaUsername(snipBean.as().getAuthor(), snipBean.as().getToken());
		RepBean repBean = repService.getRep(snipBean.as().getId(), userBean.getEmail());
		if (repBean != null){
			throw new SnipValidationException(RDLConstants.ErrorCodes.C009);
		}
	}
}

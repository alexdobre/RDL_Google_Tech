package com.therdl.server.apiimpl;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.RepService;
import com.therdl.server.data.DbProvider;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.RepBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Reputation operations
 */
@Singleton
public class RepServiceImpl implements RepService {
	final Logger log = LoggerFactory.getLogger(RepServiceImpl.class);

	private DbProvider dbProvider;
	private Beanery beanery;


	@Inject
	public RepServiceImpl(DbProvider dbProvider) {
		this.dbProvider = dbProvider;
		beanery = AutoBeanFactorySource.create(Beanery.class);
	}

	@Override
	public RepBean getRep(String snipId, String userId) {
		//TODO implement RepServiceImpl.getRep()
		return null;
	}

	@Override
	public RepBean addRep(String snipId, String userId){
		//TODO implement RepServiceImpl.addRep()
		return null;
	}
}

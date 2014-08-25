package com.therdl.server.apiimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.therdl.server.api.UserService;
import com.therdl.server.data.DbProvider;
import com.therdl.server.util.EmailSender;
import com.therdl.server.util.ServerUtils;
import com.therdl.server.validator.TokenValidator;
import com.therdl.shared.RDLConstants;
import com.therdl.shared.RDLUtils;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import com.therdl.shared.beans.UserBean;
import com.therdl.shared.exceptions.RDLSendEmailException;
import com.therdl.shared.exceptions.TokenInvalidException;

/**
 * A User Service implementation with basic crud methods for managing
 * user authorisation
 *
 * @ String defaultDatabaseName, mongo database, in this case is 'rdl'
 * @ Beanery beanery, see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 */
@Singleton
public class UserServiceImpl implements UserService {
	private static Logger log = Logger.getLogger(UserServiceImpl.class.getName());

	private DbProvider dbProvider;
	private Beanery beanery;
	private TokenValidator tokenValidator;

	@Inject
	public UserServiceImpl(DbProvider dbProvider, TokenValidator tokenValidator) {
		this.dbProvider = dbProvider;
		this.tokenValidator = tokenValidator;
	}

	/**
	 * crud get
	 * returns a User ===  jpa find
	 *
	 * @param bean AutoBean User Details Bean  to find
	 * @param pass String User password string
	 * @return
	 */
	@Override
	public AutoBean<AuthUserBean> authUser(AuthUserBean bean, String pass) {
		log.info("Find user Begin: " + bean.getEmail());
		beanery = AutoBeanFactorySource.create(Beanery.class);
		AutoBean<AuthUserBean> checkedUserBean = beanery.authBean();
		UserBean ub = getUserByEmail(bean.getEmail());

		if (ub != null) {
			if (BCrypt.checkpw(pass, ub.getPassHash())) {
				ub.setToken(tokenValidator.createToken());
				if (bean.getRememberMe()){
					ub.setSid(ServerUtils.generateUUID());
				}else {
					ub.setSid(null);
				}
				updateUser(ub);
				return transformUserBeanInAuthUserBean(checkedUserBean, ub);
			}  // end hash if
		}  // end email if

		checkedUserBean.as().setAction("NotOkUser");
		log.info("Find user END NOK: " + bean.getEmail());
		return checkedUserBean;
	}

	private AutoBean<AuthUserBean> transformUserBeanInAuthUserBean(AutoBean<AuthUserBean> checkedUserBean, UserBean ub) {
		checkedUserBean.as().setName(ub.getUsername());
		checkedUserBean.as().setEmail(ub.getEmail());
		checkedUserBean.as().setSid(ub.getSid());
		checkedUserBean.as().setPaypalId(ub.getPaypalId());
		checkedUserBean.as().setAction("OkUser");
		checkedUserBean.as().setTitles(ub.getTitles());
		checkedUserBean.as().setToken(ub.getToken());
		checkedUserBean.as().setIsRDLSupporter(ServerUtils.isRdlSupporter(ub));
		log.info("User is RDL supporter?: "+checkedUserBean.as().getIsRDLSupporter());

		log.info("Find user END OK: " + checkedUserBean.as().getEmail());
		return checkedUserBean;
	}

	@Override
	public UserBean getUserById(String id) {
		log.info("UserServiceImpl getUser  id: " + id);

		beanery = AutoBeanFactorySource.create(Beanery.class);
		DB db = getMongo();
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		DBCollection coll = db.getCollection("rdlUserData");
		DBCursor cursor = coll.find(query);
		DBObject doc = cursor.next();

		UserBean user = buildBeanObject(doc);

		return user;
	}

	@Override
	public UserBean getUserByUsername(String username) {
		log.info("UserServiceImpl getUserByUsername BEGIN email: " + username);

		beanery = AutoBeanFactorySource.create(Beanery.class);
		DB db = getMongo();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		DBCollection coll = db.getCollection("rdlUserData");
		DBCursor cursor = coll.find(query);
		if (cursor.hasNext()) {
			DBObject doc = cursor.next();
			UserBean user = buildBeanObject(doc);
			log.info("UserServiceImpl getUserByUsername END FOUND: " + username);
			return user;
		}

		log.info("UserServiceImpl getUserByUsername END NOT FOUND: " + username);
		return null;
	}

	/**
	 * gets user by email
	 *
	 * @param email
	 * @return
	 */
	@Override
	public UserBean getUserByEmail(String email) {
		log.info("UserServiceImpl getUserByEmail BEGIN email: " + email);

		beanery = AutoBeanFactorySource.create(Beanery.class);
		DB db = getMongo();
		BasicDBObject query = new BasicDBObject();
		query.put("email", email);
		DBCollection coll = db.getCollection("rdlUserData");
		DBCursor cursor = coll.find(query);
		if (cursor.hasNext()) {
			DBObject doc = cursor.next();
			UserBean user = buildBeanObject(doc);
			log.info("UserServiceImpl getUserByEmail END FOUND: " + email);
			return user;
		}

		log.info("UserServiceImpl getUserByEmail END NOT FOUND: " + email);
		return null;
	}

	@Override
	public UserBean getUserByPayPalId(String paypalId) {
		log.info("UserServiceImpl getUserByPayPalId BEGIN paypalId: " + paypalId);

		beanery = AutoBeanFactorySource.create(Beanery.class);
		DB db = getMongo();
		BasicDBObject query = new BasicDBObject();
		query.put("paypalId", paypalId);
		DBCollection coll = db.getCollection("rdlUserData");
		DBCursor cursor = coll.find(query);
		if (cursor.hasNext()) {
			DBObject doc = cursor.next();
			UserBean user = buildBeanObject(doc);
			log.info("UserServiceImpl getUserByPayPalId END FOUND: " + paypalId);
			return user;
		}

		log.info("UserServiceImpl getUserByPayPalId END NOT FOUND: " + paypalId);
		return null;
	}

	@Override
	public AutoBean<AuthUserBean> findUserBySid(String sid) {
		log.info("UserServiceImpl findUserBySid BEGIN sid: " + sid);

		beanery = AutoBeanFactorySource.create(Beanery.class);
		DB db = getMongo();
		BasicDBObject query = new BasicDBObject();
		query.put("sid", sid);
		DBCollection coll = db.getCollection("rdlUserData");
		DBCursor cursor = coll.find(query);

		AutoBean<AuthUserBean> checkedUserBean = beanery.authBean();

		if (cursor.hasNext()) {
			DBObject doc = cursor.next();
			UserBean user = buildBeanObject(doc);
			log.info("UserServiceImpl findUserBySid END FOUND: " + sid);
			user.setToken(tokenValidator.createToken());
			updateUser(user);
			return transformUserBeanInAuthUserBean(checkedUserBean, user);
		}

		log.info("UserServiceImpl getUserByEmail END NOT FOUND: " + sid);
		checkedUserBean.as().setAction("NotOkUser");
		return checkedUserBean;
	}

	// poor code design, this should return a UserObject, programmer was told to do this
	// quickest to deal with this is session servlet for now, however this solution should
	// be upgraded asap

	/**
	 * crud save === jpa persist
	 *
	 * @param user UserBean, server side java  bean
	 */
	@Override
	public void createUser(UserBean user) {
		log.info("UserServiceImpl createUser  email : " + user.getEmail());
		DB db = getMongo();

		DBCollection coll = db.getCollection("rdlUserData");
		if (user.getId() == null) {
			BasicDBObject doc = buildDbObject(user);
			coll.insert(doc);
		}
	}

	@Override
	public void changePass(UserBean userBean, String pass) {
		userBean.setPassHash(ServerUtils.encryptString(pass));
		updateUser(userBean);
	}

	/**
	 * updates the object in the db
	 *
	 * @param user : Bean object to update with
	 * @return
	 */
	@Override
	public void updateUser(UserBean user) {

		log.info("UserServiceImpl updateUser  updateUser id: " + user.getId());
		System.out.println("UserServiceImpl updateUser id :  " + user.getId());

		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlUserData");

		// build the search query
		BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(user.getId()));

		// set thee id to null to avoid exception from the driver (the id could not be updated)
		user.setId(null);

		// get the db object to save
		BasicDBObject doc = buildDbObject(user);

		// build the update query
		BasicDBObject updateDocument = new BasicDBObject();
		updateDocument.append("$set", doc);

		// make update
		coll.findAndModify(searchQuery, updateDocument);
	}

	@Override
	public void updateSid(AuthUserBean bean) {
		log.info("UserServiceImpl updateSid BEGIN email: " + bean.getEmail());
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlUserData");

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("sid", bean.getSid()));
		BasicDBObject searchQuery = new BasicDBObject().append("email", bean.getEmail());
		coll.update(searchQuery, newDocument);

		log.info("UserServiceImpl updateSid END email: " + bean.getEmail());
	}

	/**
	 * user json contains a list of reputation given objects, which stores the snip ids and date that user gave a reputation
	 * the function adds a reputation given object to the list. Finds user by email.
	 *
	 * @param repGivenBean repGivenBean
	 * @param userEmail    email
	 * @return modified UserBean
	 */
	public UserBean addRepGiven(AutoBean<UserBean.RepGivenBean> repGivenBean, String userEmail) {
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlUserData");

		BasicDBObject searchQuery = new BasicDBObject().append("email", userEmail);
		DBObject listItem = new BasicDBObject("repGiven", new BasicDBObject("snipId", repGivenBean.as().getSnipId()).append("date", repGivenBean.as().getDate()));
		DBObject updateQuery = new BasicDBObject("$push", listItem);
		DBObject dbObj = coll.findAndModify(searchQuery, updateQuery);
		UserBean user = buildBeanObject(dbObj);
		return user;
	}

	/**
	 * checks if user gave a reputation to the snip with the given snipId
	 *
	 * @param email  user email
	 * @param snipId snipId
	 * @return Integer 1 or 0
	 */
	public Integer isRepGivenForSnip(String email, String snipId) {
		UserBean userBean = getUserByEmail(email);

		if (userBean.getRepGiven() != null) {
			for (UserBean.RepGivenBean repGiven : userBean.getRepGiven()) {
				if (repGiven.getSnipId().equals(snipId))
					return 1;
			}
		}

		return 0;
	}

	/**
	 * sets isRepGivenByUser flag for input snip beans
	 *
	 * @param email     current user
	 * @param snipBeans snip beans as list
	 */
	public void setRepGivenForSnips(String email, List<SnipBean> snipBeans) {
		UserBean userBean = getUserByEmail(email);
		if (userBean.getRepGiven() != null) {
			for (SnipBean snipBean : snipBeans) {
				snipBean.setIsRepGivenByUser(0);
				for (UserBean.RepGivenBean repGiven : userBean.getRepGiven()) {
					if (repGiven.getSnipId().equals(snipBean.getId())) {
						snipBean.setIsRepGivenByUser(1);
						break;
					}
				}
			}
		}

	}

	/**
	 * user json contains a list of reference given objects, which stores the snip ids and date that user wrote a reference
	 * the function adds a reference given object to the list. Finds user by email.
	 *
	 * @param refGivenBean refGivenBean
	 * @param userEmail    email
	 * @return modified UserBean
	 */
	public UserBean addRefGiven(AutoBean<UserBean.RefGivenBean> refGivenBean, String userEmail) {
		DB db = getMongo();
		DBCollection coll = db.getCollection("rdlUserData");

		BasicDBObject searchQuery = new BasicDBObject().append("email", userEmail);
		DBObject listItem = new BasicDBObject("refGiven", new BasicDBObject("snipId", refGivenBean.as().getSnipId()).append("date", refGivenBean.as().getDate()));
		DBObject updateQuery = new BasicDBObject("$push", listItem);
		DBObject dbObj = coll.findAndModify(searchQuery, updateQuery);
		UserBean user = buildBeanObject(dbObj);
		return user;
	}

	/**
	 * checks if user wrote a reference to the snip with the given snipId
	 *
	 * @param email  user email
	 * @param snipId snipId
	 * @return Integer 1 or 0
	 */
	public Integer isRefGivenForSnip(String email, String snipId) {
		UserBean userBean = getUserByEmail(email);

		if (userBean.getRefGiven() != null) {
			for (UserBean.RefGivenBean refGiven : userBean.getRefGiven()) {
				if (refGiven.getSnipId().equals(snipId))
					return 1;
			}
		}

		return 0;
	}


	@Override
	public void recoverPassword(String email, String token) throws TokenInvalidException {
		tokenValidator.validateTokenViaEmail(email, token);
		String newPass = ServerUtils.generatePassword();
		String newPassHash = ServerUtils.encryptString(newPass);

		//get the user
		UserBean userBean = getUserByEmail(email);
		//reset the pass
		userBean.setPassHash(newPassHash);
		//update the user
		updateUser(userBean);

		//send the e-mail
		try {
			EmailSender.sendNewPassEmail(newPass, email, getMongo());
		} catch (RDLSendEmailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * builds the UserBean from he db object
	 *
	 * @param : DBObject doc
	 * @return : UserBean
	 */
	private UserBean buildBeanObject(DBObject doc) {
		UserBean user = beanery.userBean().as();

		user.setId(doc.get("_id").toString());
		user.setUsername((String)doc.get("username"));
		user.setPassHash((String)doc.get("passHash"));
		user.setEmail((String)doc.get("email"));
		user.setToken((String)doc.get("token"));
		user.setSid((String)doc.get("sid"));
		user.setPaypalId((String)doc.get("paypalId"));
		user.setRep(RDLUtils.parseInt(doc.get("rep")));
		BasicDBList titles = (BasicDBList)doc.get("titles");
		BasicDBList friends = (BasicDBList)doc.get("friends");
		BasicDBList repGiven = (BasicDBList)doc.get("repGiven");
		BasicDBList refGiven = (BasicDBList)doc.get("refGiven");
		BasicDBList votesGiven = (BasicDBList)doc.get("votesGiven");

		List<UserBean.TitleBean> titleList = new ArrayList<UserBean.TitleBean>();
		for (Object obj : titles) {
			UserBean.TitleBean titlesBean = beanery.userTitleBean().as();
			titlesBean.setTitleName((String)((BasicDBObject)obj).get("titleName"));
			titlesBean.setDateGained((String)((BasicDBObject)obj).get("dateGained"));
			titlesBean.setExpires((String)((BasicDBObject)obj).get("expires"));
			titleList.add(titlesBean);
		}
		user.setTitles(titleList);

		List<UserBean.FriendBean> friendList = new ArrayList<UserBean.FriendBean>();

		for (Object obj : friends) {
			UserBean.FriendBean friendsBean = beanery.userFriendBean().as();

			// set the messages
			BasicDBList messages = (BasicDBList)((BasicDBObject)obj).get("messages");

			List<UserBean.MessageBean> messageList = new ArrayList<UserBean.MessageBean>();

			for (Object _obj : messages) {
				UserBean.MessageBean messageBean = beanery.userMessageBean().as();
				messageBean.setMessageId((String)((BasicDBObject)_obj).get("messageId"));
				messageBean.setDate((String)((BasicDBObject)_obj).get("date"));
				messageList.add(messageBean);
			}

			friendsBean.setMessages(messageList);
			friendsBean.setUsername((String)((BasicDBObject)obj).get("username"));
			friendList.add(friendsBean);
		}
		user.setFriends(friendList);

		List<UserBean.RepGivenBean> repGivenList = new ArrayList<UserBean.RepGivenBean>();

		for (Object obj : repGiven) {
			UserBean.RepGivenBean repGivenBean = beanery.userRepGivenBean().as();
			repGivenBean.setSnipId((String)((BasicDBObject)obj).get("snipId"));
			repGivenBean.setDate((String)((BasicDBObject)obj).get("date"));
			repGivenList.add(repGivenBean);
		}
		user.setRepGiven(repGivenList);

		List<UserBean.RefGivenBean> refGivenList = new ArrayList<UserBean.RefGivenBean>();

		if (refGiven != null) {
			for (Object obj : refGiven) {
				UserBean.RefGivenBean refGivenBean = beanery.userRefGivenBean().as();
				refGivenBean.setSnipId((String)((BasicDBObject)obj).get("snipId"));
				refGivenBean.setDate((String)((BasicDBObject)obj).get("date"));
				refGivenList.add(refGivenBean);
			}
		}
		user.setRefGiven(refGivenList);

		List<UserBean.VotesGivenBean> votesGivenList = new ArrayList<UserBean.VotesGivenBean>();

		for (Object obj : votesGiven) {
			UserBean.VotesGivenBean votesGivenBean = beanery.userVotesGivenBean().as();
			votesGivenBean.setProposalId((String)((BasicDBObject)obj).get("proposalId"));
			votesGivenBean.setDate((String)((BasicDBObject)obj).get("date"));
			votesGivenList.add(votesGivenBean);
		}
		user.setVotesGiven(votesGivenList);

		return user;
	}

	/**
	 * builds the db object from Bean
	 *
	 * @param : UserBean user
	 * @return : BasicDBObject
	 */
	private BasicDBObject buildDbObject(UserBean user) {
		BasicDBObject doc = new BasicDBObject();

		// if there is id in user instance , append it
		if (user.getId() != null) {
			doc.append("_id", user.getId());
		}

		doc.append("username", user.getUsername());
		doc.append("passHash", user.getPassHash());
		doc.append("email", user.getEmail());
		doc.append("sid", user.getSid());
		doc.append("paypalId", user.getPaypalId());
		doc.append("rep", user.getRep());
		doc.append("token", user.getToken());

		BasicDBList titlesList = new BasicDBList();

		if (user.getTitles() != null) {

			for (UserBean.TitleBean title : user.getTitles()) {
				BasicDBObject obj = new BasicDBObject("titleName", title.getTitleName()).
						append("dateGained", title.getDateGained()).
						append("expires", title.getExpires());
				titlesList.add(obj);
			}

		}

		BasicDBList friendsList = new BasicDBList();

		if (user.getFriends() != null) {

			for (UserBean.FriendBean friends : user.getFriends()) {

				// get the messages
				BasicDBList messageList = new BasicDBList();
				for (UserBean.MessageBean message : friends.getMessages()) {
					BasicDBObject obj = new BasicDBObject("messageId", message.getMessageId()).
							append("date", message.getDate());
					messageList.add(obj);
				}

				BasicDBObject obj = new BasicDBObject("username", friends.getUsername()).
						append("messages", messageList);
				friendsList.add(obj);
			}

		}

		BasicDBList repGivenList = new BasicDBList();

		if (user.getRepGiven() != null) {

			for (UserBean.RepGivenBean repGiven : user.getRepGiven()) {
				BasicDBObject obj = new BasicDBObject("snipId", repGiven.getSnipId()).
						append("date", repGiven.getDate());
				repGivenList.add(obj);
			}

		}

		BasicDBList refGivenList = new BasicDBList();

		if (user.getRefGiven() != null) {

			for (UserBean.RefGivenBean refGiven : user.getRefGiven()) {
				BasicDBObject obj = new BasicDBObject("snipId", refGiven.getSnipId()).
						append("date", refGiven.getDate());
				refGivenList.add(obj);
			}
		}

		BasicDBList votesGivenList = new BasicDBList();

		if (user.getVotesGiven() != null) {

			for (UserBean.VotesGivenBean votesGiven : user.getVotesGiven()) {
				BasicDBObject obj = new BasicDBObject("proposalId", votesGiven.getProposalId()).
						append("date", votesGiven.getDate());
				votesGivenList.add(obj);
			}
		}

		doc.append("titles", titlesList);
		doc.append("friends", friendsList);
		doc.append("repGiven", repGivenList);
		doc.append("refGiven", refGivenList);
		doc.append("votesGiven", votesGivenList);
		return doc;
	}

	private DB getMongo() {
		return dbProvider.getDb();
	}
}

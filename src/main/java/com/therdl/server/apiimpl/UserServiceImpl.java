package com.therdl.server.apiimpl;

import com.google.common.collect.Iterables;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.mongodb.*;
import com.therdl.server.api.UserService;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.UserBean;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Singleton
public class UserServiceImpl implements UserService {

    private String defaultDatabaseName;
    private Beanery beanery;

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * for testing
     * drops the User collection
     */
    @Override
    public void dropUserCollection() {
        DB db = getMongo();
        db.getCollection("rdlUserData").drop();
    }

    @Override
    public List<UserBean> getAllUsers() {
        DB db = getMongo();
        List<UserBean> beans = new ArrayList<UserBean>();

        Set<String> colls = db.getCollectionNames();
        for (String s : colls) {
            sLogger.info(s);
        }
        DBCollection coll = db.getCollection("rdlUserData");

        DBCursor collDocs  =  coll.find();

        while(collDocs.hasNext()) {
            DBObject doc = collDocs.next();
            UserBean user = buildBeanObject(doc);
            beans.add(user);
        }
        return beans;
    }

    @Override
    public  AutoBean<AuthUserBean>  findUser(AuthUserBean bean, String hash) {
        beanery = AutoBeanFactorySource.create(Beanery.class);
        AutoBean<AuthUserBean> checkedUserBean = beanery.authBean();
        List<UserBean> users =getAllUsers();

         for(UserBean ub : users)   {

             if  (ub.getEmail().equals(bean.getEmail())) {
                 if (BCrypt.checkpw(hash, ub.getPassHash())) {
                     checkedUserBean.as().setName(ub.getUsername());
                     checkedUserBean.as().setEmail(ub.getEmail());
                     checkedUserBean.as().setAction("OkUser");
                     // always check for null
                     if(ub.getAvatarUrl() != null) checkedUserBean.as().setAvatarUrl(ub.getAvatarUrl());
                     return checkedUserBean;
                 }  // end hash if
                 }  // end email if

         } // end loop
        checkedUserBean.as().setAction("NotOkUser");
        return  checkedUserBean;

    }




    @Override
    public UserBean getLastUser(String  match) {
        List<UserBean> beans = getAllUsers();

        UserBean lastBean = Iterables.getLast(beans);

        return lastBean;
    }

    @Override
    public UserBean getUser(String id) {
        sLogger.info("UserServiceImpl getUser  id: "+id);

        beanery = AutoBeanFactorySource.create(Beanery.class);
        DB db = getMongo();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCollection coll = db.getCollection("rdlUserData");
        DBCursor cursor = coll.find(query);
        DBObject  doc=  cursor.next();

        UserBean user = buildBeanObject(doc);

        return user;
    }


    // poor code design, this should return a UserObject
    @Override
    public void createUser(UserBean user) {
        sLogger.info("UserServiceImpl createUser  email : " + user.getEmail());
        DB db = getMongo();

        DBCollection coll = db.getCollection("rdlUserData");
        if(user.getId()==null)  {
            BasicDBObject doc = buildDbObject(user);
            coll.insert(doc);
        }
    }

    /**
     * updates the object in the db
     * @param user : Bean object to update with
     * @return
     */
    @Override
    public void updateUser(UserBean user) {

        sLogger.info("UserServiceImpl updateUser  updateUser id: " + user.getId());
        System.out.println("UserServiceImpl updateUser id :  " +user.getId());

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
    public void deleteUser(String id) {
        DB db = getMongo();
        DBCollection coll = db.getCollection("rdlUserData");
        BasicDBObject deleteDocument = new BasicDBObject();
        deleteDocument.append("_id" ,new ObjectId(id));
        DBCursor cursor = coll.find(deleteDocument);
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            coll.remove(item);
        }
    }

    @Override
    public String getDebugString() {
        return "User Service wired up for guice injection ok";
    }

    /**
     * builds the UserBean from he db object
     * @param : DBObject doc
     * @return : UserBean
     */
    private UserBean buildBeanObject(DBObject doc) {
        UserBean user = beanery.userBean().as();

        user.setId(doc.get("_id").toString());
        // avatar code
        user.setAvatarUrl((String) doc.get("avatarUrl"));
        user.setUsername((String) doc.get("username"));
        user.setPassHash((String) doc.get("passHash"));
        user.setEmail((String) doc.get("email"));
        user.setRep((String) doc.get("rep"));
        BasicDBList titles = (BasicDBList)doc.get("titles");
        BasicDBList friends = (BasicDBList)doc.get("friends");
        BasicDBList repGiven = (BasicDBList)doc.get("repGiven");
        BasicDBList votesGiven = (BasicDBList)doc.get("votesGiven");

        List<UserBean.TitleBean> titleList = new ArrayList<UserBean.TitleBean>();
        for(Object obj : titles){
            UserBean.TitleBean titlesBean = beanery.userTitleBean().as();
            titlesBean.setTitleName((String)((BasicDBObject)obj).get("titleName"));
            titlesBean.setDateGained((String)((BasicDBObject)obj).get("dateGained"));
            titleList.add(titlesBean);
        }
        user.setTitles(titleList);


        List<UserBean.FriendBean> friendList = new ArrayList<UserBean.FriendBean>();

        for(Object obj : friends){
            UserBean.FriendBean friendsBean = beanery.userFriendBean().as();

            // set the messages
            BasicDBList messages = (BasicDBList) ((BasicDBObject) obj).get("messages");

            List<UserBean.MessageBean> messageList = new ArrayList<UserBean.MessageBean>();

            for(Object _obj : messages){
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

        for(Object obj : repGiven){
            UserBean.RepGivenBean repGivenBean = beanery.userRepGivenBean().as();
            repGivenBean.setSnipId((String)((BasicDBObject)obj).get("snipId"));
            repGivenBean.setDate((String)((BasicDBObject)obj).get("date"));
            repGivenList.add(repGivenBean);
        }
        user.setRepGiven(repGivenList);


        List<UserBean.VotesGivenBean> votesGivenList = new ArrayList<UserBean.VotesGivenBean>();

        for(Object obj : votesGiven){
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
     * @param : UserBean user
     * @return : BasicDBObject
     */
    private BasicDBObject buildDbObject(UserBean user) {
        BasicDBObject doc = new BasicDBObject();

        // if there is id in user instance , append it
        if(user.getId() != null){
            doc.append("_id", user.getId());
        }

        // avatar code
        String avatarUrl = "userAvatar"+File.separator+ user.getUsername()+"small.jpg";
        doc.append("avatarUrl",avatarUrl);
        doc.append("username", user.getUsername());
        doc.append("passHash", user.getPassHash());
        doc.append("email", user.getEmail());
        doc.append("rep", user.getRep());

        BasicDBList titlesList = new BasicDBList();

        if(user.getTitles() != null) {

        for (UserBean.TitleBean title : user.getTitles()) {
            BasicDBObject obj = new BasicDBObject("titleName", title.getTitleName()).
                    append("dateGained", title.getDateGained());
            titlesList.add(obj);
        }

        }


        BasicDBList friendsList = new BasicDBList();

        if(user.getFriends() != null) {

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

        if(user.getRepGiven() != null) {

            for (UserBean.RepGivenBean repGiven : user.getRepGiven()) {
            BasicDBObject obj = new BasicDBObject("snipId", repGiven.getSnipId()).
                    append("date", repGiven.getDate());
            repGivenList.add(obj);
        }

        }

        BasicDBList votesGivenList = new BasicDBList();

        if(user.getVotesGiven() != null) {

        for (UserBean.VotesGivenBean votesGiven : user.getVotesGiven()) {
            BasicDBObject obj = new BasicDBObject("proposalId", votesGiven.getProposalId()).
                    append("date", votesGiven.getDate());
            votesGivenList.add(obj);
        }
        }

        doc.append("titles", titlesList);
        doc.append("friends", friendsList);
        doc.append("repGiven", repGivenList);
        doc.append("votesGiven", votesGivenList);
        return doc;
    }



    // later the url will be a cloud based schema hence exception
    private DB getMongo() {

        defaultDatabaseName = "rdl";

        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB(defaultDatabaseName);
            return db;

        } catch (UnknownHostException e) {
            sLogger.error(e.getMessage());
            return null;
        }
    }
}

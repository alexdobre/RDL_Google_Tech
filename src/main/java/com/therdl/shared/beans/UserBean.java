package com.therdl.shared.beans;

import java.util.List;


/**
 * A UserBean, in the
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean
 * for new developers important to understand GWT AutoBean client/server architecture
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanCodex
 * see http://code.google.com/p/google-web-toolkit/wiki/AutoBean#AutoBeanFactory
 * <p/>
 * the AutoBean design pattern allows us to use the same beans in the java layer on
 * the server and the javascript layer in the browser(recall GWT java code runs as javascript)
 * UserBean is used on the client as a java class for the Mongo persistence layer
 */
public interface UserBean {

    /**
     * used for implementing the command pattern in this application
     * for actions see
     * http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html
     *
     * @return
     */
    String getAction();

    void setAction(String action);

    /**************************** Getters and Setters ******************************/


    /**
     * UserBean needs the avatar url to pass to currentuserbean
     */
    String getAvatarUrl();

    /**
     * UserBean needs the avatar url to pass to currentuserbean
     *
     * @ String avatarUrl  the uri to locate the users image,
     * used in the browser/javascript layer
     */
    void setAvatarUrl(String avatarUrl);

    /**
     * the unique id, primary key
     */
    String getId();

    void setId(String id);

    /**
     * methods below are for standard form based credentials submitted on the clien
     * for user login and sign up
     *
     * @return
     */
    String getUsername();

    void setUsername(String username);

    String getPassHash();

    void setPassHash(String passHash);

    String getEmail();

    void setEmail(String email);

    /**
     * methods below are composition for the Snip rdl schema
     */

    String getRep();

    void setRep(String rep);

    List<TitleBean> getTitles();

    void setTitles(List<TitleBean> titles);

    List<FriendBean> getFriends();

    void setFriends(List<FriendBean> friends);

    List<RepGivenBean> getRepGiven();

    void setRepGiven(List<RepGivenBean> repGiven);

    List<RefGivenBean> getRefGiven();

    void setRefGiven(List<RefGivenBean> refGiven);

    List<VotesGivenBean> getVotesGiven();

    void setVotesGiven(List<VotesGivenBean> votesGiven);

    /**
     * nested interfaces
     */
    interface TitleBean {
        public String getTitleName();

        public void setTitleName(String titleName);

        public String getDateGained();

        public void setDateGained(String dateGained);
    }

    interface FriendBean {
        public String getUsername();

        public void setUsername(String username);

        public List<MessageBean> getMessages();

        public void setMessages(List<MessageBean> messages);
    }

    interface RepGivenBean {
        public String getSnipId();

        public void setSnipId(String snipId);

        public String getDate();

        public void setDate(String date);
    }

    interface RefGivenBean {
        public String getSnipId();

        public void setSnipId(String snipId);

        public String getDate();

        public void setDate(String date);
    }

    interface VotesGivenBean {
        public String getProposalId();

        public void setProposalId(String proposalId);

        public String getDate();

        public void setDate(String date);
    }

    interface MessageBean {
        public String getMessageId();

        public void setMessageId(String messageId);

        public String getDate();

        public void setDate(String date);
    }
}

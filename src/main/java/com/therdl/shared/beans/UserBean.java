package com.therdl.shared.beans;

import java.util.List;

public interface UserBean {

    /**
     * this field is not being saved in the db
     * it is mainly the action for the server to handle the request
     * @return
     */
    String getAction();
    void setAction(String action);

    /**************************** Getters and Setters ******************************/

    String getId();
    void setId(String id);

    String getUsername();
    void setUsername(String username);

    String getPassHash();
    void setPassHash(String passHash);

    String getEmail();
    void setEmail(String email);

    String getRep();
    void setRep(String rep);

    List<TitleBean> getTitles();
    void setTitles(List<TitleBean> titles);

    List<FriendBean> getFriends();
    void setFriends(List<FriendBean> friends);

    List<RepGivenBean> getRepGiven();
    void setRepGiven(List<RepGivenBean> repGiven);

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

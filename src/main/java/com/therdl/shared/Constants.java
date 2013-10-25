package com.therdl.shared;

/**
 * All shared constants could be placed here
 *
 * @DEPLOY boolean determine url for deployment mode
 */
public class Constants {

    /**
     * Please change the DEPLOY constant to configure the paths of the
     * projects for jetty and jboss
     * <p/>
     * true  : will optimize the paths for jboss this is the deployed mode
     * false : will optimize the paths for jetty this is the development mode
     */
    public static final boolean DEPLOY = false;
    //  public static final boolean DEPLOY =  true;
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String FACEBOOK_URL = "https://www.facebook.com/therdl";
    public static final String TWITTER_URL = "https://twitter.com/RDLSocial";
    public static final String YOUTUBE_URL = "http://www.youtube.com/user/AlexTheRDL";
    public static final String REDDIT_URL = "http://www.reddit.com/user/RDLSocial";


    public static final String IDEAS_TEXT = "The RDL Snips (Information snippets) is a concept of a small piece of golden concise thought provoking and useful information. Text snips, video snips and audio.";
    public static final String STORIES_TEXT = "You need advice specific for your problem. You need the experience of people who went through what you are going through. You need the support of people who truly understand your situation. Discussion tags and streams are for you. ";
    public static final String VOTES_TEXT = "With this we hope to achieve an unparalleled amount of transparency and feedback. A site that truly is for the users and we work for them and report to them. ";
    public static final String SERVICES_TEXT = "The intent of this section is to promote high quality counselling services. Registered members can vote on the service or counsellor along with references. \n";

    public static final String SNIP_TEXT = "The RDL Snips (Information snippets) is a concept of a small piece of golden concise thought provoking and useful information.";
    public static final String FAST_CAP_TEXT = "Fast caps (fast capsules) are intended to be fast relief quick concise pieces of information based on your specific problem.";
    public static final String MATERIAL_TEXT = "Materials are long format media such as books, long videos or audio.";
    public static final String HABIT_TEXT = "Habits (skills) are the end goal, the result of study in relationships.";
}

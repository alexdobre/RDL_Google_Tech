package com.therdl.client.social.facebook;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class Facebook
{
    private static String APIKey;

    /**
     * Initialize the Facebook API. Call this method before using any facebook
     * feature.
     * 
     * @param APIKey
     *            - The API Key for your facebook app. This is listed for each
     *            of your apps at <a
     *            href="http://www.facebook.com/developers/apps.php"
     *            >http://www.facebook.com/developers/apps.php</a>
     */
    public static void init(String APIKey)
    {
	if (Facebook.APIKey != null)
	    return;
	Facebook.APIKey = APIKey;
	init(APIKey, GWT.getModuleBaseURL() + "xd_receiver.htm");
    }

    /**
     * The API Key set in the init() method.
     */
    public static String getAPIKey()
    {
	return APIKey;
    }

    private static native void init(String APIKey, String xdReceiver) /*-{
        window.fbAsyncInit = function() {
		    FB.init({
		      appId      : '1568442000092974',
		      xfbml      : true,
		      version    : 'v2.2'
		    });
	  };
	  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "//connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
   	}(document, 'script', 'facebook-jssdk'));
    }-*/;

    protected static HandlerManager handlerManager;

    protected static HandlerManager getHandlerManager()
    {
	if (handlerManager == null)
	    handlerManager = new HandlerManager(Facebook.class);
	return handlerManager;
    }

    // ---- CONNECTION STATE ----

    /**
     * Returns true if the user is currently logged in and has granted access to
     * the application.
     */
    public static boolean isLoggedIn()
    {
//    	return getConnectionStatus() == ConnectState.CONNECTED;
    	return false;
    }

    /**
     * A Facebook login handler is fired whenever the Facebook connection status
     * changes. For example when a user logs in or logs out.
     */
//    public static HandlerRegistration addLoginHandler(FacebookLoginHandler handler)
//    {
//	HandlerRegistration hr = getHandlerManager().addHandler(FacebookLoginEvent.getType(), handler);
//	startConnectionListeners();
//	return hr;
//    }

    /**
     * Possible states for the Facebook connection
     */
    public static enum ConnectState
    {
	/**
	 * A user is logged in.
	 */
	CONNECTED,
	/**
	 * No user is logged in.
	 */
	USER_NOT_LOGGED_IN,
	/**
	 * A user is logged in but has not authorized the application.
	 */
	APP_NOT_AUTHORIZED,
	/**
	 * The current status is unknown. The Facebook API is still trying to
	 * determine the status of the connection.
	 */
	PENDING;
    }

    /**
     * Get the current status of the Facebook connection. See the
     * ConnectionState enum for possible values.
     */
//    public static native ConnectState getConnectionStatus() /*-{
//        if ($wnd.FB.Connect && $wnd.FB.Connect.get_status().get_isReady()) {
//        	var status = $wnd.FB.Connect.get_status().result;
//        	if (status == $wnd.FB.ConnectState.connected) {
//        		return @com.reveregroup.gwt.facebook4gwt.Facebook.ConnectState::CONNECTED;
//        	} else if (status == $wnd.FB.ConnectState.userNotLoggedIn) {
//        		return @com.reveregroup.gwt.facebook4gwt.Facebook.ConnectState::USER_NOT_LOGGED_IN;
//        	} else if (status == $wnd.FB.ConnectState.appNotAuthorized) {
//        		return @com.reveregroup.gwt.facebook4gwt.Facebook.ConnectState::APP_NOT_AUTHORIZED;
//        	} else {
//        		return @com.reveregroup.gwt.facebook4gwt.Facebook.ConnectState::PENDING;
//        	}
//        } else {
//        	return @com.reveregroup.gwt.facebook4gwt.Facebook.ConnectState::PENDING;
//        }
//    }-*/;

    private static native void startConnectionListeners() /*-{
        if (!$wnd.FB.GWT_connectionStatusChanged) {
        	$wnd.FB.GWT_connectionStatusChanged = function() {
        		@com.reveregroup.gwt.facebook4gwt.Facebook::connectionStatusChanged()();
        	};
        	$wnd.FB_RequireFeatures(["Connect"], function() {
        		$wnd.FB.Connect.get_status().add_changed($wnd.FB.GWT_connectionStatusChanged);
        		$wnd.FB.GWT_connectionStatusChanged();
        	});
        	$wnd.FB.GWT_connectionStatusChanged();
        }
    }-*/;

    @SuppressWarnings("unused")
    private static void connectionStatusChanged()
    {
//	FacebookLoginEvent event = new FacebookLoginEvent(getConnectionStatus());
//	getHandlerManager().fireEvent(event);
    }

    /**
     * If no facebook user is logged in, present the login dialog.
     * 
     * @param callback
     *            - onSuccess is called when the users successfully logs in. If
     *            the user cancels the login dialog, no callback is made. The
     *            result value is true if the login is successful and false if
     *            it is not.
     */
    public static native void login(AsyncCallback<Boolean> callback) /*-{
        $wnd.FB_RequireFeatures(["Connect"], function() {
        	$wnd.FB.Connect.requireSession(function() {
//        		@com.reveregroup.gwt.facebook4gwt.Facebook::loginCallback(Lcom/google/gwt/user/client/rpc/AsyncCallback;)(callback);
        	});
        });
    }-*/;

    @SuppressWarnings("unused")
    private static void loginCallback(AsyncCallback<Boolean> callback)
    {
	if (callback != null)
	    callback.onSuccess(isLoggedIn());
    }

    // ---- Other ----
    /**
     * Get the id of the Facebook user that is currently logged in. Return null
     * if no user is logged in and authorized.
     */
    public static native String getLoggedInUserId() /*-{
        return $wnd.FB.Connect.get_loggedInUser();
    }-*/;
    
    
    
    
    public static native void shareContent(String url) /*-{ 
	 
	    FB.api(
	    "/?id=http%3A%2F%2Fwww.imdb.com%2Ftitle%2Ftt2015381%2F",
	    function (response) {
	      if (response && !response.error) {
	       
	      }
	    }
	    );
 	}-*/; 
    
    public static native void shareUrl() /*-{ 
	   FB.ui({
		  method: 'share',
		  href: document.URL,
		}, function(response){

		});
	}-*/; 

//    private static APIClient apiClient;

    /**
     * Use the APIClient to interact with Facebook through the standard APIs.
     */
   // public static APIClient APIClient()
    //{
//	if (apiClient == null)
//	    apiClient = new APIClient();
//	return apiClient;
   
//    }

    // ---- Debug Helpers ----

    public static String toStr(Object o)
    {
	if (o == null)
	    return "null";
	else
	    return o.toString();
    }

    public native static void log(Object o) /*-{
        if ($wnd.console)
        	$wnd.console.log(o);
        else {
        	if (0 == null)
        		$wnd.alert(null);
        	else
        		$wnd.alert(o.toString());
        }
    }-*/;

    public native static void debug() /*-{
        debugger;
    }-*/;

	
}

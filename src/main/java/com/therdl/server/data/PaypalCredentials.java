package com.therdl.server.data;

/**
 * Bean to store the PaypalCredentials retrieved from the database
 *
 */
public class PaypalCredentials {

    private String type;
    private String url;
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

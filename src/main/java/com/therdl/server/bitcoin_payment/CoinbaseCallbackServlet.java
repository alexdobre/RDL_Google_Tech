package com.therdl.server.bitcoin_payment;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


@Singleton
public class CoinbaseCallbackServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(CoinbaseCallbackServlet.class);
    private String urlParameters = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("doGet CoinbaseCallbackServlet!");

        //Obtain access code
        if (request.getParameter("code") != null) {
            String code = request.getParameter("code");
            logger.info("code = " + code);

            urlParameters = "?grant_type=authorization_code"
                    + "&code=" + code
                    + "&redirect_uri=" + CoinbasePaymentConstants.COINBASE_FULL_CALLBACK_URL
                    + "&client_id=" + CoinbaseConfiguration.getCoinbaseCredentials().getClientId()
                    + "&client_secret=" + CoinbaseConfiguration.getCoinbaseCredentials().getClientSecret();
            doPost(request, response);
        } else {
            logger.info("doPost CoinbaseCallbackServlet!");
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Initiate POST request to get access token
        URL obj = new URL(CoinbasePaymentConstants.COINBASE_REQUEST_TOKEN_ENDPOINT_URL + urlParameters);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);

        // Read response
        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String line;
        while ((line = br.readLine()) != null) {
            responseSB.append(line);
        }

        String responseString = responseSB.toString();
        String[] coinbaseTokens = responseString.split(",");

        String[] accessToken = coinbaseTokens[0].split(":");
        String finalAccesstoken = accessToken[1];

        br.close();
        request.setAttribute("coinbase_access_token", finalAccesstoken);

        response.sendRedirect(CoinbasePaymentConstants.COINBASE_CREATE_PROFILE_URL);

    }

}

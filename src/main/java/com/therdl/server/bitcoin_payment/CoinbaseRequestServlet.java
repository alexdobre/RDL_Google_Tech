package com.therdl.server.bitcoin_payment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Need a SSL host server in order to work
@Singleton
public class CoinbaseRequestServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(CoinbaseRequestServlet.class);

    private CoinbaseConfiguration coinbaseConfiguration;

    @Inject
    public CoinbaseRequestServlet(CoinbaseConfiguration coinbaseConfiguration) {
        this.coinbaseConfiguration = coinbaseConfiguration;

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        logger.info("process CoinbaseServlet!");
        String url = CoinbasePaymentConstants.COINBASE_AUTHORIZE_URL + "?response_type=code&client_id=" + CoinbaseConfiguration.getCoinbaseCredentials().getClientId()
                + "&redirect_uri=" + CoinbasePaymentConstants.COINBASE_FULL_CALLBACK_URL + "&scope=" + CoinbasePaymentConstants.COINBASE_SCOPE;
        try {
            //Redirect users from theRDL to coinbase authorization page
            response.sendRedirect(url);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}


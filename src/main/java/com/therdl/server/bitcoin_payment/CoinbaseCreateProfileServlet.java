package com.therdl.server.bitcoin_payment;

import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Button;
import com.coinbase.api.exception.CoinbaseException;
import com.google.inject.Singleton;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CoinbaseCreateProfileServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(CoinbaseCreateProfileServlet.class);


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = (String) request.getAttribute("coinbase_access_token");

        Coinbase coinbase = new CoinbaseBuilder()
                .withAccessToken(accessToken)
                .build();

        Button buttonParams = new Button();
        buttonParams.setText("This is the text");
        buttonParams.setDescription("This is the description");
        buttonParams.setPrice(Money.parse("USD 1.23"));
        buttonParams.setName("This is the name");
        buttonParams.setCustom("Custom tracking code here");
        buttonParams.setType(Button.Type.SUBSCRIPTION);
        try {
            Button button = coinbase.createButton(buttonParams);
            button.getCode();
        } catch (CoinbaseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        try {
            String id = coinbase.getUser().getId();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (CoinbaseException e) {
            logger.error(e.getMessage());
        }
    }

}

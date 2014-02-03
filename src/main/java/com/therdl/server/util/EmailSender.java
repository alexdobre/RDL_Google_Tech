package com.therdl.server.util;

import com.mongodb.*;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Sends e-mails using credentials from the database
 * Created by Alex on 03/02/14.
 */
public class EmailSender {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(EmailSender.class);

    /**
     *Sends an e-mail with the new user password
     * @param newPass
     * @param email
     */
    public static void sendNewPassEmail(String newPass,String email, DB db ){
        //get the mail credentials from the DB
        DBCollection coll = db.getCollection("mailCredentials");

        BasicDBObject query = new BasicDBObject();
        query.put("uid", 1);

        DBCursor cursor = coll.find(query);
        DBObject doc = cursor.next();

        String from = (String)doc.get("from");
        String to = email;
        String subject = "The RDL password reset";
        String message = "Your RDL password has been reset. Your new password is: "+newPass+" . Please go to www.therdl.com to log in and change it.";
        String login = (String)doc.get("login");
        String password = (String)doc.get("password");

        try {
            Properties props = new Properties();
            props.setProperty("mail.host", (String) doc.get("mailHost"));
            props.setProperty("mail.smtp.port",""+((Double)doc.get("smtpPort")).intValue());
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");

            Authenticator auth = new SMTPAuthenticator(login, password);

            Session session = Session.getInstance(props, auth);

            MimeMessage msg = new MimeMessage(session);
            msg.setText(message);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport.send(msg);

        } catch (AuthenticationFailedException ex) {
            sLogger.error("Authentication failed", ex);

        } catch (AddressException ex) {
            sLogger.error("Wrong email address", ex);

        } catch (MessagingException ex) {
            sLogger.error("Message exception", ex);
        }
    }

    private static class SMTPAuthenticator extends Authenticator {

        private PasswordAuthentication authentication;

        public SMTPAuthenticator(String login, String password) {
            authentication = new PasswordAuthentication(login, password);
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }


    public static void main(String args[]) {
        System.out.println("Send mail BEGIN: ");
        sendNewPassEmail(ServerUtils.generatePassword(), "alx.dobre@gmail.com",ServerUtils.getMongo());
        System.out.println("Send mail END: ");
    }
}

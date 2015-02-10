package com.therdl.server.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Message throttle. Limit posting of message to maximum of 3 post in a minute
 *
 * scenario 1: user can send maximum of 3 message in 1 minute
 * scenario 2: 3 spam warning in 1 hour will block the user
 *
 */
public class MessageThrottle {
    final Logger log = LoggerFactory.getLogger(MessageThrottle.class);

    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
    static final String MMDDYYY_HHMMSSA = "MM/dd/yyyy HH:mm:ss a";
    private HttpSession session;

    public MessageThrottle() {

    }

    public MessageThrottle(HttpSession session) {
        this.session = session;
    }

    /**
     * validate if user can post a message
     * @return
     */
    public  boolean canPostMessage() {
        log.info("canPostMessage - BEGIN");
         String timeSend = (String)session.getAttribute("timeSend");
        Integer msgCtr = (Integer)session.getAttribute("msgCtr");
        long interval =  minutesDiff(getItemDate(getCurrentDateString()), getItemDate(timeSend));
        log.info("canPostMessage :" + interval + " msgCtr :" + msgCtr + " timeSend :" + timeSend);
        if (msgCtr <= 3 && interval < 1) {
            return true;
        }
         else {
            //if user reach its limit update the spamCoutner
            Integer spamCounter = (Integer)session.getAttribute("spamWarning");
//            session.setAttribute("spamTimeStart",this.getCurrentDateString());
            session.setAttribute("spamWarning",spamCounter+1);
            this.updateSpammingTime();
             return false;
        }


    }

    /**
     * update the message sending time and message counter of the user
     *
     */
    public void updateTimeSend() {
    	log.info("updateTimeSend - BEGIN");
    	Integer msgCtr = (Integer)session.getAttribute("msgCtr");
       String timeSend = (String)session.getAttribute("timeSend");
        if (timeSend.isEmpty()) { //first send of messsage
            session.setAttribute("timeSend",this.getCurrentDateString());
            session.setAttribute("msgCtr",1);
        }else {
        //session.setAttribute("timeSend",this.getCurrentDateString());
	        msgCtr = (Integer)session.getAttribute("msgCtr");
	        long interval =  minutesDiff(getItemDate(getCurrentDateString()), getItemDate(timeSend));
	        if (msgCtr <= 3 && interval >= 1) {
	            //reset the counter
	        	session.setAttribute("msgCtr",0);
	        	session.setAttribute("timeSend",this.getCurrentDateString());
	        }
	        Integer spamCounter = (Integer)session.getAttribute("spamWarning");
	        if (spamCounter < 3) {
	        	if (msgCtr > 3 && interval >=1) {
	        		//rest the msg if the user is not yet block due to suspicious spamming
	        		session.setAttribute("msgCtr", 0);
	        		session.setAttribute("timeSend",this.getCurrentDateString());
	        	}
	        }
	        
//	        if (msgCtr > 3 && s)
        }
        session.setAttribute("msgCtr",msgCtr + 1);
        
       
    }

    /**
     * reset the time when the user is past 1 minute
     *
     */
    public void resetMessageCounter(){
        String timeSend =(String) session.getAttribute("timeSend");
        String currentTime = this.getCurrentDateString();
        int minutes = this.minutesDiff(this.getItemDate(currentTime),this.getItemDate(timeSend));
        if (minutes >= 1) {
            session.setAttribute("msgCtr",0);
        }


    }

    /**
     * get the current date of the server
     * @return
     */
    private String getCurrentDateString() {
        String timeSend = "";
        Calendar now = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat(MessageThrottle.MMDDYYY_HHMMSSA);
        timeSend = format.format(now.getTime());
        return timeSend;

    }

    /**
     * return a date format from a string
     * @param date
     * @return
     */
    private Date getItemDate(final String date)
    {
        final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        final SimpleDateFormat format = new SimpleDateFormat(MessageThrottle.MMDDYYY_HHMMSSA);
        format.setCalendar(cal);

        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * compute minutes between two date time
     * @param earlierDate
     * @param laterDate
     * @return
     */
    private int minutesDiff(Date earlierDate, Date laterDate)
    {
        if( earlierDate == null || laterDate == null ) return 0;

        return (int)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
    }

    /**
     * check the spamwarning within a user
     */
    public boolean isUserSpamming() {
        Integer spamWarning = (Integer)this.session.getAttribute("spamWarning");
        String currentDate = this.getCurrentDateString();
        String spamStart = (String)session.getAttribute("spamTimeStart");
        int interval = minutesDiff(getItemDate(currentDate), getItemDate(spamStart));

        if (spamWarning ==3 && interval >= 60) {
            //send email and block the user
            return true;
        }
        return false;
    }

    private void updateSpammingTime() {
        String spamTimeStart = (String)session.getAttribute("spamTimeStart");
        if (spamTimeStart.isEmpty()) { //first warning raise
            session.setAttribute("spamTimeStart",this.getCurrentDateString());
         }else {
            //chheck the spaming counter
            Integer spamCounter = (Integer)session.getAttribute("spamWarning");
            String currentDate = this.getCurrentDateString();
            String spamStart = (String)session.getAttribute("spamTimeStart");
            int interval = minutesDiff(getItemDate(currentDate), getItemDate(spamStart));
            if ( spamCounter < 3 && interval >=60 ) { //reset the spaming counter
                session.setAttribute("spamWarning",0);
                session.setAttribute("spamTimeStart","");
                session.setAttribute("msgCtr", 0); //also reset the msg counter;
            }
        }


    }
}

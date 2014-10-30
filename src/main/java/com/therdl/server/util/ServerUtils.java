package com.therdl.server.util;

import com.therdl.shared.RDLConstants;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.UserBean;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Contains business logic common utility methods to be used on the server side
 */
public class ServerUtils {

	static final Logger log = LoggerFactory.getLogger(ServerUtils.class);

	/**
	 * Extends the title specified by a month starting from the current date, creates a new title if one does not exist
	 *
	 * @param userBean the current user to add the title to
	 * @param title    the title name
	 * @param beanery  used to create a new title if necessary
	 */
	public static void extendTitle(UserBean userBean, String title, Beanery beanery) {
		List<UserBean.TitleBean> titleBeans = new ArrayList<UserBean.TitleBean>();
		UserBean.TitleBean foundTitle = null;
		for (UserBean.TitleBean titleBean : titleBeans) {
			if (titleBean.getTitleName().equals(title)) {
				foundTitle = titleBean;
				break;
			}
		}
		if (foundTitle == null) {
			foundTitle = beanery.userTitleBean().as();
			titleBeans.add(foundTitle);
		}
		foundTitle.setTitleName(title);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RDLConstants.DATE_PATTERN);
		foundTitle.setDateGained(simpleDateFormat.format(new Date()));
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, 1);
		foundTitle.setExpires(simpleDateFormat.format(cal.getTime()));
		userBean.setTitles(titleBeans);
	}


	/**
	 * Extends the title specified, creates a new title if one does not exist
	 *
	 * @param timeCreated     the time the title was gained
	 * @param nextPaymentDate the time the title expires
	 * @param userBean        the current user to add the title to
	 * @param title           the title name
	 * @param beanery         used to create a new title if necessary
	 */
	public static void extendTitle(String timeCreated, String nextPaymentDate, UserBean userBean, String title, Beanery beanery) {
		List<UserBean.TitleBean> titleBeans = new ArrayList<UserBean.TitleBean>();
		UserBean.TitleBean foundTitle = null;
		for (UserBean.TitleBean titleBean : titleBeans) {
			if (titleBean.getTitleName().equals(title)) {
				foundTitle = titleBean;
				break;
			}
		}
		if (foundTitle == null) {
			foundTitle = beanery.userTitleBean().as();
			titleBeans.add(foundTitle);
		}
		foundTitle.setTitleName(title);
		foundTitle.setDateGained(timeCreated);
		foundTitle.setExpires(nextPaymentDate);
		userBean.setTitles(titleBeans);
	}

	public static boolean isRdlSupporter(UserBean ub) {
		List<UserBean.TitleBean> titles = ub.getTitles();
		if (titles != null && !titles.isEmpty()) {
			for (UserBean.TitleBean titleBean : titles) {
				if (RDLConstants.UserTitle.RDL_SUPPORTER.equals(titleBean.getTitleName())) {
					return !isExpired(titleBean);
				}
			}
		}
		return false;
	}

	public static boolean isExpired(UserBean.TitleBean titleBean) {
		log.info("Checking is expired on title date: " + titleBean.getExpires());
		if (RDLConstants.UserTitle.NEVER_EXPIRES.equals(titleBean.getExpires()))
			return false;
		SimpleDateFormat dateFormat = new SimpleDateFormat(RDLConstants.DATE_PATTERN);
		try {
			//if the expiry date is before the current date then the title is expired
			return dateFormat.parse(titleBean.getExpires()).before(new Date());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return true;
		}
	}

	/**
	 * Encrypts a string - used for storing passwords
	 *
	 * @param input the string to be encrypted
	 * @return the encrypted string
	 */
	public static String encryptString(String input) {
		if (input == null || input.equals("")) {
			return null;
		}
		String hash = BCrypt.hashpw(input, BCrypt.gensalt());
		return hash;
	}

	/**
	 * Generates a new password for the user
	 *
	 * @return the randomly generated password
	 */
	public static String generatePassword() {
		//we generate a random sequence of 6 numbers
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append((int) (Math.random() * 10));
		}

		return sb.toString();
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static boolean isOneWeekOld(String date) throws ParseException {
		log.info("check isOneWeekOld for date: "+date);
		SimpleDateFormat sdf = new SimpleDateFormat(RDLConstants.DATE_PATTERN);
		long time = sdf.parse(date).getTime();
		//7 days have passed
		long sevenDays = (7 * 24 * 60 * 60 * 1000);
		if ((new Date().getTime() - time) > sevenDays) {
			log.info("check isOneWeekOld END return true");
			return true;
		}
		log.info("check isOneWeekOld END return false");
		return false;
	}
}

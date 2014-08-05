package com.therdl.client.view.common;

import com.therdl.client.view.PaginatedView;
import com.therdl.shared.Constants;

import java.util.logging.Logger;

/**
 * Common methods used in pagination
 */
public class PaginationHelper {
	protected static Logger log = Logger.getLogger(PaginationHelper.class.getName());

	public static void showPaginationOnView(int pageIndex, int listSize, PaginatedView view) {
		view.setListRange(calculateListRange(listSize, pageIndex));
		calculatePrevNextVisibility(pageIndex, listSize, view);
	}

	/**
	 * Returns the range of items in the list to be displayed based on the default page length
	 *
	 * @param listSize  the current list on the page
	 * @param pageIndex the current page index starting with 1
	 * @return the page range in the format 1-50
	 */
	public static String calculateListRange(int listSize, int pageIndex) {
		if (listSize == 0 && pageIndex == 0) return "0";

		if (listSize == 00 && pageIndex > 0){
			return "Limit reached, go back to previous page";
		}

		int startIndex = pageIndex * Constants.DEFAULT_PAGE_SIZE + 1;
		int stopIndex = startIndex + listSize - 1;
		return startIndex + "-" + stopIndex;
	}

	public static void calculatePrevNextVisibility(int pageIndex, int listSize, PaginatedView view){
		log.info("Pagination helper calculatePrevNextVisibility pageIndex: "+pageIndex+" listSize: "+listSize);

		if (listSize < Constants.DEFAULT_PAGE_SIZE) {
			view.nextPageActive(false);
		}else {
			view.nextPageActive(true);
		}
		if(pageIndex == 0){
			view.prevPageActive(false);
		}else{
			view.prevPageActive(true);
		}
	}
}

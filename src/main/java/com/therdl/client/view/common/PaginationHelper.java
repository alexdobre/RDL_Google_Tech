package com.therdl.client.view.common;

import com.therdl.client.view.PaginatedView;
import com.therdl.client.view.widget.ListWidget;
import com.therdl.shared.Constants;
import com.therdl.shared.events.GuiEventBus;
import com.therdl.shared.events.PaginationSnipsEvent;

import java.util.logging.Logger;

/**
 * Common methods used in pagination
 */
public class PaginationHelper {
	protected static Logger log = Logger.getLogger(PaginationHelper.class.getName());

	public static void fireNextPageEvent (int pageIndex, String listRange, int currentListSize, int maxListSize){
		//do nothing if upper limit was reached
		if (currentListSize < maxListSize){
			return;
		}else if (listRange.startsWith("Limit")){
			return;
		}
		GuiEventBus.EVENT_BUS.fireEvent(new PaginationSnipsEvent(true, pageIndex));
	}

	public static void firePrevPageEvent (int pageIndex){
		//do nothing if lower limit was reached
		if (pageIndex == 0 ) return;
		GuiEventBus.EVENT_BUS.fireEvent(new PaginationSnipsEvent(false, pageIndex));
	}

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
		int defaultPageSize;
		if (view instanceof ListWidget) {
			defaultPageSize = Constants.DEFAULT_PAGE_SIZE;
		}else {
			defaultPageSize = Constants.DEFAULT_REFERENCE_PAGE_SIZE;
		}

		if (listSize < defaultPageSize) {
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
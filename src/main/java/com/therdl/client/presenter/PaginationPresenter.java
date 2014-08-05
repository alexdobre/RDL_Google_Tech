package com.therdl.client.presenter;

import java.util.logging.Logger;

import com.therdl.client.view.PaginatedView;
import com.therdl.shared.Constants;

/**
 * Encapsulates pagination logic
 */
public class PaginationPresenter implements PaginatedView.Presenter {
	protected static Logger log = Logger.getLogger(PaginationPresenter.class.getName());

	private PaginatedView view;
	private PaginationFilter filter;

	public PaginationPresenter (PaginatedView view, PaginationFilter filter){
		this.view = view;
		this.filter = filter;
		calculatePrevNextVisibility();
	}


	@Override
	public void nextPage() {
		log.info("PaginationPresenter Next page");
		filter.doFilterSearch(view.getPageIndex()+1);
	}

	@Override
	public void prevPage() {
		log.info("PaginationPresenter Prev page");
		filter.doFilterSearch(view.getPageIndex()-1);
	}

	private void calculatePrevNextVisibility(){
		int pageIndex = view.getPageIndex();
		int currentListSize = 0;
		if (view.getItemList() != null){
			currentListSize = view.getItemList().size();
		}

		if (currentListSize < Constants.DEFAULT_PAGE_SIZE) {
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

	/**
	 * Returns the range of items in the list to be displayed based on the default page length
	 *
	 * @param listSize  the current list on the page
	 * @param pageIndex the current page index starting with 1
	 * @return the page range in the format 1-50
	 */
	public String calculateListRange(int listSize, int pageIndex) {
		if (listSize == 0) return "0";

		int startIndex = pageIndex * Constants.DEFAULT_PAGE_SIZE + 1;
		int stopIndex = startIndex + listSize - 1;
		return startIndex + "-" + stopIndex;
	}
}

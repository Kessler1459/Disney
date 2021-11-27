package Disney.utils;

import Disney.exception.PageException;

public class CheckPages {
    /**
     * Check if the selected page is valid
     */
    public static boolean checkPages(Integer totalPages, Integer selectedPage) {
        if (totalPages != 0 && ((selectedPage < 0) || (totalPages - 1 < selectedPage))) {
            throw new PageException(selectedPage, totalPages - 1);
        } else return true;
    }
}

package util;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private int pageNumber;
    private int pagesAvailable;
    private List pageItems = new ArrayList();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPagesAvailable(int pagesAvailable) {
        this.pagesAvailable = pagesAvailable;
    }

    public void setPageItems(List pageItems) {
        this.pageItems = pageItems;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPagesAvailable() {
        return pagesAvailable;
    }

    public List getPageItems() {
        return pageItems;
    }
}

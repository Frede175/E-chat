package Business.Models;

import Acquaintence.IChat;
import Acquaintence.IPage;

public class Page implements IPage {

    private int page;
    private int pageSize;

    public Page(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}

package Business.Models;

import Acquaintence.IChat;
import Acquaintence.IPage;
import Acquaintence.IToMap;

import java.util.HashMap;

public class Page implements IPage, IToMap {

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

    @Override
    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }
}
}

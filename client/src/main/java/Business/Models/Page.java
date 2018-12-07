package Business.Models;

import Acquaintence.IPage;
import Business.Interfaces.IParameters;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Page implements IPage, IParameters {

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


    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNumber", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("pageNumber", String.valueOf(page)));
        nvps.add(new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
        return nvps;
    }
}


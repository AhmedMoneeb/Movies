package com.solutionco.android.movies.Data;

/**
 * Created by Ahmed on 4/9/2017.
 */

public class Trailer {
    private String trailer_ID;
    private String key;
    private String site;

    public String getTrailer_ID() {
        return trailer_ID;
    }

    public void setTrailer_ID(String trailer_ID) {
        this.trailer_ID = trailer_ID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String url) {
        if (url.charAt(0)!='v' && url.charAt(1)!= '=')
            url="v="+url;
        this.key = url;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}

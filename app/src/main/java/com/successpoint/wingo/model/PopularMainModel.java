package com.successpoint.wingo.model;
import java.io.Serializable;
import java.util.List;

public class PopularMainModel implements Serializable {
    public String type;
    public String streamerImgUrl;
    public List<String> streamerUrl;
    public String streamerName;
    public String viewrs;

    public PopularMainModel(String type, String streamerImgUrl, List<String> streamerUrl, String streamerName, String viewrs) {
        this.type = type;
        this.streamerImgUrl = streamerImgUrl;
        this.streamerUrl = streamerUrl;
        this.streamerName = streamerName;
        this.viewrs = viewrs;
    }
}

package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultModel implements Parcelable
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;
    public final static Parcelable.Creator<SearchResultModel> CREATOR = new Creator<SearchResultModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SearchResultModel createFromParcel(Parcel in) {
            return new SearchResultModel(in);
        }

        public SearchResultModel[] newArray(int size) {
            return (new SearchResultModel[size]);
        }

    }
            ;

    protected SearchResultModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.content, (Content.class.getClassLoader()));
    }

    public SearchResultModel() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(message);
        dest.writeValue(count);
        dest.writeList(content);
    }

    public int describeContents() {
        return 0;
    }
    public static class Content implements Parcelable
    {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("picture")
        @Expose
        private String picture;
        @SerializedName("fans")
        @Expose
        private Integer fans;
        @SerializedName("last_login")
        @Expose
        private Object lastLogin;
        @SerializedName("online")
        @Expose
        private Boolean online;
        @SerializedName("live")
        @Expose
        private Boolean live;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("vip")
        @Expose
        private Integer vip;
        @SerializedName("following")
        @Expose
        private Boolean following;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("total_recived")
        @Expose
        private Integer totalRecived;
        @SerializedName("total_send")
        @Expose
        private Integer totalSend;
        @SerializedName("elite_id")
        @Expose
        private Object eliteId;
        @SerializedName("frame")
        @Expose
        private Integer frame;
        public final static Parcelable.Creator<Content> CREATOR = new Creator<Content>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Content createFromParcel(Parcel in) {
                return new Content(in);
            }

            public Content[] newArray(int size) {
                return (new Content[size]);
            }

        }
                ;

        protected Content(Parcel in) {
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.userId = ((String) in.readValue((String.class.getClassLoader())));
            this.picture = ((String) in.readValue((String.class.getClassLoader())));
            this.fans = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.lastLogin = ((Object) in.readValue((Object.class.getClassLoader())));
            this.online = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.live = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.level = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.vip = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.following = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.country = ((String) in.readValue((String.class.getClassLoader())));
            this.totalRecived = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.totalSend = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.eliteId = ((Object) in.readValue((Object.class.getClassLoader())));
            this.frame = ((Integer) in.readValue((Integer.class.getClassLoader())));
        }

        public Content() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public Integer getFans() {
            return fans;
        }

        public void setFans(Integer fans) {
            this.fans = fans;
        }

        public Object getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(Object lastLogin) {
            this.lastLogin = lastLogin;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public Boolean getLive() {
            return live;
        }

        public void setLive(Boolean live) {
            this.live = live;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getVip() {
            return vip;
        }

        public void setVip(Integer vip) {
            this.vip = vip;
        }

        public Boolean getFollowing() {
            return following;
        }

        public void setFollowing(Boolean following) {
            this.following = following;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getTotalRecived() {
            return totalRecived;
        }

        public void setTotalRecived(Integer totalRecived) {
            this.totalRecived = totalRecived;
        }

        public Integer getTotalSend() {
            return totalSend;
        }

        public void setTotalSend(Integer totalSend) {
            this.totalSend = totalSend;
        }

        public Object getEliteId() {
            return eliteId;
        }

        public void setEliteId(Object eliteId) {
            this.eliteId = eliteId;
        }

        public Integer getFrame() {
            return frame;
        }

        public void setFrame(Integer frame) {
            this.frame = frame;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(name);
            dest.writeValue(userId);
            dest.writeValue(picture);
            dest.writeValue(fans);
            dest.writeValue(lastLogin);
            dest.writeValue(online);
            dest.writeValue(live);
            dest.writeValue(level);
            dest.writeValue(vip);
            dest.writeValue(following);
            dest.writeValue(country);
            dest.writeValue(totalRecived);
            dest.writeValue(totalSend);
            dest.writeValue(eliteId);
            dest.writeValue(frame);
        }

        public int describeContents() {
            return 0;
        }

    }
}
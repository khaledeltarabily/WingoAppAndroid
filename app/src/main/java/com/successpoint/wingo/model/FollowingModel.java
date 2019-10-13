package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowingModel implements Parcelable
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
    public final static Parcelable.Creator<FollowingModel> CREATOR = new Creator<FollowingModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FollowingModel createFromParcel(Parcel in) {
            return new FollowingModel(in);
        }

        public FollowingModel[] newArray(int size) {
            return (new FollowingModel[size]);
        }

    }
            ;

    protected FollowingModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.content, (Content.class.getClassLoader()));
    }

    public FollowingModel() {
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
        @SerializedName("fans")
        @Expose
        private Integer fans;
        @SerializedName("picture")
        @Expose
        private String picture;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("vip")
        @Expose
        private Integer vip;
        @SerializedName("position")
        @Expose
        private Integer position;
        @SerializedName("frame")
        @Expose
        private Integer frame;
        @SerializedName("elite_id")
        @Expose
        private Object eliteId;
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
            this.fans = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.picture = ((String) in.readValue((String.class.getClassLoader())));
            this.userId = ((String) in.readValue((String.class.getClassLoader())));
            this.level = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.vip = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.position = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.frame = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.eliteId = ((Object) in.readValue((Object.class.getClassLoader())));
        }

        public Content() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getFans() {
            return fans;
        }

        public void setFans(Integer fans) {
            this.fans = fans;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public Integer getFrame() {
            return frame;
        }

        public void setFrame(Integer frame) {
            this.frame = frame;
        }

        public Object getEliteId() {
            return eliteId;
        }

        public void setEliteId(Object eliteId) {
            this.eliteId = eliteId;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(name);
            dest.writeValue(fans);
            dest.writeValue(picture);
            dest.writeValue(userId);
            dest.writeValue(level);
            dest.writeValue(vip);
            dest.writeValue(position);
            dest.writeValue(frame);
            dest.writeValue(eliteId);
        }

        public int describeContents() {
            return 0;
        }

    }
}
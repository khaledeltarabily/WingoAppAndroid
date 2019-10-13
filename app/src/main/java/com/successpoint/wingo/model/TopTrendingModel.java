package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopTrendingModel implements Parcelable
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
    public final static Parcelable.Creator<TopTrendingModel> CREATOR = new Creator<TopTrendingModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TopTrendingModel createFromParcel(Parcel in) {
            return new TopTrendingModel(in);
        }

        public TopTrendingModel[] newArray(int size) {
            return (new TopTrendingModel[size]);
        }

    }
            ;

    protected TopTrendingModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.content, (Content.class.getClassLoader()));
    }

    public TopTrendingModel() {
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
        @SerializedName("picture_index")
        @Expose
        private String pictureIndex;
        @SerializedName("user_id")
        @Expose
        private Integer userId;

        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

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
            this.pictureIndex = ((String) in.readValue((String.class.getClassLoader())));
            this.userId = ((Integer) in.readValue((Integer.class.getClassLoader())));
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

        public String getPictureIndex() {
            return pictureIndex;
        }

        public void setPictureIndex(String pictureIndex) {
            this.pictureIndex = pictureIndex;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(name);
            dest.writeValue(fans);
            dest.writeValue(pictureIndex);
            dest.writeValue(userId);
        }

        public int describeContents() {
            return 0;
        }

    }
}

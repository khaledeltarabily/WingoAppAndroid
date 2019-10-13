package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularModel implements Parcelable
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
    @SerializedName("max_page")
    @Expose
    private Integer maxPage;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;
    public final static Parcelable.Creator<PopularModel> CREATOR = new Creator<PopularModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PopularModel createFromParcel(Parcel in) {
            return new PopularModel(in);
        }

        public PopularModel[] newArray(int size) {
            return (new PopularModel[size]);
        }

    }
            ;

    protected PopularModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.content, (Content.class.getClassLoader()));
    }

    public PopularModel() {
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

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
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
        dest.writeValue(maxPage);
        dest.writeList(content);
    }

    public int describeContents() {
        return 0;
    }
    public static class Content implements Parcelable
    {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("viewers")
        @Expose
        private Integer viewers;
        @SerializedName("earned")
        @Expose
        private Integer earned;
        @SerializedName("boosted")
        @Expose
        private Boolean boosted;
        @SerializedName("pk")
        @Expose
        private Boolean pk;
        @SerializedName("lat")
        @Expose
        private Integer lat;
        @SerializedName("lon")
        @Expose
        private Double lon;
        @SerializedName("stream_description")
        @Expose
        private String streamDescription;
        @SerializedName("wowza_id")
        @Expose
        private String wowzaId;
        @SerializedName("stream_cover")
        @Expose
        private String streamCover;
        @SerializedName("prodcaster")
        @Expose
        private Prodcaster prodcaster;
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
            this.type = ((String) in.readValue((String.class.getClassLoader())));
            this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(this.data, (Datum.class.getClassLoader()));
            this.startTime = ((String) in.readValue((String.class.getClassLoader())));
            this.viewers = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.earned = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.boosted = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.pk = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.lat = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.lon = ((Double) in.readValue((Double.class.getClassLoader())));
            this.streamDescription = ((String) in.readValue((String.class.getClassLoader())));
            this.wowzaId = ((String) in.readValue((String.class.getClassLoader())));
            this.streamCover = ((String) in.readValue((String.class.getClassLoader())));
            this.prodcaster = ((Prodcaster) in.readValue((Prodcaster.class.getClassLoader())));
        }

        public Content() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Integer getViewers() {
            return viewers;
        }

        public void setViewers(Integer viewers) {
            this.viewers = viewers;
        }

        public Integer getEarned() {
            return earned;
        }

        public void setEarned(Integer earned) {
            this.earned = earned;
        }

        public Boolean getBoosted() {
            return boosted;
        }

        public void setBoosted(Boolean boosted) {
            this.boosted = boosted;
        }

        public Boolean getPk() {
            return pk;
        }

        public void setPk(Boolean pk) {
            this.pk = pk;
        }

        public Integer getLat() {
            return lat;
        }

        public void setLat(Integer lat) {
            this.lat = lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public String getStreamDescription() {
            return streamDescription;
        }

        public void setStreamDescription(String streamDescription) {
            this.streamDescription = streamDescription;
        }

        public String getWowzaId() {
            return wowzaId;
        }

        public void setWowzaId(String wowzaId) {
            this.wowzaId = wowzaId;
        }

        public String getStreamCover() {
            return streamCover;
        }

        public void setStreamCover(String streamCover) {
            this.streamCover = streamCover;
        }

        public Prodcaster getProdcaster() {
            return prodcaster;
        }

        public void setProdcaster(Prodcaster prodcaster) {
            this.prodcaster = prodcaster;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(type);
            dest.writeValue(count);
            dest.writeList(data);
            dest.writeValue(startTime);
            dest.writeValue(viewers);
            dest.writeValue(earned);
            dest.writeValue(boosted);
            dest.writeValue(pk);
            dest.writeValue(lat);
            dest.writeValue(lon);
            dest.writeValue(streamDescription);
            dest.writeValue(wowzaId);
            dest.writeValue(streamCover);
            dest.writeValue(prodcaster);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class Prodcaster implements Parcelable
    {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("followers")
        @Expose
        private Integer followers;
        @SerializedName("position")
        @Expose
        private Integer position;
        @SerializedName("gender")
        @Expose
        private String gender;
        public final static Parcelable.Creator<Prodcaster> CREATOR = new Creator<Prodcaster>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Prodcaster createFromParcel(Parcel in) {
                return new Prodcaster(in);
            }

            public Prodcaster[] newArray(int size) {
                return (new Prodcaster[size]);
            }

        }
                ;

        protected Prodcaster(Parcel in) {
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.id = ((String) in.readValue((String.class.getClassLoader())));
            this.followers = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.position = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.gender = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Prodcaster() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getFollowers() {
            return followers;
        }

        public void setFollowers(Integer followers) {
            this.followers = followers;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(name);
            dest.writeValue(id);
            dest.writeValue(followers);
            dest.writeValue(position);
            dest.writeValue(gender);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class Datum implements Parcelable
    {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("image")
        @Expose
        private String image;
        public final static Parcelable.Creator<Datum> CREATOR = new Creator<Datum>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Datum createFromParcel(Parcel in) {
                return new Datum(in);
            }

            public Datum[] newArray(int size) {
                return (new Datum[size]);
            }

        }
                ;

        protected Datum(Parcel in) {
            this.url = ((String) in.readValue((String.class.getClassLoader())));
            this.image = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Datum() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(url);
            dest.writeValue(image);
        }

        public int describeContents() {
            return 0;
        }

    }
}
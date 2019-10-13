package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyModel implements Parcelable
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
    public final static Parcelable.Creator<NearbyModel> CREATOR = new Creator<NearbyModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NearbyModel createFromParcel(Parcel in) {
            return new NearbyModel(in);
        }

        public NearbyModel[] newArray(int size) {
            return (new NearbyModel[size]);
        }

    }
            ;

    protected NearbyModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.content, ( Content.class.getClassLoader()));
    }

    public NearbyModel() {
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

        @SerializedName("distance")
        @Expose
        private Double distance;
        @SerializedName("stream_id")
        @Expose
        private String streamId;
        @SerializedName("stream_description")
        @Expose
        private String streamDescription;
        @SerializedName("viewers")
        @Expose
        private Integer viewers;
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
            this.distance = ((Double) in.readValue((Double.class.getClassLoader())));
            this.streamId = ((String) in.readValue((String.class.getClassLoader())));
            this.streamDescription = ((String) in.readValue((String.class.getClassLoader())));
            this.viewers = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.streamCover = ((String) in.readValue((String.class.getClassLoader())));
            this.prodcaster = ((Prodcaster) in.readValue((Prodcaster.class.getClassLoader())));
        }

        public Content() {
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public String getStreamId() {
            return streamId;
        }

        public void setStreamId(String streamId) {
            this.streamId = streamId;
        }

        public String getStreamDescription() {
            return streamDescription;
        }

        public void setStreamDescription(String streamDescription) {
            this.streamDescription = streamDescription;
        }

        public Integer getViewers() {
            return viewers;
        }

        public void setViewers(Integer viewers) {
            this.viewers = viewers;
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
            dest.writeValue(distance);
            dest.writeValue(streamId);
            dest.writeValue(streamDescription);
            dest.writeValue(viewers);
            dest.writeValue(streamCover);
            dest.writeValue(prodcaster);
        }

        public int describeContents() {
            return 0;
        }

    }
    public static class Prodcaster implements Parcelable
    {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
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
            this.userId = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Prodcaster() {
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(userId);
            dest.writeValue(name);
        }

        public int describeContents() {
            return 0;
        }

    }
}
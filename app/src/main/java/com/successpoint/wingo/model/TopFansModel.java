package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopFansModel implements Parcelable
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
    private List<Datum> data = null;
    public final static Parcelable.Creator<TopFansModel> CREATOR = new Creator<TopFansModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TopFansModel createFromParcel(Parcel in) {
            return new TopFansModel(in);
        }

        public TopFansModel[] newArray(int size) {
            return (new TopFansModel[size]);
        }

    }
            ;

    protected TopFansModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (Datum.class.getClassLoader()));
    }

    public TopFansModel() {
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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(message);
        dest.writeValue(count);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

    public static class Datum implements Parcelable
    {

        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("picture")
        @Expose
        private String picture;
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
            this.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.userId = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.level = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.picture = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Datum() {
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
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

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(amount);
            dest.writeValue(userId);
            dest.writeValue(name);
            dest.writeValue(level);
            dest.writeValue(picture);
        }

        public int describeContents() {
            return 0;
        }

    }

}
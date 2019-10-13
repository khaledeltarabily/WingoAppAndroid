package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginModel implements Parcelable
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_token")
    @Expose
    private String userToken;
    public final static Parcelable.Creator<SocialLoginModel> CREATOR = new Creator<SocialLoginModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SocialLoginModel createFromParcel(Parcel in) {
            return new SocialLoginModel(in);
        }

        public SocialLoginModel[] newArray(int size) {
            return (new SocialLoginModel[size]);
        }

    }
            ;

    protected SocialLoginModel(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.userToken = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SocialLoginModel() {
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(message);
        dest.writeValue(userToken);
    }

    public int describeContents() {
        return 0;
    }

}

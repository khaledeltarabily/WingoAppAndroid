package com.successpoint.wingo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CountryModel implements Parcelable
{

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("alpha2Code")
    @Expose
    private String alpha2Code;
    @SerializedName("callingCodes")
    @Expose
    private List<String> callingCodes = new ArrayList<>();
    public final static Parcelable.Creator<CountryModel> CREATOR = new Creator<CountryModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CountryModel createFromParcel(Parcel in) {
            return new CountryModel(in);
        }

        public CountryModel[] newArray(int size) {
            return (new CountryModel[size]);
        }

    }
            ;

    protected CountryModel(Parcel in) {
        this.flag = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.alpha2Code = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.callingCodes, (java.lang.String.class.getClassLoader()));
    }

    public CountryModel() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public List<String> getCallingCodes() {
        return callingCodes;
    }

    public void setCallingCodes(List<String> callingCodes) {
        this.callingCodes = callingCodes;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(flag);
        dest.writeValue(name);
        dest.writeValue(alpha2Code);
        dest.writeList(callingCodes);
    }

    public int describeContents() {
        return 0;
    }

}

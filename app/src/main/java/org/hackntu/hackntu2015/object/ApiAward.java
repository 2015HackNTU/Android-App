package org.hackntu.hackntu2015.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weitang114 on 15/7/19.
 */
public class ApiAward implements Parcelable {
    public String imageUrl;
    public String companyName;
    public String companyInfo;
    public String prize;
    public String criteria;
    public int priority;

    public ApiAward(String imageUrl, String companyName, String companyInfo, String prize, String
            criteria, int priority) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.companyInfo = companyInfo;
        this.prize = prize;
        this.criteria = criteria;
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.companyName);
        dest.writeString(this.companyInfo);
        dest.writeString(this.prize);
        dest.writeString(this.criteria);
        dest.writeInt(this.priority);
    }

    protected ApiAward(Parcel in) {
        this.imageUrl = in.readString();
        this.companyName = in.readString();
        this.companyInfo = in.readString();
        this.prize = in.readString();
        this.criteria = in.readString();
        this.priority = in.readInt();
    }

    public static final Creator<ApiAward> CREATOR = new Creator<ApiAward>() {
        public ApiAward createFromParcel(Parcel source) {
            return new ApiAward(source);
        }

        public ApiAward[] newArray(int size) {
            return new ApiAward[size];
        }
    };
}

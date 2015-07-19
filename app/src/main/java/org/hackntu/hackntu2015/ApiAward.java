package org.hackntu.hackntu2015;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weitang114 on 15/7/19.
 */
public class ApiAward implements Parcelable {
    public String imageUrl;
    public String companyName;
    public String prize;
    public String criteria;

    public ApiAward(String imageUrl, String companyName, String prize, String criteria) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.prize = prize;
        this.criteria = criteria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.companyName);
        dest.writeString(this.prize);
        dest.writeString(this.criteria);
    }

    protected ApiAward(Parcel in) {
        this.imageUrl = in.readString();
        this.companyName = in.readString();
        this.prize = in.readString();
        this.criteria = in.readString();
    }

    public static final Parcelable.Creator<ApiAward> CREATOR = new Parcelable.Creator<ApiAward>() {
        public ApiAward createFromParcel(Parcel source) {
            return new ApiAward(source);
        }

        public ApiAward[] newArray(int size) {
            return new ApiAward[size];
        }
    };
}

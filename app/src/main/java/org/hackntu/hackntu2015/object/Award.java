package org.hackntu.hackntu2015.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weitang114 on 15/7/24.
 */
public class Award implements Parcelable {

    public int rank;
    public String prize;
    public String criteria;

    public Award(int rank, String prize, String criteria) {
        this.rank = rank;
        this.prize = prize;
        this.criteria = criteria;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.rank);
        dest.writeString(this.prize);
        dest.writeString(this.criteria);
    }

    protected Award(Parcel in) {
        this.rank = in.readInt();
        this.prize = in.readString();
        this.criteria = in.readString();
    }

    public static class Comparator implements java.util.Comparator<Award> {
        @Override
        public int compare(Award lhs, Award rhs) {
            return lhs.rank < rhs.rank ? -1
                    : (lhs.rank == rhs.rank ? 0 : 1);
        }
    }

    public static final Creator<Award> CREATOR = new Creator<Award>() {
        public Award createFromParcel(Parcel source) {
            return new Award(source);
        }

        public Award[] newArray(int size) {
            return new Award[size];
        }
    };
}

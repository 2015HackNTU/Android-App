package org.hackntu.hackntu2015.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;

import org.hackntu.hackntu2015.R;
import org.hackntu.hackntu2015.object.ApiAward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class APIAwardAdapter extends RecyclerView.Adapter<APIAwardAdapter.ViewHolder> {
    List<ApiAward> data;

    public APIAwardAdapter() {}

    public void changeDataset(List<ParseObject> list) {
        data = convertParseAwards(list);
        notifyDataSetChanged();
    }


    private List<ApiAward> convertParseAwards(List<ParseObject> list) {
        List<ApiAward> newList = new ArrayList<>();
        if (list == null) return newList;

        for (ParseObject p : list) {
            ParseFile image = p.getParseFile("image");
            String imageUrl = image != null ? image.getUrl() : null;

            ApiAward award = new ApiAward(
                    imageUrl,
                    p.getString("name"),
                    p.getString("info"),
                    p.getString("prize"),
                    p.getString("criteria"),
                    p.getInt("priority")
            );
            newList.add(award);
        }

        sortAwardsByPriority(newList);

        return newList;
    }

    public ApiAward getItem(int index) {
        if (data == null) return null;
        return data.get(index);
    }


    private void sortAwardsByPriority(List<ApiAward> list) {
        Comparator<ApiAward> comp = new Comparator<ApiAward>() {
            @Override
            public int compare(ApiAward lhs, ApiAward rhs) {
                return lhs.priority < rhs.priority ? -1 :
                        (lhs.priority == rhs.priority ? 0 : 1);
            }
        };
        Collections.sort(list, comp);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_award, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title= data.get(position).companyName;
        holder.title.setText(title);
        holder.index=position;
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each olddata item is just a string in this case
        public TextView title;
        public int index;
        public CardView cardView;

        public ViewHolder (CardView cardView) {
            super(cardView);
            title = (TextView) cardView.findViewById(R.id.card_title_award);
            this.cardView = cardView;
        }
    }
}
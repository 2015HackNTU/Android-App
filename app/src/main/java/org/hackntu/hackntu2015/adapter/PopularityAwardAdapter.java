package org.hackntu.hackntu2015.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import org.hackntu.hackntu2015.object.Award;
import org.hackntu.hackntu2015.R;

import java.util.ArrayList;
import java.util.List;


public class PopularityAwardAdapter extends RecyclerView.Adapter<PopularityAwardAdapter.ViewHolder> {
    List<Award> data;
    Context context;

    public PopularityAwardAdapter(Context context) {
        this.context = context;
    }

    public void changeDataset(List<ParseObject> list) {
        data = convertParseAwards(list);
        notifyDataSetChanged();
    }


    private List<Award> convertParseAwards(List<ParseObject> list) {
        List<Award> newList = new ArrayList<>(10);
        if (list == null) return newList;

        for (ParseObject p : list) {
            int rank = p.getInt("order");
            Award award = new Award(
                    rank,
                    p.getString("prizeDes"),
                    p.getString("criteriaDes")
            );
            newList.add(rank - 1, award);
        }
        return newList;
    }

    public Award getItem(int index) {
        if (data == null) return null;
        return data.get(index);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_award, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] awardRanks = context.getResources().getStringArray(R.array.award_ranks);
        String title = awardRanks[position];
        holder.title.setText(title);
        holder.index = position;
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
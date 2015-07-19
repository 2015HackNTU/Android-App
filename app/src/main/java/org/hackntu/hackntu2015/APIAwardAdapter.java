package org.hackntu.hackntu2015;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class APIAwardAdapter extends RecyclerView.Adapter<APIAwardAdapter.ViewHolder> {
    CardViewClickListener mListener;

    List<ApiAward> data;

    public APIAwardAdapter(CardViewClickListener listener) {
        mListener = listener;
    }

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
                    p.getString("prize"),
                    p.getString("criteria")
            );
            newList.add(award);
        }
        return newList;
    }

    public ApiAward getItem(int index) {
        if (data == null) return null;
        return data.get(index);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_award, parent, false);
        return new ViewHolder(v, mListener);
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



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each olddata item is just a string in this case
        public TextView title;
        public int index;
        public CardView cardView;
        CardViewClickListener listener;

//		public RelativeLayout parent; // for removing description TextView when it's empty

        public ViewHolder (CardView cardView, CardViewClickListener listener) {
            super(cardView);
            title = (TextView) cardView.findViewById(R.id.card_title_award);
            this.cardView = cardView;
            cardView.setOnClickListener(this);
            this.listener = listener;
        }
        @Override
        public void onClick(View v) {
            //Log.i("test", "successapi" + index);
            listener.onClick(index);
        }

    }

    public static interface CardViewClickListener {
        public void onClick(int index);
    }
}
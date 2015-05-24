package com.treehacks.treehacks;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Created by mac on 15/5/10.
 */
public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder> {
    String[] data={"general top10","general top3","API"};
    static View.OnClickListener listener;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_award, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title=data[position];
        holder.title.setText(title);
        holder.index=position;
        if (position == 2) {
            holder.cardView.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView title;
        public int index;
        public CardView cardView;

//		public RelativeLayout parent; // for removing description TextView when it's empty

        public ViewHolder (CardView cardView) {
            super(cardView);
            title = (TextView) cardView.findViewById(R.id.card_title_award);
            this.cardView = cardView;
            //cardView.setOnClickListener(listener);

//	        parent = (RelativeLayout) description.getParent();

        }
        @Override
        public void onClick(View v) {
            //Log.i("test", "success"+index);


        }


    }

}
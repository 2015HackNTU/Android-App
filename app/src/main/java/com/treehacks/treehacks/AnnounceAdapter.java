package com.treehacks.treehacks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by eddie_000 on 1/25/2015.
 */
public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {
    public List<ParseObject> announcements;

    public AnnounceAdapter(List<ParseObject> announcements) {
        this.announcements = announcements;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView description;
	    public TextView time;

        public ViewHolder (CardView cardView) {
            super(cardView);
            title = (TextView) cardView.findViewById(R.id.card_title);
            description = (TextView) cardView.findViewById(R.id.card_description);
	        time = (TextView) cardView.findViewById(R.id.card_time);
        }
    }

    @Override
    public AnnounceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_announce, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AnnounceAdapter.ViewHolder viewHolder, int i) {
        ParseObject announcement = announcements.get(i);
        viewHolder.title.setText(announcement.getString("title"));
        viewHolder.description.setText(announcement.getString("description"));
	    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
	    viewHolder.time.setText(sdf.format(announcement.getUpdatedAt()));
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }
}

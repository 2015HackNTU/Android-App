package org.hackntu.hackntu2015;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
 * Created by eddie_000 on 1/25/2015.
 */
public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> implements Filterable {
    public List<ParseObject> announcements;
	List<ParseObject> filteredAnnouncements;

	public AnnounceAdapter() {
		this(new ArrayList<ParseObject>());
	}

    public AnnounceAdapter(List<ParseObject> announcements) {
	    super();
        this.announcements = announcements;
	    filteredAnnouncements = announcements;
    }

	public void changeDataSet(List<ParseObject> set) {
		announcements = set;
		filteredAnnouncements = set;
		notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {return announceFilter;
	}

	// Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView description;
	    public TextView time;
//		public RelativeLayout parent; // for removing description TextView when it's empty

        public ViewHolder (CardView cardView) {
            super(cardView);
            title = (TextView) cardView.findViewById(R.id.card_title);
            description = (TextView) cardView.findViewById(R.id.card_description);
	        time = (TextView) cardView.findViewById(R.id.card_time);
//	        parent = (RelativeLayout) description.getParent();
        }
    }

    @Override
    public AnnounceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_announce, parent, false);
	    return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnnounceAdapter.ViewHolder viewHolder, int i) {
        ParseObject announcement = filteredAnnouncements.get(i);
        viewHolder.title.setText(announcement.getString("title"));
	    String description = announcement.getString("description");
	    viewHolder.description.setText(description);

	    // Remove description field when empty
	    ViewGroup.LayoutParams lps = viewHolder.description.getLayoutParams();
	    if (description == null || description.trim().isEmpty())
		    lps.height = 0;
		else
		    lps.height = ViewGroup.LayoutParams.WRAP_CONTENT;
	    viewHolder.description.setLayoutParams(lps);

	    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
	    viewHolder.time.setText(sdf.format(announcement.getUpdatedAt()));
    }

    @Override
    public int getItemCount() {
        return filteredAnnouncements.size();
    }

	Filter announceFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			ArrayList<ParseObject> retainedAnnouncements = new ArrayList<>();
			// Add announcements if either of title or description match
			for (ParseObject announcement : announcements) {
				if ((announcement.containsKey("title") && announcement.getString("title").toLowerCase().contains(filterString)) ||
						(announcement.containsKey("description") && announcement.getString("description").toLowerCase().contains(filterString))) {
					retainedAnnouncements.add(announcement);
				}
			}
			results.values = retainedAnnouncements;
			results.count = retainedAnnouncements.size();
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filteredAnnouncements = (List<ParseObject>) results.values;
			notifyDataSetChanged();
		}
	};
}

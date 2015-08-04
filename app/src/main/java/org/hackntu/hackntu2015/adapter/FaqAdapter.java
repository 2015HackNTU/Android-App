package org.hackntu.hackntu2015.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.parse.ParseObject;

import org.hackntu.hackntu2015.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2/1/2015.
 */
public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> implements Filterable{
	public List<ParseObject> faqs;
	List<ParseObject> filteredFaqs;

	public FaqAdapter() {
		this(new ArrayList<ParseObject>());
	}

	public FaqAdapter(List<ParseObject> faqs) {
		super();
		this.faqs = faqs;
		filteredFaqs = faqs;
	}

	public void changeDataSet(List<ParseObject> set) {
		faqs = set;
		filteredFaqs = set;
		notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return faqFilter;
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView question;
		public TextView answer;

		public ViewHolder (CardView cardView) {
			super(cardView);
			question = (TextView) cardView.findViewById(R.id.card_question);
			answer = (TextView) cardView.findViewById(R.id.card_answer);
		}
	}

	@Override
	public FaqAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_faq, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(FaqAdapter.ViewHolder viewHolder, int i) {
		ParseObject announcement = filteredFaqs.get(i);
		viewHolder.question.setText(announcement.getString("question"));
		viewHolder.answer.setText(announcement.getString("answer"));
	}

	@Override
	public int getItemCount() {
		return filteredFaqs.size();
	}

	Filter faqFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			ArrayList<ParseObject> retainedFaqs = new ArrayList<>();
			// Add announcements if either of title or description match
			for (ParseObject faq : faqs) {
				if ((faq.containsKey("question") && faq.getString("question").toLowerCase().contains(filterString)) ||
						(faq.containsKey("answer") && faq.getString("answer").toLowerCase().contains(filterString))) {
					retainedFaqs.add(faq);
				}
			}
			results.values = retainedFaqs;
			results.count = retainedFaqs.size();
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filteredFaqs = (List<ParseObject>) results.values;
			notifyDataSetChanged();
		}
	};
}

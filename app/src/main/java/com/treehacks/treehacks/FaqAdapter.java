package com.treehacks.treehacks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Eddie on 2/1/2015.
 */
public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
	public List<ParseObject> faqs;

	public FaqAdapter(List<ParseObject> announcements) {
		this.faqs = announcements;
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
		CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_faq, parent, false);
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(FaqAdapter.ViewHolder viewHolder, int i) {
		ParseObject announcement = faqs.get(i);
		viewHolder.question.setText(announcement.getString("question"));
		viewHolder.answer.setText(announcement.getString("answer"));
	}

	@Override
	public int getItemCount() {
		return faqs.size();
	}
}

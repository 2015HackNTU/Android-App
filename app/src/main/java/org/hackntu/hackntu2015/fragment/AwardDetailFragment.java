package org.hackntu.hackntu2015.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackntu.hackntu2015.object.Award;
import org.hackntu.hackntu2015.R;

/**
 * Created by weitang114 on 15/7/24.
 */
public class AwardDetailFragment extends Fragment {

    public static final String AWARD = "award";
    Award award;

    public static AwardDetailFragment newInstance(Award award) {
        AwardDetailFragment frag = new AwardDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(AWARD, award);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        award = getArguments().getParcelable(AWARD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_awarddetail, container, false);
        TextView titleText = (TextView) rootView.findViewById(R.id.txt_title);
        TextView prizeText = (TextView) rootView.findViewById(R.id.txt_prize);
        TextView criteriaText = (TextView) rootView.findViewById(R.id.txt_criteria);

        String[] ranks = getActivity().getResources().getStringArray(R.array.award_ranks);
        titleText.setText(ranks[award.rank - 1]);
        prizeText.setText(award.prize);
        criteriaText.setText(award.criteria);

        return rootView;
    }
}

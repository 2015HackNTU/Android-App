package com.treehacks.treehacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Arrays;

/**
 * Created by eddie_000 on 2/1/2015.
 */
public class ReportFragment extends Fragment {
    View reportForm;
    Button makeButton;
    Button submitButton;
    EditText email, seat, tags, descr;
    boolean form_visible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        reportForm = rootView.findViewById(R.id.report_form);
        reportForm.setVisibility(View.INVISIBLE);
        makeButton = (Button) rootView.findViewById(R.id.btn_make_report);
        submitButton = (Button) rootView.findViewById(R.id.btn_submit_report);
        email = (EditText) rootView.findViewById(R.id.edit_email);
        seat = (EditText) rootView.findViewById(R.id.edit_seat_number);
        tags = (EditText) rootView.findViewById(R.id.edit_tags);
        descr = (EditText) rootView.findViewById(R.id.edit_description);
        form_visible = false;
        makeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (form_visible) {
                    makeButton.setText("Report an issue");
                    reportForm.setVisibility(View.INVISIBLE);
                    email.setText("");
                    seat.setText("");
                    tags.setText("");
                    descr.setText("");
                    form_visible = false;
                }
                else {
                    makeButton.setText("Cancel");
                    reportForm.setVisibility(View.VISIBLE);
                    form_visible = true;
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseObject ticket = new ParseObject("Ticket");
                ticket.add("email", email.getText().toString());
                ticket.add("seatNumber", seat.getText().toString());
                ticket.add("description", descr.getText().toString());
                ticket.addAll("tags", Arrays.asList(tags.getText().toString().split(" ")));
                ticket.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        String message = "Submission success!";
                        if (e != null) {
                            message = "Submission failed";
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
                        makeButton.setText("Report an issue");
                        reportForm.setVisibility(View.INVISIBLE);
                        email.setText("");
                        seat.setText("");
                        tags.setText("");
                        descr.setText("");
                        form_visible = false;
                    }
                });
            }
        });
        return rootView;
    }
}

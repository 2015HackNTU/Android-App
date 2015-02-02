package com.treehacks.treehacks;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
	    submitButton.setEnabled(false);
        email = (EditText) rootView.findViewById(R.id.edit_email);
        seat = (EditText) rootView.findViewById(R.id.edit_seat_number);
        tags = (EditText) rootView.findViewById(R.id.edit_tags);
        descr = (EditText) rootView.findViewById(R.id.edit_description);
        form_visible = false;
        makeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (form_visible) {
	                // Cancel form input
	                hideFormInput(v);
                }
                else {
	                // Make form input
                    makeButton.setText("Cancel");
                    reportForm.setVisibility(View.VISIBLE);
                    form_visible = true;
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                ParseObject ticket = new ParseObject("Ticket");
                ticket.put("email", email.getText().toString());
                ticket.put("seatNumber", Integer.parseInt(seat.getText().toString()));
                ticket.put("description", descr.getText().toString());
                ticket.put("tags", Arrays.asList(tags.getText().toString().split("\\s+")));
                ticket.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        String message = "Submission success!";
                        if (e != null) {
                            message = "Submission failed";
	                        e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	                    hideFormInput(v);
                    }
                });
            }
        });
	    descr.addTextChangedListener(validReportListener);
        return rootView;
    }

	private void hideFormInput(View focus) {
		makeButton.setText("Report an issue");
		reportForm.setVisibility(View.INVISIBLE);
		email.setText("");
		seat.setText("");
		tags.setText("");
		descr.setText("");
		form_visible = false;
		// Hide soft keyboard if it's active
		if (focus != null) {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
		}
	}

	private TextWatcher validReportListener = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			boolean canSubmit = !(isEmpty(descr));
			submitButton.setEnabled(canSubmit);
		}
	};

	private static boolean isEmpty(EditText editText) {
		return editText.getText().toString().trim().length() == 0;
	}
}

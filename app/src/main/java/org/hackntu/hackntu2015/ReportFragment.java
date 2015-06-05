package org.hackntu.hackntu2015;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.treehacks.treehacks.R;

/**
 * Created by eddie_000 on 2/1/2015.
 */
public class ReportFragment extends Fragment {
    EditText name, email, location, category, description;
	Spinner categorySelector;
	MenuItem submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
	    setHasOptionsMenu(true);
	    name = (EditText) rootView.findViewById(R.id.edit_name);
        email = (EditText) rootView.findViewById(R.id.edit_email);
        location = (EditText) rootView.findViewById(R.id.edit_location);
        category = (EditText) rootView.findViewById(R.id.edit_category);
	    category.setVisibility(View.INVISIBLE);
	    categorySelector = (Spinner) rootView.findViewById(R.id.spin_category);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cat_names, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    categorySelector.setAdapter(adapter);
	    categorySelector.setOnItemSelectedListener(otherListener);
        description = (EditText) rootView.findViewById(R.id.edit_description);
	    description.addTextChangedListener(validReportListener);
        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_report, menu);
		submitButton = menu.findItem(R.id.action_report);
		submitButton.setEnabled(canSubmit());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_report) {
			submitReport();
			return true;
		}
		return false;
	}

	void submitReport() {
		ParseObject ticket = new ParseObject("Ticket");
		ticket.put("name", name.getText().toString());
		ticket.put("email", email.getText().toString());
		ticket.put("location", location.getText().toString());
		String categoryString = categorySelector.getSelectedItem().toString();
		if (categoryString.equals("Other"))
			categoryString = category.getText().toString();
		ticket.put("category", categoryString);
		ticket.put("description", description.getText().toString());
		ticket.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				String message = "Submission success!";
				if (e != null) {
					message = "Submission failed";
					e.printStackTrace();
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				hideSoftInput();
			}
		});
		categorySelector.setSelection(0);
		description.setText("");
	}

	private void hideSoftInput() {
		// Hide soft keyboard if it's active
		if (getActivity().getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
			if (submitButton != null)
				submitButton.setEnabled(canSubmit());
		}
	};

	boolean canSubmit() {
		return !(isEmpty(description));
	}

	private static boolean isEmpty(EditText editText) {
		return editText.getText().toString().trim().length() == 0;
	}

	AdapterView.OnItemSelectedListener otherListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String selected = parent.getItemAtPosition(position).toString();
			category.setVisibility(selected.equals("Other") ? View.VISIBLE: View.INVISIBLE);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			category.setVisibility(View.INVISIBLE);
		}
	};
}

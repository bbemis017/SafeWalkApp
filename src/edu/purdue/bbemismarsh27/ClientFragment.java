package edu.purdue.bbemismarsh27;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * This fragment is the "page" where the user inputs information about the
 * request, he/she wishes to send.
 *
 * @author YL
 */
public class ClientFragment extends Fragment implements OnClickListener {

	/**
	 * Activity which have to receive callbacks.
	 */
	private SubmitCallbackListener activity;
	
	private RadioGroup preferences;
	private EditText name;
	private Spinner from,to;

	/**
	 * Creates a ProfileFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the submit Button.
	 * 
	 *            ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	 * 
	 * @return the fragment initialized.
	 */
	// ** DO NOT CREATE A CONSTRUCTOR FOR ProfileFragment **
	public static ClientFragment newInstance(SubmitCallbackListener activity) {
		ClientFragment f = new ClientFragment();

		f.activity = activity;
		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.client_fragment_layout,container, false);

		/**
		 * Register this fragment to be the OnClickListener for the submit
		 * Button.
		 */
		view.findViewById(R.id.bu_submit).setOnClickListener(this);

		// import your Views from the layout here. See example in
		// ServerFragment.
		name = (EditText)view.findViewById(R.id.et_name);
		preferences = (RadioGroup) view.findViewById(R.id.rg_preferences);
		from = (Spinner)view.findViewById(R.id.spin_from);
		ArrayAdapter<CharSequence> fromAdapter  = ArrayAdapter.createFromResource(view.getContext(), R.array.from_array, android.R.layout.simple_spinner_dropdown_item);
		from = (Spinner)view.findViewById(R.id.spin_from);
		from.setAdapter(fromAdapter);
		ArrayAdapter<CharSequence> toAdapter  = ArrayAdapter.createFromResource(view.getContext(), R.array.to_array, android.R.layout.simple_spinner_dropdown_item);
		to = (Spinner)view.findViewById(R.id.spin_to);
		to.setAdapter(toAdapter);
		
		

		//TODO: load submit button from xml

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		this.activity.onSubmit();
	}
}

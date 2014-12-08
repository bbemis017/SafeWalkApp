package edu.purdue.bbemismarsh27;

import edu.purdue.bbemismarsh27.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * This fragment is the "page" where the user enter the server coordinates.
 *
 * @author CS180_TAs
 * @author Benjamin Bemis <bbemis@purdue.edu>
 * @author Lucas Marsh <marsh27@purdue.edu>
 */
public class ServerFragment extends Fragment {

	/**
	 * EditText where the user enter the host address.
	 */
	private EditText host;

	/**
	 * EditText where the user enter the port address.
	 */
	private EditText port;

	/**
	 * Creates a ServerFragment
	 * 
	 * @return the fragment initialized.
	 */
	public static ServerFragment newInstance() {
		ServerFragment f = new ServerFragment();

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

		View view = inflater.inflate(R.layout.server_fragment_layout,
				container, false);

		this.host = (EditText) view.findViewById(R.id.et_host);
		this.port = (EditText) view.findViewById(R.id.et_port);

		return view;
	}

	/**
	 * Returns the host enter by the user or the default value if the user
	 * didn't open the fragment.
	 */
	public String getHost(String defaultVal) {
		return this.host != null ? host.getText().toString() : defaultVal;
	}

	/**
	 * Returns the port enter by the user or the default value if the user
	 * didn't open the fragment.
	 */
	public int getPort(int defaultVal) {
		return this.port != null ? Integer.parseInt(port.getText().toString()) : defaultVal;
	}

}

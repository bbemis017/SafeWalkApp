package edu.purdue.bbemismarsh27;

import edu.purdue.bbemismarsh27.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author Benjamin Bemis <bbemis@purdue.edu>
 * @author Lucas Marsh <marsh27@purdue.edu>
 * 
 */
public class MainActivity extends Activity implements SubmitCallbackListener, StartOverCallbackListener {
	public TextView from, to, partner, connection, foundPair, clientInfo, match;
	/**
	 * The ClientFragment used by the activity.
	 */
	private ClientFragment clientFragment;

	/**
	 * The ServerFragment used by the activity.
	 */
	private ServerFragment serverFragment;

	/**
	 * UI component of the ActionBar used for navigation.
	 */
	private Button left;
	private Button right;
	private TextView title;

	/**
	 * Called once the activity is created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);

		this.clientFragment = ClientFragment.newInstance(this);
		this.serverFragment = ServerFragment.newInstance();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.fl_main, this.clientFragment);
		ft.commit();
	}

	/**
	 * Creates the ActionBar: - Inflates the layout - Extracts the components
	 */
	@SuppressLint("InflateParams")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

		// Set up the ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		// Extract the UI component.
		this.title = (TextView) actionBarLayout.findViewById(R.id.tv_title);
		this.left = (Button) actionBarLayout.findViewById(R.id.bu_left);
		this.right = (Button) actionBarLayout.findViewById(R.id.bu_right);
		this.right.setVisibility(View.INVISIBLE);

		return true;
	}

	/**
	 * Callback function called when the user click on the right button of the
	 * ActionBar.
	 * 
	 * @param v
	 */
	public void onRightClick(View v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(this.getResources().getString(R.string.client));
		this.left.setVisibility(View.VISIBLE);
		this.right.setVisibility(View.INVISIBLE);
		ft.replace(R.id.fl_main, this.clientFragment);
		ft.commit();
	}

	/**
	 * Callback function called when the user click on the left button of the
	 * ActionBar.
	 * 
	 * @param v
	 */
	public void onLeftClick(View v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(this.getResources().getString(R.string.server));
		this.left.setVisibility(View.INVISIBLE);
		this.right.setVisibility(View.VISIBLE);
		ft.replace(R.id.fl_main, this.serverFragment);
		ft.commit();

	}

	/**
	 * Callback function called when the user click on the submit button.
	 */
	@Override
	public void onSubmit() {
		// Get client info via client fragment
		ClientFragment cf = clientFragment;
		int id = cf.preferences.getCheckedRadioButtonId();
		int type = -1;
		switch (id) {
		case R.id.rb_requester:
			type = 1;
			break;
		case R.id.rb_volunteer:
			type = 2;
			break;
		case R.id.rb_noPreference:
			type = 0;
			break;
		default:
			type = -1;
			break;
		}
		Request request = new Request(cf.name.getText().toString(),
				Request.LOCATION[cf.from.getSelectedItemPosition()], Request.LOCATION[cf.to.getSelectedItemPosition()],
				type);

		// Server info
		String host = this.serverFragment.getHost(getResources().getString(R.string.default_host));

		int port = this.serverFragment.getPort(Integer.parseInt(getResources().getString(R.string.default_port)));
		//sanity check the results of the previous two dialogs
		request.isHostPortValid(host, port);
		if (request.isValid()) {
			// TODO: Need to get command from client fragment
			String command = this.getResources().getString(R.string.default_command);

			FragmentTransaction ft = getFragmentManager().beginTransaction();

			this.title.setText(getResources().getString(R.string.match));
			this.left.setVisibility(View.INVISIBLE);
			this.right.setVisibility(View.INVISIBLE);

			// TODO: You may want additional parameters here if you tailor
			// the match fragment
			MatchFragment frag = MatchFragment.newInstance(this, host, port, command, request);

			ft.replace(R.id.fl_main, frag);
			ft.commit();
		} else {
			// display AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(request.getAlerts());
			builder.setTitle("Form Invalid");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// closes dialog
					dialog.cancel();

				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	/**
	 * Callback function call from MatchFragment when the user want to create a
	 * new request.
	 */
	@Override
	public void onStartOver() {
		onRightClick(null);
	}

}

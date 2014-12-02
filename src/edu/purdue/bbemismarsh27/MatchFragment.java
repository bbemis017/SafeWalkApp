package edu.purdue.bbemismarsh27;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import edu.purdue.bbemismarsh27.R;
import android.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This fragment is the "page" where the application display the log from the
 * server and wait for a match.
 * 
 * @author YL
 * @author Benjamin Bemis
 */
public class MatchFragment extends Fragment implements OnClickListener {
	private TextView from, to, partner, connection, foundPair, clientInfo, serverMatch, matchFound;
	private static final String DEBUG_TAG = "DEBUG";

	/**
	 * Activity which have to receive callbacks.
	 */
	private StartOverCallbackListener activity;

	/**
	 * AsyncTask sending the request to the server.
	 */
	private Client client;

	/**
	 * Coordinate of the server.
	 */
	private String host;
	private int port;

	/**
	 * Command the user should send.
	 */
	private String command;

	private Request reqToSend, reqRecieved;

	// TODO: your own class fields here

	// Class methods
	/**
	 * Creates a MatchFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the start over
	 *            Button.
	 * @param host
	 *            address or IP address of the server.
	 * @param port
	 *            port number.
	 * 
	 * @param command
	 *            command you have to send to the server.
	 * @param reqToSend
	 *            request to send to the server
	 * 
	 * @return the fragment initialized.
	 */
	// TODO: you can add more parameters, follow the way we did it.
	// ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	public static MatchFragment newInstance(StartOverCallbackListener activity, String host, int port, String command,
			Request reqToSend) {
		MatchFragment f = new MatchFragment();

		f.activity = activity;
		f.host = host;
		f.port = port;
		f.command = command;
		f.reqToSend = reqToSend;

		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.match_fragment_layout, container, false);

		/**
		 * Register this fragment to be the OnClickListener for the startover
		 * button.
		 */
		view.findViewById(R.id.bu_start_over).setOnClickListener(this);

		connection = (TextView) view.findViewById(R.id.server_connection);
		clientInfo = (TextView) view.findViewById(R.id.client_info);
		matchFound = (TextView) view.findViewById(R.id.match_found);
		partner = (TextView) view.findViewById(R.id.server_partner);
		from = (TextView) view.findViewById(R.id.server_from);
		to = (TextView) view.findViewById(R.id.server_to);
		serverMatch = (TextView) view.findViewById(R.id.match_text);
		// import your Views from the layout here. See example in
		// ServerFragment.

		/**
		 * Launch the AsyncTask
		 */
		this.client = new Client();
		this.client.execute("");

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		/**
		 * Close the AsyncTask if still running.
		 */
		this.client.close();

		/**
		 * Notify the Activity.
		 */
		this.activity.onStartOver();
	}

	class Client extends AsyncTask<String, String, String> implements Closeable {

		/**
		 * NOTE: you can access MatchFragment field from this class:
		 * 
		 * Example: The statement in doInBackground will print the message in
		 * the Eclipse LogCat view.
		 */

		private Socket sock;
		private int publishes = 0;

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... params) {

			/**
			 * TODO: Your Client code here.
			 */
			Log.d(DEBUG_TAG, String.format("The Server at the address %s uses the port %d", host, port));
			try {
				// create sock and send request to server
				InetAddress serverAddr = InetAddress.getByName(host);

				Log.d(DEBUG_TAG, "getAddress");
				sock = new Socket(host, port);

				Log.d(DEBUG_TAG, "socket created");
				publishProgress("connection to the server. Success");
				PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
				pw.println(reqToSend.toString());
				publishProgress(reqToSend.toString());

				Log.d(DEBUG_TAG, "message sent");

				isCancelled();

				// wait for response and store that response as reqRecieved
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				reqRecieved = new Request(br.readLine());
				if (reqRecieved.isValid()) {
					// acknowledge match request
					pw.println(":ACK");
					Log.d(DEBUG_TAG, "aknowledge");
					publishProgress("a pair has been found by the server.");
				}
				
				isCancelled();

			} catch (UnknownHostException e) {
				Log.d("test","UNKOWNHOST;ASDKLSDFJLSDFK");
				publishes = -1;
				cancel(true);
				publishProgress("server is not available");
			} catch (IOException e) {
				publishes = -1;
				publishProgress("server is not available");
				cancel(true);
			} catch ( NullPointerException e){
				Log.d("NULLPOINTER", "" + e.getMessage());
			}
			

			Log.d(DEBUG_TAG, String.format("The Client will send the command: %s", command));

			return "";
		}

		public void close() {
			// TODO: Clean up the client
			if (sock != null && !sock.isClosed()) {
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			cancel(true);
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */

		// TODO: use the following method to update the UI.
		// ** DO NOT TRY TO CALL UI METHODS FROM doInBackground!!!!!!!!!! **

		/**
		 * Method executed just before the task.
		 */
		@Override
		protected void onPreExecute() {
			publishes = 0;
			connection.setText("");
			clientInfo.setText("");
			matchFound.setText("");
		}

		/**
		 * Method executed once the task is completed.
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO: set appropriate data to textviews
			partner.setText(reqRecieved.getName());
			from.setText(reqRecieved.getFrom());
			to.setText(reqRecieved.getTo());
			// changes visibility of objects
			partner.setVisibility(TextView.VISIBLE);
			from.setVisibility(TextView.VISIBLE);
			to.setVisibility(TextView.VISIBLE);
			matchFound.setVisibility(TextView.VISIBLE);
			serverMatch.setVisibility(TextView.VISIBLE);
		}

		/**
		 * Method executed when progressUpdate is called in the doInBackground
		 * function.
		 */
		@Override
		protected void onProgressUpdate(String... result) {
			String output = "";
			Calendar c = Calendar.getInstance();
			output += "[" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
			output += " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND)
					+ "] ";
			output += result[0];
			switch (publishes) {
			case -1:
				connection.setText(output);
				break;
			case 0:
				connection.setText(output);
				break;
			case 1:
				clientInfo.setText(output);
				break;
			case 2:
				matchFound.setText(output);
				break;
			}
			publishes++;
		}
	}

}

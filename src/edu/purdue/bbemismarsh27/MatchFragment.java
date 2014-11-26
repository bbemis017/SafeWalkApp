package edu.purdue.bbemismarsh27;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
	private TextView from, to, partner, connection, foundPair, clientInfo,
			serverMatch, matchFound;
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
	public static MatchFragment newInstance(StartOverCallbackListener activity,
			String host, int port, String command, Request reqToSend) {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.match_fragment_layout, container,
				false);

		/**
		 * Register this fragment to be the OnClickListener for the startover
		 * button.
		 */
		view.findViewById(R.id.bu_start_over).setOnClickListener(this);

		connection = (TextView)view.findViewById(R.id.server_connection);
		clientInfo = (TextView)view.findViewById(R.id.client_info);
		matchFound = (TextView)view.findViewById(R.id.match_found);
		partner = (TextView)view.findViewById(R.id.server_partner);
		from = (TextView)view.findViewById(R.id.server_from);
		to = (TextView)view.findViewById(R.id.server_to);
		serverMatch= (TextView)view.findViewById(R.id.match_text);
		//  import your Views from the layout here. See example in
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
		private static final int PORT = 5000;
		private static final String IP = "10.0.2.2";

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... params) {

			/**
			 * TODO: Your Client code here.
			 */
			Log.d(DEBUG_TAG, String
					.format("The Server at the address %s uses the port %d",
							host, port));
			try {
				// create sock and send request to server
				InetAddress serverAddr = InetAddress.getByName(host);
				Log.d(DEBUG_TAG,"getAddress");
				sock = new Socket(serverAddr, port);
				Log.d(DEBUG_TAG,"socket created");
				PrintWriter pw = new PrintWriter( sock.getOutputStream() );
				pw.println( reqToSend.toString() );
				pw.flush();
				
				Log.d(DEBUG_TAG,"message sent");
				
				// wait for response and store that response as reqRecieved
				BufferedReader br = new BufferedReader( new InputStreamReader( sock.getInputStream() ) );
				
				reqRecieved = new Request( br.readLine() );
				if ( reqRecieved.isValid()){
					// acknowledge match request
					pw.write(":ACK");
					Log.d(DEBUG_TAG, "aknowledge");
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			Log.d(DEBUG_TAG, String.format(
					"The Client will send the command: %s", command));

			return "";
		}

		public void close() {
			// TODO: Clean up the client
			if(!sock.isClosed()){
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
		}

		/**
		 * Method executed once the task is completed.
		 */
		@Override
		protected void onPostExecute(String result) {
		}

		/**
		 * Method executed when progressUpdate is called in the doInBackground
		 * function.
		 */
		@Override
		protected void onProgressUpdate(String... result) {
		}
	}

}

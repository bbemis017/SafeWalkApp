package edu.purdue.bbemismarsh27;

/**
 * This class checks the validity of a request and also provides a string to
 * send to the server
 * 
 * @author Benjamin Bemis
 * 
 */
public class Request {

	// All valid locations
	public static final String[] LOCATION = new String[] { "EE", "CL50", "LWSN", "PMU", "PUSH", "*" };

	private String name, from, to;
	private int type;
	private boolean valid;
	private String[] alerts;

	/**
	 * Creates new Request and determines if it is a valid request
	 * 
	 * @param name
	 * @param from
	 * @param to
	 * @param type
	 */
	public Request(String name, String from, String to, int type) {
		valid = true;
		alerts = new String[0];
		this.name = name;
		this.from = from;
		this.to = to;
		this.type = type;
		if (from.equals("*")){
			valid = false;
			addAlert("From must be a defined location");
		}

		if (to.equals(from)){
			valid = false;
			addAlert("To cannot be the same location as from");
		}

		if (!validLocation(from)){
			valid = false;
			addAlert("From is not a defined location");
		}

		if (!validLocation(to)){
			valid = false;
			addAlert("To is not a defined location");
		}

		if (type > 2 || type < 0){
			valid = false;
			addAlert("Must choose a Preference");
		}
		
		if ( to.equals("*") && type !=2	){
			valid = false;
			addAlert("You must be a volunteer to set undefined location");
		}
		
		if ( name.contains(",") ){
			valid = false;
			addAlert("Name cannot contain a comma");
		}
		
		if( name.length() == 0){
			valid = false;
			addAlert("Must enter a name");
		}
			
		//if a requester only wants to match with volunteers to location must be defined
		if ( type == 1 && to.equals("*") )
			valid = false;
		
	}

	/**
	 * 
	 * @return whether or not this is a valid request
	 */
	public boolean isValid() {
		return valid;
	}

	@Override
	public String toString() {
		return name + "," + from + "," + to + "," + type;
	}

	/**
	 * 
	 * @param loc
	 * @return - boolean whether or not the String argument is a valid location
	 */
	private boolean validLocation(String loc) {
		for (int i = 0; i < LOCATION.length; i++)
			if (loc.equalsIgnoreCase(LOCATION[i]))
				return true;
		return false;
	}
	
	/**
	 * 
	 * @return String - alerts corresponding to how form is invalid
	 */
	public String getAlerts(){ 
		String Alert = "";
		for ( int i = 0; i < alerts.length; i++)
			Alert += "\n*" + alerts[i];
		return Alert; 
		}
	
	/**
	 * Adds new Alert to Alert array
	 */
	 private void addAlert(String alert){
		 String[] temp = new String[alerts.length + 1];
		 int i = 0;
		 for ( ; i < alerts.length; i++)
			 temp[i] = alerts[i];
		 temp[i] = alert;
		 alerts = temp;
	 }
	 

}

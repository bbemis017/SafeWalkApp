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
	private static final String[] LOCATION = new String[] { "EE", "CL50", "LWSN", "PMU", "PUSH", "*" };

	private String name, from, to;
	private int type;
	private boolean valid;

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

		this.name = name;
		this.from = from;
		this.to = to;
		this.type = type;
		if (from.equalsIgnoreCase("*"))
			valid = false;

		if (to.equalsIgnoreCase(from))
			valid = false;

		if (!validLocation(from))
			valid = false;

		if (!validLocation(to))
			valid = false;

		if (type > 2 || type < 0)
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

}

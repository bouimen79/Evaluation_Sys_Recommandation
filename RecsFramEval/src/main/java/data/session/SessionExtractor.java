package data.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Event;

/**
 * Creates sessions from time-ordered transactions based on the user id and the
 * time between transactions.
 * 
 * @author Mozhgan
 *
 */
public class SessionExtractor {
	private static long thresholdInMS = 1000 * 60 * 10;
	//private static long SESSION_TIME_THRESHOLD = 1000 * 60 * 20;

	// the storage for the sessions
	public static Map<Long, List<List<Event>>> sessions = new HashMap<Long, List<List<Event>>>();

	/**
	 * Adds one click to the session storage and assigns it to the corresponding
	 * user session. This method needs to be called in order (time-wise).
	 * 
	 * @param t the current click
	 * @return the session
	 */
	public List<Event> addClick(Event t) {

		// anonymous users
		if (t.getId_user() <= 0) {
			// The user has no previous clicks or sessions
			// -> create a list of sessions with one session in it and add the
			// current click
			List<Event> session = new ArrayList<Event>();
			session.add(t);
			// we are done for this case. Do not continue.
			return session;
		} else {

			if (!sessions.containsKey(t.getId_user())) {
				// The user has no previous clicks or sessions
				// -> create a list of sessions with one session in it and add the
				// current click
				List<List<Event>> sessionList = new ArrayList<List<Event>>();
				sessions.put(t.getId_user(), sessionList);
				List<Event> session = new ArrayList<Event>();
				session.add(t);
				sessionList.add(session);
				// we are done for this case. Do not continue.
				return session;
			}
			// if the user had previous clicks, get the list of session and retrieve
			// the last session from it.
			List<List<Event>> sessionList = sessions.get(t.getId_user());
			List<Event> lastSession = sessionList.get(sessionList.size() - 1);
			if (t.getTime().getTime()
					- lastSession.get(lastSession.size() - 1).getTime().getTime() <= getThresholdInMS()) {
				// if the difference between the last click event of the last
				// session
				// and the current click is less than N milliseconds,
				// add it at the end of the current session
				lastSession.add(t);
				return lastSession;
			} else {
				// else, create a new session and add the click
				List<Event> session = new ArrayList<Event>();
				session.add(t);
				sessionList.add(session);
				return session;
			}

		}

	}

	/**
	 * Finds and returns the specific session in which this click occured
	 * 
	 * @param t the click
	 * @return the session for the click
	 */
	public List<Event> getSession(Event t) {
		// first, find the all sessions for this user
		List<List<Event>> sessionList = sessions.get(t.getId_user());
		// iterate over the user sessions and find the right click
		for (List<Event> list : sessionList) {
			for (Event transactionInList : list) {
				if (transactionInList == t) {
					// reference comparison should be fine, since we do not copy transactions
					return list;
				}
			}
		}
		// we did not find anything -> return null
		return null;
	}

	/**
	 * Returns a list of all sessions mapped by user ID
	 * 
	 * @return all sessions so far mapped by user ID
	 */
	public Map<Long, List<List<Event>>> getSessionMap() {
		return sessions;
	}

	/**
	 * Determines the threshold in milliseconds for session cohesion, i.e., the
	 * maximum allowed time between two clicks of one user to be considered part of
	 * one session.
	 * 
	 * @return the sessions threshold
	 */
	public static long getThresholdInMS() {
		return thresholdInMS;
	}

	/**
	 * Determines the threshold in milliseconds for session cohesion, i.e., the
	 * maximum allowed time between two clicks of one user to be considered part of
	 * one session.
	 * 
	 * @param thresholdInMS -
	 */
	public static void setThresholdInMS(long thresholdInMS) {
		SessionExtractor.thresholdInMS = thresholdInMS;
	}
}

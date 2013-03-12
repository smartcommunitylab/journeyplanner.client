package eu.trentorise.smartcampus.journeyplanner;

/**
 * Exception thrown by {@link JourneyPlannerConnector}
 *
 */
public class JourneyPlannerConnectorException extends Exception {

	private static final long serialVersionUID = -6682965816616260202L;

	public JourneyPlannerConnectorException() {
		super();
	}

	public JourneyPlannerConnectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public JourneyPlannerConnectorException(String message) {
		super(message);
	}

	public JourneyPlannerConnectorException(Throwable cause) {
		super(cause);
	}

}

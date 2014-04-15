package next.wildgoose.exception;

public class SearchQueryIllegalArgumentException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;
	
	public SearchQueryIllegalArgumentException (String message) {
		super (message);
	}
	
	public SearchQueryIllegalArgumentException (Throwable cause) {
		super (cause);
	}
	
	public SearchQueryIllegalArgumentException (String message, Throwable cause) {
		super (message, cause);
	}
}

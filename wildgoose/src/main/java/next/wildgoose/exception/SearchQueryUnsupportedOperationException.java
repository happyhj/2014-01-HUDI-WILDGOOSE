package next.wildgoose.exception;

public class SearchQueryUnsupportedOperationException extends UnsupportedOperationException {

	private static final long serialVersionUID = 1L;
	
	public SearchQueryUnsupportedOperationException (String message) {
		super (message);
	}
	
	public SearchQueryUnsupportedOperationException (Throwable cause) {
		super (cause);
	}

}

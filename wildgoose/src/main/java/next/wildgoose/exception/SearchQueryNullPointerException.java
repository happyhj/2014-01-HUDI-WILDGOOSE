package next.wildgoose.exception;

public class SearchQueryNullPointerException extends NullPointerException {

	private static final long serialVersionUID = 1L;
	
	public SearchQueryNullPointerException() {
		super ();
	}
	
	public SearchQueryNullPointerException (String message) {
		super (message);
	}


}

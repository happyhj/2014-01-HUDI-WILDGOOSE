package next.wildgoose.model;

public class VerifySearchQuery {
	
	static final String WHITESPACE = "whitespace";
	
	String searchQuery;
	String cause;
	WebError error;

	public VerifySearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}


	public boolean isWhitespase() {

		if (this.searchQuery.trim().length() == 0) {
			this.cause = VerifySearchQuery.WHITESPACE;
			return true;
		}
		return false;
	}

	public boolean isValid() {
		
		if (!this.isWhitespase()) {
			return true;
		}
		
		return false;
	}

	public String getCause() {
		
		if (!this.isValid() ) {
			return this.cause; 
		}
		return null;
	}

	public WebError getError() {
		
		if (!this.isValid()) {
			error = new WebError(this.cause);			
			return error;
		}
		
		return null;
	}
}

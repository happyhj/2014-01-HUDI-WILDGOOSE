package next.wildgoose.model;

import java.io.UnsupportedEncodingException;

import next.wildgoose.exception.SearchQueryIllegalArgumentException;
import next.wildgoose.exception.SearchQueryNullPointerException;
import next.wildgoose.exception.SearchQueryUnsupportedOperationException;
import next.wildgoose.utility.Utility;
import next.wildgoose.utility.Wildgoose;

public class SearchQuery {

	private String searchQuery;
	
	// constructor
	public SearchQuery(String searchQuery, Utility utility, String encodeType) throws SearchQueryNullPointerException, SearchQueryUnsupportedOperationException, SearchQueryIllegalArgumentException {
		
		try {
			this.searchQuery = utility.encode(searchQuery, encodeType);
			this.isValid();
		}
		catch (NullPointerException npe) {
			// log로 대체 필요함
			npe.printStackTrace();
			// getParameter에 대해서만 encoding이 필요한 작업이므로 GetParameterNullPointerException을 발생시켜 전달
			throw new SearchQueryNullPointerException ();
		}
		
		catch (UnsupportedEncodingException uee) {
			// log로 대체 필요함
			uee.printStackTrace();
			
			// getParameter의 encoding이 실패했을 때
			throw new SearchQueryUnsupportedOperationException (uee);
		}
		
		catch (IllegalArgumentException iae) {
			// log로 대체 필요함
			iae.printStackTrace();
			
			// searchQuery내용이 invalid할 때
			throw new SearchQueryIllegalArgumentException(iae);
		}
	}
	
	
	private void isValid() {
		// 입력문자가 없을 때
		if (this.isNone()) {
			throw new NullPointerException();
		}
		
		// 입력문자가 공맥일 때
		if (this.isWhitespase()) {
			throw new IllegalArgumentException(Wildgoose.WHITE_SPACE);
		}
		
		this.searchQuery = this.searchQuery.trim();
	}
	
	
	private boolean isNone() {
		
		if (this.searchQuery.equals("")) {
			return true;
		}
			
		return false;
	}

	private boolean isWhitespase() {
		
		if (this.searchQuery.trim().length() == 0) {
			return true;
		}
		return false;
	}


	@Override
	public String toString() {

		return this.searchQuery;
	}
	

}

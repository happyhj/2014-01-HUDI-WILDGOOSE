package next.wildgoose.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VerifySearchQueryTest {
	
	VerifySearchQuery searchQueryWS, searchQueryNotWS;
	WebError errorWS;
	
	@Before
    public void setUp() {
		searchQueryWS = new VerifySearchQuery(" ");
		searchQueryNotWS = new VerifySearchQuery("Not");
    }

	@Test
	public void testIsWhitespace() {
		
		assertEquals(true, searchQueryWS.isWhitespase());		
	}
	
	@Test
	public void testIsNotWhitespace() {
		
		assertEquals(false, searchQueryNotWS.isWhitespase());	
	}
	
	@Test
	public void testIsValidQuery() {
	
		assertEquals(true, searchQueryNotWS.isValid());
		assertEquals(false, searchQueryWS.isValid());
	}
	
	@Test
	public void testCauseUnvalid() {
		
		assertEquals(VerifySearchQuery.WHITESPACE, searchQueryWS.getCause());
		assertNull(null, searchQueryNotWS.getCause());
	}
	
	@Test
	public void testGetErrorOfWS() {
		WebError errorWS = searchQueryWS.getError();
		
		assertError(errorWS);
	}
	
	@Test
	public void testGetErrorOfNotWS() {
		WebError errorNotWS = searchQueryNotWS.getError();
		
		assertNull(errorNotWS);
	}

	private void assertError(WebError error) {
		
		assertEquals(VerifySearchQuery.WHITESPACE, error.getCause());
		assertEquals("검색할 수 없는 키워드 입니다.", error.getNotice());
		
		
	}

}

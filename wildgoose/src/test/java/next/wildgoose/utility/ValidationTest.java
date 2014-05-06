package next.wildgoose.utility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidationTest {

	
	@Test
	public void testURL() {
		
		// true
		assertTrue(Validation.isURL("http://www.hankyung.com/news/app/newsview.php?aid=2014040622521"));
		assertTrue(Validation.isURL("hTTp://wWw.hANkyung.cOM/news/app/newsview.php?aid=2014040622521"));
		assertTrue(Validation.isURL("www.hankyung.com/news/app/newsview.php?aid=2014040622521"));
		assertTrue(Validation.isURL("hankyung.com/news/app/newsview.php?aid=2014040622521"));
		assertTrue(Validation.isURL("hankyung.com/"));
		assertTrue(Validation.isURL("hankyung.com"));
		assertTrue(Validation.isURL("hankyung.co.kr"));
		/*
		 *  true ㅠㅠ
		 *  뒤 지역코드가 다를 경우 false를 반환도록 하고 싶지만, 규칙이 없어서 모든 코드를 다 넣어야 한다는 문제가 있었어요 
		 */
		assertTrue(Validation.isURL("hankyung.co"));
		assertTrue(Validation.isURL("hankyung.cok"));
		
		
		//false
		assertFalse(Validation.isURL("htp://www.hankyung.com/news/app/newsview.php?aid=2014040622521"));
		assertTrue(Validation.isURL("http://w.hankyung.com/news/app/newsview.php?aid=2014040622521"));		
	}
	
	@Test
	public void testEmail() {
		//true
		assertTrue(Validation.isEmail("cocagola@gmail.com"));
		//false
		assertFalse(Validation.isEmail("cocagola_gmail.com"));
	}
	

}

package next.wildgoose.web;

import java.util.ArrayList;
import java.util.List;

import next.wildgoose.model.ArticleCardData;
import next.wildgoose.model.ReporterCardData;

public class TestDatas {
	
	ReporterCardData getReporter() {	
		ReporterCardData reporter = new ReporterCardData();
		
		reporter.setEmail("fourwingsy@nhnnext.org");
		reporter.setPressName("Nextian");
		reporter.setName("양현석");
		
		return reporter;		
	}

	List<ArticleCardData> getArticles() {
		List<ArticleCardData> articles = new ArrayList<ArticleCardData>();
		
		ArticleCardData article1 = new ArticleCardData();
		article1.setUrl("http://www.hankyung.com/news/app/newsview.php?aid=2014040928348");
		article1.setTitle("野 기초공천 투표·여론조사 종료…10일 오전 결과 발표");
//		article1.setSection_id(1);
		article1.setContent("새정치민주연합이 9일 기초선거 정당공천 여부를 묻는 전(全)당원투표와 여론조사를 실시했다. 이석현 의원을 위원장으로 하는 관리위원회는 이날 오전 회의에서 논란");
		article1.setDatetime("2014-04-09 13:13:16");
		articles.add(article1);
		
		ArticleCardData article2 = new ArticleCardData();
		article2.setUrl("http://www.hankyung.com/news/app/newsview.php?aid=2014040803161");
		article2.setTitle("기通찬 원기찬…부르면 달려간다");
//		article2.setSection_id(2);
		article2.setContent("지난해 말 취임한 원기찬 삼성카드 사장(사진)의 ‘면대면 소통법’이 화제다. 삼성전자를 글로벌 기업으로 성장시킨 ‘수평적 문화’를 삼성카드에 이식하고 있다는 평가다. ");
		article2.setDatetime("2014-04-08 21:12:04");
		articles.add(article2);
		
		ArticleCardData article3 = new ArticleCardData();
		article3.setUrl("http://www.hankyung.com/news/app/newsview.php?aid=2014040943991");
		article3.setTitle("산업단지에 규제 자유지역 만들자");
//		article3.setSection_id(1);
		article3.setContent("대한상공회의소가 9일 주최한 경제혁신 토론회에서 나온 아이디어들이다. 서울 남대문로 대한상의회관에서 열린 행사에는 추경호 기획재정부 1차관을 비롯한 정부 관료, 기");
		article3.setDatetime("2014-04-09 20:34:49");
		articles.add(article3);
		
		ArticleCardData article4 = new ArticleCardData();
		article4.setUrl("http://www.hankyung.com/news/app/newsview.php?aid=2014040801331");
		article4.setTitle("상품만 팔면 끝?…기업 존재 이유에 물음표 던진 CEO");
//		article4.setSection_id(2);
		article4.setContent("지난해 12월 서울 소공로 신세계백화점 본점 10층 대강당. 정용진 신세계그룹 부회장은 계열사 신임 임원들을 대상으로 한 강연에서 ‘삶의 의미’를 물었다. 신임 임원들");
		article4.setDatetime("2014-04-08 21:46:01");
		articles.add(article4);
		
		ArticleCardData article5 = new ArticleCardData();
		article5.setUrl("http://www.hankyung.com/news/app/newsview.php?aid=2014040802271");
		article5.setTitle("황우석, 홈캐스트와 손잡고 증시 입성한다");
//		article5.setSection_id(3);
		article5.setContent("황우석 전 서울대 교수(62·사진)가 코스닥 상장사인 홈캐스트 지분 5.7%를 확보할 전망이다. 자신이 대표이사를 맡고 있는 비상장사 에이치바이온을 통해서다. 업계에선");
		article5.setDatetime("2014-04-08 21:35:15");
		articles.add(article5);		
		
		return articles;
	}

}

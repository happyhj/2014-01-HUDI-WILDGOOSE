#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import re
from bs4 import BeautifulSoup
	
def get_article_urls_with_pagenum(page) :
	# print "PAGE NUMBER : " + str(page)
	LIST_URL = "http://news.donga.com/List?p="
	number_of_article_per_page = 16
	article_list_URL = LIST_URL + str( 1 + number_of_article_per_page * ( page - 1 ) )	
	# 동아일보 기사만
	article_list_URL += "&m=NP"

	# 파싱할 개별 기사의 URL들을 확보하기
	article_URLs = get_article_URLs(article_list_URL)	

	'''
	article_urls = []
	# 각 URL을 파싱해서 기사 객체를 가져와 리스트에 추가하기 
	for article_URL in article_URLs :
		article = {}
		article = parse_article_with_url(article_URL)
		articles.append(article)
	for article in articles :
		article["press"] = pressName
	'''
	return article_URLs

def get_article_URLs(article_list_URL) :
	url_opener = urllib.urlopen(article_list_URL)
	url_opener.addheaders = [("User-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.74.9 (KHTML, like Gecko) Version/7.0.2 Safari/537.74.9")]
	html_doc = url_opener.read()
	url_opener.close()

	soup = BeautifulSoup(html_doc)
	news_list = soup.find_all("div", "articleList")
	URLs = []
	for news in news_list :
		url = news.find("div","rightList").p.a['href'].strip()
		URLs.append(url)
	
	return URLs

def parse_article_with_url(URL): 
	html_doc = urllib.urlopen(URL).read()	
	article = {}
#	article['press'] = "donga"
	article['URL'] = URL.encode('utf-8')
	article["title"] = _extract_title(html_doc)
	if article["title"] is None:
		return None
	article["datetime"] = _extract_datetime(html_doc)
	#article["modified"] = article_soup.find("p","title_foot").find("span","date2").contents[0].strip()
	# SECTION 검출
	article["section"] = _extract_section(html_doc)						
	article["content"] = _extract_contents(html_doc)
	article["author_info"] = _extract_author_info(html_doc)
	# article["press_id"] = 3
	# article["is_email_exist"] = _extract_email_existance(article["author_info"])

	# 기자이름을 발견하지 못할 시 관련 정보 콘솔에 출력
	if(article["author_info"] == "") :
		_no_author_info_error(article)   
	'''
	print "기사 읽는 중 ... " + article["author_info"]
	print u"--제목--- "+ article["title"]		
	print u"--주소--- "+ article["URL"]
	print u"--기자--- "+ article["author_info"]
	'''
	return article

def _no_author_info_error(article) :
	dead = article["title"].find("[부고]")
	job = article["title"].find("[인사]")
	channel_a = article["title"].find("[오늘의 채널A]")
	hot_donga = article["title"].find("[화제의 동아닷컴]")
	channel_a_news = article["content"].find("채널A 뉴스")
	channel_a_news2 = article["content"].find("채널A뉴스")
	board = article["content"].find("게시판")
	woman = article["content"].find("[우먼 동아일보]")	  
	if( dead == -1 & job == -1 & channel_a == -1 & hot_donga == -1 & channel_a_news == -1 & channel_a_news2 == -1 & board == -1 & woman == -1):
		text_file = open("Author_not_found.txt", "a")
		text_file.write("\n\n------!!!!!------- 기자를 찾지못한 기사입니다 ------!!!!!------")
		text_file.write("\n--제목--" + article["title"])
		text_file.write("\n--주소--" + article["URL"])
		text_file.write("\n--내용--" + article["content"])
		text_file.close()
		'''
		print "------!!!!!------- 기자를 찾지못한 기사입니다 ------!!!!!------"
		print "--내용--- "+ article["content"]	
		print "--제목--- "+ article["title"]		
		print "--주소--- "+ article["URL"]
		print "------!!!!!------- ----- ----- ----- ----- --- ------!!!!!------" 
		'''
def _extract_email_existance(author_info):
	email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"
	emails = re.findall(email_pattern, author_info)
	return len(emails)

def _extract_section(html_doc) :
	article_soup = BeautifulSoup(html_doc)
	article_section = article_soup.find("p","location")
	if( len(article_section.strong.contents) > 0) :
		section = article_section.contents[0] + article_section.strong.contents[0].strip()
		#print("*** SECTION *** " + article_section.contents[0] + article_section.strong.contents[0] )
	else :
		section = article_section.contents[0].strip()		
		#print("*** SECTION *** " + article_section.contents[0] )	
	section = section[5:]
	# section = section.split(u' > ', 1 )[0];
	return section.encode('utf-8')

def _extract_title(html_doc) :
	article_soup = BeautifulSoup(html_doc)
	if article_soup.find("div","article_title").h1 is None:
		return None
	title = article_soup.find("div","article_title").h1.contents[0].strip()
	return title.encode('utf-8')

def _extract_datetime(html_doc) :
	article_soup = BeautifulSoup(html_doc)
	datetime = article_soup.find("p","title_foot").find("span","date").contents[0].strip()
	return datetime

def _extract_contents(html_doc) :
	article_soup = BeautifulSoup(html_doc)
	# 본문 내용에 해당하는 텍스트들의 리스트 확보. 관련기사 항목. 사진박스는 삭제
	article_relation = article_soup.find("div","article_relation")
	if(article_relation is not None):
		article_relation.clear()
	art_Rel = article_soup.find("div","art_Rel")
	if(art_Rel is not None):
		art_Rel.clear()
		
	article_photos = article_soup.findAll("div","articlePhotoC")
	if(article_photos is not None):
		for article_photo in article_photos:
			article_photo.clear()		
	# 컨텐츠 텍스트 리스트를 모두 합친 내용 문자열을 만듬. idx_of_author_line 이후의 내용들은 대상에서 제외		
	content_text_element_list = article_soup.find("div","article_txt").findAll(text=True)
	
	text_total = ""
	
	for text_element_item in content_text_element_list :	
		text_total += text_element_item + "\n "	
							
	content = text_total.strip()	
	return content.encode('utf-8')

def _extract_author_info(html_doc) :
	article_soup = BeautifulSoup(html_doc)
	# 본문 내용에 해당하는 텍스트들의 리스트 확보. 관련기사 항목. 사진박스는 삭제
	article_relation = article_soup.find("div","article_relation")
	if(article_relation is not None):
		article_relation.clear()
	art_Rel = article_soup.find("div","art_Rel")
	if(art_Rel is not None):
		art_Rel.clear()
		
	article_photos = article_soup.findAll("div","articlePhotoC")
	if(article_photos is not None):
		for article_photo in article_photos:
			article_photo.clear()		
		
	content_text_element_list = article_soup.find("div","article_txt").findAll(text=True)
	
	# 기사 작성자를 나타내는 줄이라고 유추할 수 있는 키워드를 마지막 줄 부터 찾아감.
	# 존재한다면 그 줄의 내용을 작성자 이름으로 사용
	writter_found = 0
	idx_of_author_line = -1
	
	# 뒤에서 부터 찾기위해 역순의 컨텐츠 배열을 만들어 사용
	author = ""
	#article["author_keyword"] = None
	content_text_element_list_r = content_text_element_list[::-1]
	for idx, text_element_item in enumerate(content_text_element_list_r):
		author_keyword = __is_name_occured(text_element_item)
		if(author_keyword is not None) :
			idx_of_author_line = idx
			writter_found = 1
			#article["author_keyword"] = author_keyword
			author = text_element_item.strip()		
			break    
	return author.encode('utf-8')

def __is_name_occured(line_text) :

	keyword_array = [u'디지털뉴스팀',u'영상뉴스팀',u'도깨비뉴스',u'동아오토', u'[스포츠동아]', u'.[스포츠동아]', u' 논설위원 ', u' 특파원 ', u' 인턴기자 ', u' 전문기자 ', u' 동아닷컴 기자 ',u' 통신원 ',u'스포츠동아 해설위원', u' 객원논설위원', u'=뉴스1)', u' 뉴스1', u'=뉴시스】', u'뉴시스',u'=신화/뉴시스】', u'신화 뉴시스', u'. 뉴시스',u'우한=AP', u' 뉴시스',u'로스앤젤레스=',u'진주=', u'[뉴스엔]', u'StartFragment', u'남양주=', u'기앙쿠르=', u'천안=',u'도쿄=',u'뉴욕=',u'부산=', u'파리=', u'창원｜', u'잠실｜',u'세종=',u'부리람(태국)｜',u'상주｜', u'마산｜', u'대구｜', u'창원｜',u'하이커우(중국 하이난성)｜', u' 뉴욕=', u'예산=', u'김천=', u'천안=', u'하이커우=' ,u'서귀포=', u'경주=', u'청주=', u'아산=',u'베이징=',u'구례=', u'옥천·영동=',u'음성=',u'광주=', u'안성=',u'포항=', u'고창=',u'.베이징=' ,u'워싱턴=', u'.워싱턴=', u'=AP', u'제네바=' u'창원=',u' 창원=', u'하이코우=', u'/ IT동',u'·정책사회부', u'[엔터테인먼트부]', u' 에세이스트',u'소치=', u'경기 화성=', u'특약=', u'<본 자료는 해당기관에서 제공한 보도 자료입니다.>', u'원성열', u'담당·', u'@donga.co', u' 번역가', u'뉴스1', u'홈플러스 제공', u'박상준 기자', u'전영희 기자', u'제네바=정진수 동아닷컴 기자',u'주영로 기자',u'부산｜', u'sereno@donga.com', u'<동아닷컴>', u'동아바둑', u'윤양섭 전문기자', u'사진·문형일 기자', u' 기자 ', u' 동아닷컴 ',u'로이터·동아닷컴 특약']
	for idx, keyword in enumerate(keyword_array):
		keyword_index = line_text.find(keyword)
		equal_index = line_text.find(u'=')	
		if((keyword_index != -1) & (line_text.replace(keyword,"").find(u'=') == -1)) :
			return keyword
	return None

def insertArticleToDB(articles):
	import mysql.connector
	from mysql.connector import errorcode

	#articles = []

	config = {
	  'user': 'root',
#	  'host': '127.0.0.1',
	  'host': '10.73.45.134',
	  'database': 'Wildgoose',
	  'password': 'wildgoose',
	  'raise_on_warnings': True,
	}
	
	try:
		cnx = mysql.connector.connect(**config)
		cursor = cnx.cursor()
		for article in articles:
			query = ("SELECT * FROM Articles "
					"WHERE URL = %(URL)s")
			cursor.execute(query, article)
			one_row = cursor.fetchone()
			
			if( one_row is None ): 	
				add_article = ("INSERT INTO Articles "
#							   "(URL, press, title, section, content, author, datetime) "
#							   "VALUES (%(URL)s, %(press)s, %(title)s, %(section)s, %(content)s, %(author)s, %(datetime)s)") 			
							   "(URL, title, section, content, author, datetime) "
							   "VALUES (%(URL)s, %(title)s, %(section)s, %(content)s, %(author)s, %(datetime)s)") 			
				cursor.execute(add_article, article)
				
				'''
				if article['author_keyword'] is not None:
					add_keyword = ("INSERT INTO author_keyword "
									   "(keyword) "
									   "VALUES (%(author_keyword)s)") 			
					cursor.execute(add_keyword, article)
				'''
				print u"<< DB에 추가함 << " + article["datetime"] +" << "+ article["title"]	
				cnx.commit()	
	
			else:
				#duplicated_article_count += 1
				print u"<< 이미 입력됨 << " + article["datetime"] +" << "+ article["title"]	
						
		cursor.close()
		
	except mysql.connector.Error as err:
		if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
			print("Something is wrong with your user name or password")
		elif err.errno == errorcode.ER_BAD_DB_ERROR:
			print("Database does not exists")
		else:
			print(err)
	else:
		cnx.close()
	
	return

def main(pagenum) :
	catchingUpMode = False
	#parseArticle('http://news.donga.com/List/3/all/20140306/61474405/1')
	# 4788 페이지 정도가 마지막 페이지이다.
	url_list = []
	articles = []

	'''
	for i in range(start_page_index,end_page_index):
		duplicated_article_count = parsePageToPage(i,i+1)
		if(duplicated_article_count> 5 & catchingUpMode == True):
			print "이미 긁은 가장 마지막 페이지 까지 도달했습니다. 크롤링을 마칩니다	'''
	counter = 0
	# 대상 기사 URL을 모두 가져와 배열변수에 모으기
	url_list += get_article_urls_with_pagenum(pagenum)
	# 기사 URL 들을 통해 기사정보 원소의 배열을 만든다.
	for url in url_list:
		article = parse_article_with_url(url)
		if article is not None :
			articles.append(article)
	return articles
'''
import sys
start_page_index = int(sys.argv[1])
end_page_index = int(sys.argv[2])

for i in range(start_page_index,end_page_index):
	articles = main(i)
	insertArticleToDB(articles)
	articles = []
'''
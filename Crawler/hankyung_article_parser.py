#!/usr/bin/pythond
# -*- coding: utf-8 -*-

###
#
#
# 1차 구현
# author를 뽑아낼 때 contents를 기준으로 아래 4문장까지 확인하여 의미있는 author 정보를 출력
#
# 1단계 리팩토링: 완성
# 프로젝트 통합 github를 사용하여 파일명을 hankyung.py로 변경
# 팀의 통합 interface에 맞추어 구현 
#
# 2단계 리팩토링: 완성
# 함수적 종속관계 및 전역변수 사용을 최소화
# 함수별 로직을 효율적으로 구현
#
# 3단계 리팩토링: 예정
# main.py를 만들어 각 신문사별 crawler를 통합관리 및 실행할 수 있는 프로그램 제작필요
#
# class단위로 parser를 만들어 각 신문사별 output이 다른 함수만 overriding하여 사용
# contents를 real paragraph 단위로 구분하여 문장과 비문장을 구분하는 로직 구현
# 마지막 4문장에서 어떤 author정보도 찾지 못한 경우 문장의 첫부분을 재확인하는 로직 구현
# 
#
###

import urllib
import re
import os, sys
#from time import sleep
from bs4 import BeautifulSoup

'''
## 아래 코드는 상위 폴더에 있는 python file을 import하기 위한 코드 입니다.
## 인터넷에서 그대로 복사했습니다
#first change the cwd to the script path
scriptPath = os.path.realpath(os.path.dirname(sys.argv[0]))
os.chdir(scriptPath)
#append the relative location you want to import from
sys.path.append("../utility")
from newsSQL import NewsSQL
'''

# _reporter_pattern_in_first_line = u"[가-힝|\s|\w]*"

### A번
### url에 담긴 html정보를 읽고 반환
def _get_html_doc (url) :
	u = urllib.urlopen(url)
	html_doc = u.read()
	u.close()

	return html_doc


### B번
def _get_soup_in_container (html_doc, container) :

	### html_doc을 외부 라이브러리인 Beautiful soup를 이용하여 변환
	soup = BeautifulSoup(html_doc)

	### soup 중 id가 container 변수에 담긴 내용을 저장
	soup_in_container = soup.find(id=container)

	### 저장된 내용을 반환
	return soup_in_container


### 9번
### 
def _select_procedure_with_bs (dom) :
	# dom_name = ["br", "iframe"]
	#exit condition
	if dom.name == "div" :
		# sns구조 전까지 검색 후 종료
		if dom["class"][0] == "sns_article" :
			return "break"

		#advertisement
		if dom["class"][0] == "articleAD_L" :
			return "continue"

	#ignore
	if dom.name == "br" or dom.name == "iframe" :
		return "continue"
	if dom.string is None or dom.string == "\n" or dom.string == " " :
		return "continue"
	if dom.string.find ( u"서브 제목" ) != -1 or dom.string.find ( u"//서브 제목" ) != -1 :
		return "continue"
	return "progress"


### 8번
### html_doc을 입력받으면 필요없는 문단을 제외시킨 결과물을 반환
def _filter_out_useless_paragraphs(html_doc) :

	### 문단을 담을 list를 생성
	paragraphs = []

	### html_doc에서 id가 contents인 부분만 추출하여 beautiful soup 객체로 반환
	### B번으로 이동
	contents = _get_soup_in_container(html_doc, "contents")

	### contents에서 id가 newsView인 부분을 찾아 news_view에 저장
	news_view = contents.find(id="newsView")


	### news_view.contents의 dom을 순회
	# 분류한다.
	for dom in news_view.contents :

		### 해당 dom을 paragraphs list에 추가할지 말지를 결정
		### 9번으로 이동
		procedure_state = _select_procedure_with_bs(dom)
		if procedure_state == "break" : break
		elif procedure_state == "continue" : continue

		### 만약 위 문단의 if문을 실행시키지 않았다면 paragraphs list에 dom의 string을 추가
		paragraphs.append(dom.string)
			

	return paragraphs

def _extract_emails (paragraph) :
	email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"
	
	emails = re.findall(email_pattern, paragraph)
	return emails

def _has_period(paragraph):
	paragraph = paragraph.strip()
	if paragraph[len(paragraph)-1 ] == "." :
		return True
	return False

def _has_meaningful_email (paragraph, author_code) :
	if author_code == "special":
		return False

	####################################################
	public_emails = [ u"newsinfo@hankyung.com", u"open@hankyung.com" ]
	emails = {"public":public_emails}
	####################################################

	#문단에서 email추출
	emails_in_paragraph = _extract_emails(paragraph)

	for email in emails_in_paragraph :
		#public인 경우 public_emails가 있는 경우 true
		if author_code == "public" :
			if email in emails["public"] :
				return email
		#private인 경우 public_emails가 없는 경우 true
		elif author_code == "private" :
			if not email in emails["public"] :
				return email

	return False

def _has_meaningful_word (paragraph, author_code) :

	####################################################
	private_keywords = [ u"기자", u"특파원", u"통신원", u"위원" ]
	public_keywords = [ u"한경닷컴", u"뉴스팀", u"제보", u"뉴스", u"연구소" ]
	special_keywords = [ u"원장", u"회장", u"위원" ]
	# keyword
	keywords = { "private" : private_keywords, "public" : public_keywords, "special" : special_keywords }
	####################################################

	for word in keywords[author_code] :
		index = paragraph.rfind(word)
		if index != -1 :
			return word
	return False

def _find_meaningful_paragraph (paragraph) :
	paragraph_status = {"public":{"email":"", "keyword":""}, "private":{"email":"", "keyword":""}, "special":{"email":"", "keyword":""}}
	author_codes = ["public", "private", "special"]
	
	for author_code in author_codes :
		email = _has_meaningful_email(paragraph, author_code)
		paragraph_status[author_code]["email"] = email

		keyword = _has_meaningful_word(paragraph, author_code)
		paragraph_status[author_code]["keyword"] = keyword

	return paragraph_status



def _select_procedure_for_contents(i, paragraph) :
	paragraph_num_for_exit = 4
	# 종료조건
	if i >= paragraph_num_for_exit :
		return "break"

	complete_paragraph = _has_period(paragraph)
	if i == 0 and complete_paragraph :
		return "break"

def _select_author_code(paragraph_status) :

	if paragraph_status["private"]["email"] or paragraph_status["private"]["keyword"] :
		return "private"

	if paragraph_status["public"]["email"] or paragraph_status["public"]["keyword"] :
		if paragraph_status["special"]["email"] or paragraph_status["special"]["keyword"] :
			return "special"
		return "public"

	return "anonymous"


def _combine_meaningful_paragraphs(paragraphs_divided_by_author_code):

	public = paragraphs_divided_by_author_code["public"]
	private = paragraphs_divided_by_author_code["private"]
	special	= paragraphs_divided_by_author_code["special"]

	if len(private) > 0 :
		return " ".join(reversed(private)).strip()

	if len(special) > 0 :
		return " ".join(reversed(special)).strip()

	if len(public) > 0 :
		return " ".join(reversed(public)).strip()

	return "anonymous"



### 3번
### html_doc을 받아 author 정보를 반환
def extract_author (html_doc) :

	### html_doc에서 필요없는 문단을 제외시킨 결과를 contents에 저장
	### 8번으로 이동
	contents = _filter_out_useless_paragraphs(html_doc)
	contents_num = len(contents)
	paragraphs_divided_by_author_code = {"public":[], "private":[], "special":[]}
	
	# 뒷 문단부터 확인
	for i, paragraph in enumerate(reversed(contents)) :
		# 종료조건
		procedure_state = _select_procedure_for_contents(i, paragraph)
		if procedure_state == "break" : break
		elif procedure_state == "continue" : continue
		
		# paragraph(현재문단)가 author를 구별하는데 의미있는 email과 keyword가 있는지 찾음
		paragraph_status = _find_meaningful_paragraph(paragraph)
		# 확인된 data가 담긴 paragraph_status를 바탕으로 paragraph가 어떤 author_code를 갖는지 확인
		author_code = _select_author_code(paragraph_status)
		# 확인된 author_code에 paragraph를 차례대로 추가

		if author_code and author_code != "anonymous" :
			paragraphs_divided_by_author_code[author_code].append(paragraph)

	return _combine_meaningful_paragraphs(paragraphs_divided_by_author_code).encode('utf-8')



def extract_contents (html_doc) :
	contents = _filter_out_useless_paragraphs(html_doc)
	return "".join(contents).strip().encode('utf-8')


### 1번
### 해당 url이 가진 기사 url정보를 list형으로 반환
def get_article_urls_with_pagenum (page) :
	# hankyung_latest_list_url_with_page = _hankyung_latest_list_url+str(page)
	_hankyung_list_url = "http://www.hankyung.com/news/app/newslist_all.php?tab=&iscts=&popup=1&sdate=&page="
	hankyung_latest_list_url_with_page = _hankyung_list_url+str(page)

	### url의 html 문서를 가져와 html_doc에 저장
	### A번으로 이동
	html_doc = _get_html_doc(hankyung_latest_list_url_with_page)

	### html_doc 중에서 id가 newslist_ty1인 정보만 Beautiful soup로 변환하여 article_list에 저장
	### B번으로 이동
	article_list = _get_soup_in_container(html_doc, "newslist_ty1")

	### article_list에서 tag가 h3인 부분만 찾아 tags에 저장
	tags = article_list.findAll("h3")

	### urls에 기사의 url정보를 담을 list를 생성
	urls = []

	### tags를 순회
	for tag in tags :
		### 개별 h3 tag 내에서 a tag 중 href 속성만 찾아 url에 저장
		url = tag.a["href"]
		### 확인을 위해 url 출력
		print url
		### url을 urls에 추가
		urls.append(url)

	### urls list 반환
	return urls

def get_press_id():
	return "hankyung"

### 2번
### url을 인자로 받아 article이라는 객체를 생성하고 반환
def parse_article_with_url (url) :
	# try :

	### 해당 url의 html 문서를 받아 html_doc에 저장
	html_doc = _get_html_doc(url)

	### article에 새로운 dict를 생성
	article = {}
	article["press_id"] = 4

	### article["url"]에 url 정보 저장
	article["url"] = url.encode('utf-8')

	### article["author"]에 추출된 author 값을 저장
	### 3번으로 이동
	article["author_info"] = extract_author(html_doc)

	### article["section"]에 추출된 section 값을 저장
	### 4번으로 이동
	article["section"] = extract_section(html_doc)

	### article["title"]에 추출된 title 값을 저장
	### 5번으로 이동
	article["title"] = extract_title(html_doc)

	### article["datetime"]에 추천된 datetime 값을 저장
	### 6번으로 이동
	article["datetime"] = extract_datetime(html_doc)

	### article["contents"]에 추출된 contents 값을 저장
	### 7번으로 이동
	article["content"] = extract_contents(html_doc)

	### article 객체를 반환
	return article

	# except :
	# 	return False
	

def extract_section (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")
	section = contents.find("span",{"class":"cate"} ).a.string.strip()

	return section.encode('utf-8')

def extract_title (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")

	title = contents.h1.find (text=True, recursive=False)
	status = contents.h1.span
	if status :
		status = status.string
	title = status.strip()+title.strip()

	return title.encode('utf-8')


def extract_datetime (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")
	date_pattern = "^\d{4}-\d{2}-\d{2}\s?\d{2}:\d{2}"

	date_list = contents.find("dl",{"class":"modify_date"}).findAll("dd")	
	date = re.match(date_pattern, date_list[0].string)

	return date.group()
	
'''

# main
### main code를 실행합니다.
print "\n\nstart"
# _hankyung_latest_list_url = "http://www.hankyung.com/news/app/newslist_all.php?tab=&iscts=&popup=1&sdate=&page=1"
### 한경의 최신기사를 보여주는 list url입니다.
_hankyung_list_url = "http://www.hankyung.com/news/app/newslist_all.php?tab=&iscts=&popup=1&sdate=&page="

### DB를 연결하기 때문에 오류발생여부를 확인하기 위해 예외처리를 합니다.
try :
	### DB연결 정보
	config = { "host":"10.73.45.134", "user":"root", "password":"wildgoose", "database":"Wildgoose"}

	### SQL에 연결 후 연결객체를 conn에 저장
	conn = NewsSQL(config)

	### Articles 테이블에 데이터를 입력하는 query
	query = "INSERT INTO Articles ( URL, title, section, content, author, datetime ) VALUES ( %(url)s, %(title)s, %(section)s, %(contents)s, %(author)s, %(datetime)s )"

	### url을 담을 수 있는 빈 list 생성
	url_list = []

	### 1 ~ 2 page의 url을 읽어오도록 for문으로 2번 순환
	for page in range(1,3) :
		### get_article_urls_with_pagenum(page)를 호출 후 결과물을 url_list에 저장
		### 1번으로 이동
		url_list.extend(get_article_urls_with_pagenum(page))

	### 파싱된 article 객체를 담기 위한 list 생성
	articles = []

	### url_list를 순회하며 개별 url을 추출
	for url in url_list :
		### url을 인자로 전달하면 파싱된 결과를 dict 객체로 전달받아 article에 저장
		### 2번으로 이동
		article = parse_article_with_url(url)
		articles.append(article)

	for article in articles :
		conn.insert(query, article)
		print "url: " + article["url"]
		print "section: " + article["section"].encode("utf-8")
		print "title: " + article["title"].encode("utf-8")
		print "datetime: " + article["datetime"]
		print "contents: " + article["contents"].encode("utf-8")
		print "------------------------\n"

except :
	print "error"

else :
	try :
		conn.close()
	except :
		print "close error"

print "\n\nEnd"
'''




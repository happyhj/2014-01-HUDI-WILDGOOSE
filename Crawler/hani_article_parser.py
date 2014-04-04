#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import re
from bs4 import BeautifulSoup

def get_article_urls_with_pagenum(page):
	URLs = [];
	PRE_URL = "http://www.hani.co.kr/arti/?type=0&cline="
	i = 1

	URL = PRE_URL + str(page*10-1)

	#read URL and use beautifulsoup
	html_doc = urllib.urlopen(URL).read()
	soup = BeautifulSoup(html_doc)

	news_list = soup.find("div", "sorting-result-section").ul

	for news in news_list : #news_list is 'ul'
		if(i % 2 == 0):
			URL = "http://www.hani.co.kr"+news.dl.dt.a['href'] #URL
			if(URL.find("english_edition")== -1):
				try :
					URLs.append(URL)
				except :
					print "----URL parsing error: " + URL
		i = i + 1;
	return URLs

def parse_article_with_url(url):
	article = dict()
	html_doc = urllib.urlopen(url).read() # 간단한 get요청
	
	article["press_id"] = 4
	article['URL'] = url
	article['section'] = _extract_section(html_doc) # section 추출 차후에 특정 섹션(만화 등)은 파싱하지 않도록 조치 필요
	article['title'] = _extract_title(html_doc)
	article['datetime'] = _extract_datetime(html_doc) +':00' #초단위가 표시되지 않아 00초를 붙여줘서 형식 통일
	article['content'] = _extract_content(html_doc)
	try :
		article['author_info'] = _extract_author_info(html_doc)
		article['is_email_exist'] = _extract_email_existance(article['author_info'])
		
		# print article['content']
		# print article['URL']
		# print article['author_info']
		# print article['is_email_exist']
	except :
		print "error : " + article['URL']
	return article

def _extract_section(html_doc):
	soup = BeautifulSoup(html_doc)
	section = soup.find("div","article-category-title").table.tr.h3.img['alt']
	return section.encode("utf-8")

def _extract_title(html_doc):
	soup = BeautifulSoup(html_doc)
	title = soup.find("div", "article-category-title").table.tr.td.next_sibling.next_sibling.h3.findAll(text=True)[0]
	return title.encode("utf-8")

def _extract_datetime(html_doc):
	soup = BeautifulSoup(html_doc)
	datetime = soup.find("p", "date").span.get_text().encode("utf-8").split(': ')[1].replace('.','-')
	# datetime에도 utf-8 인코딩이 필요해????
	return datetime

def __trim_content(html_doc):
	soup = BeautifulSoup(html_doc)

	# 사진이 있는경우 그 사진의 주석을 지운다.
	photo = soup.find("table", "photo-view-area")
	if(photo):
		photo.clear()

	content = soup.find("div", "article-contents").findAll(text=True)
	content = " ".join(content).strip().replace("\t", "")
	return content.encode("utf-8")

def _extract_content(html_doc):
	content = __trim_content(html_doc)
	content = content.replace("\n", "").split('추천기사 ::')[0]
	return content

def _extract_author_info(html_doc):
	content = __trim_content(html_doc)
	find_author_info=[u" 기자 ", u" 기자", u"기자", u" 선임기자",u" 특파원", u"편집장", u" 인턴기자", u"뉴스팀", u"뉴시스", u"연합뉴", u"교수", u"위원", u"뉴스1", u"연구원"]
	keyword = {"author_info_keyword" : find_author_info}
	author_info = " "
	# print len(find_author)
	# print find_author[3].encode('utf-8')
	escape = False
	for line in reversed(content.splitlines()):
		for keyword in find_author_info:
			if(line.rfind(keyword.encode('utf-8')) != -1):
				# print "author키워드: " + keyword.encode('utf-8')
				author_info = line
				escape = True
				break
			if(escape):
				break
	if(author_info.find('연합뉴') != -1):
		author_info = author_info[author_info.rfind('.')+1 :].strip()

	if(len(author_info) > 200):
		author_info = author_info[author_info.split('@')[0].rfind(".")+1: ].strip()

	return author_info.strip()
'''
def _check_email_exit(author_info):
	match = re.search(r'\w+@\w+', author_info)
	if match:
		return 1
	else:
		return 0
'''	
# 이메일 갯수로 저장	-> 단순 BOOL 값 이상의 정보를 가진다.
def _extract_email_existance(author_info):
	email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"
	emails = re.findall(email_pattern, author_info)
	return len(emails)

# 전체 기사의 URL을 모두 긁어온다
# def _find_last_pagenum():
# 	URL = "http://www.hani.co.kr/arti/?type=0&cline="
# 	html_doc = urllib.urlopen(URL).read()
# 	soup = BeautifulSoup(html_doc)
# 	return soup.find("a", "last")['href'].split("=")[2]
# hani_URLs=[];
# for i in range(1, int(_find_last_pagenum())) :
# 	hani_URLs = hani_URLs + get_artile_urls_with_pagenum(i)
# 	for j in hani_URLs:
# 		parse_article_with_url(j)



# parse_article_with_url test
# parse_article_with_url('http://www.hani.co.kr/arti/politics/politics_general/631205.html')



# 한 페이지 당 DB에 입력. 로그 만들기 emailNotFound, 응답없음, 인코딩Err

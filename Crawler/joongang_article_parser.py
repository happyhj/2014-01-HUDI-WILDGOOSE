#!/usr/bin/python
# -*- coding: utf-8 -*-

import re
import urllib
from bs4 import BeautifulSoup

# return: list of articles
def get_article_urls_with_pagenum(page_num) :
	PRE_URL = "http://article.joins.com/news/list/list.asp?sc=JO&page="
	URL = PRE_URL + str(page_num)

	html_doc = urllib.urlopen(URL).read()
	soup = BeautifulSoup(html_doc)

	news_list = soup.find('div', 'bd').ul
	article_urls = []
	for news in news_list.find_all('li') :
		# NON-TEXT CHILD
		article_url = news.dl.dt.a['href']
		article_urls.append(article_url)
	return article_urls

# return: dict of article
def parse_article_with_url(article_url) :
	article = urllib.urlopen(article_url).read()
	
	article_info = dict()
	article_info['URL'] = article_url.encode('utf-8')
	article_info['title'] = _extract_title(article)
	article_info['datetime'] = _extract_datetime(article)
	article_info['content'] = _extract_content(article)
	#article_info['provider'] = _extract_provider(article)
	article_info['press_id'] = 2

	article_info['section'] = _extract_section(article)
	article_info['author_info'] = _extract_author(article)
	article_info['is_email_exist'] = _extract_email_existance(article_info['author_info'])

	return article_info

def print_article_info(article_info) :
	print article_info['URL'].encode('utf-8')
	print article_info['title']
	# print article_info['datetime']
	# print article_info['content']
	# print article_info['provider']
	# print article_info['section']
	print article_info['author']
	print ''

def _extract_title(article) :
	article_soup = BeautifulSoup(article)
	title_div = article_soup.find('div', 'title')
	title = title_div.h3.get_text()
	return title.encode('utf-8')

def _extract_section(article) :
	from joongang_service_code import CTG_CODE

	article_soup = BeautifulSoup(article)
	script_text = article_soup.head.get_text()
	ctg_code_pos = script_text.find('sServiceCode')
	ctg_code = script_text[ctg_code_pos+16:ctg_code_pos+20]

	large_code = ctg_code[0:2]
	small_code = ctg_code[2:4]

	large_ctg = CTG_CODE[large_code]['k']
	return large_ctg

def _extract_datetime(article) :
	article_soup = BeautifulSoup(article)
	title_div = article_soup.find('div', 'title')
	date = title_div.find('span', 'date').get_text()
	datetime = re.search('[0-9\.]+\s[0-9:]+', date).group(0) + ':00'
	return datetime

def _extract_author(article) :
	article_soup = BeautifulSoup(article)
	article_soup = __remove_photo(article_soup)

	content_div = article_soup.find('div', 'article_content');
	jname = __get_journalist_name(content_div)
	jemail = __get_email(content_div)
	return (jname + ' ' + jemail).encode('utf-8')

def _extract_content(article) :
	article_soup = BeautifulSoup(article)
	article_soup = __remove_photo(article_soup)

	content_div = article_soup.find('div', 'article_content');
	content = content_div.get_text().strip()
	return content.encode('utf-8')

def _extract_provider(article) :
	article_soup = BeautifulSoup(article)
	title_div = article_soup.find('div', 'title')
	provider = title_div.find('em', 'provide').get_text()[1:-1]

	return provider.encode('utf-8')

def __get_journalist_name(content_div) :
	content = content_div.get_text()
	result = re.findall(u'[가-힣·,\s]+\s?[가-힣]*?[^(연)(학생)]기자[^(간담)(회견)(들)(의)(는)(와)(가)(로)(에게)(인)(답게)(동차)(재)(예요)(조선)(기)]+?', content)
	faraway = re.findall(u'[가-힣·]+\s?특파원[^(들)(의)(와)(가)(에게)(인)(답게)(으로)]+?', content)

	result += faraway

	if (len(result) != 0) :
		i = 0
		for name in result :
			result[i] = name[:-1].strip()
			i += 1
		return ','.join(result)
	return 'None'

def __get_email(content_div) :
	email_list = re.findall('[-0-9a-zA-Z][-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[-0-9a-zA-Z.+_]+', content_div.get_text().strip())
	if (len(email_list) != 0) :
		return ','.join(email_list)
	return 'None'

# 이메일 갯수로 저장	-> 단순 BOOL 값 이상의 정보를 가진다.
def _extract_email_existance(author_info):
	email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"
	emails = re.findall(email_pattern, author_info)
	return len(emails)
	
def __remove_photo(article_soup) :
	# remove photo div
	photo_divs = article_soup.find_all('div', 'html_photo')
	photo_divs += article_soup.find_all('div', 'html_photo_center')
	if(photo_divs is not None):
		for photo_div in photo_divs :
			photo_div.clear()
	return article_soup
#!/usr/bin/python
# -*- coding: utf-8 -*-

import re
import urllib
from bs4 import BeautifulSoup

# return: list of articles
def get_article_urls_with_pagenum(page_num) :
	PRE_URL = "http://article.joins.com/news/list/list.asp?sc=JO&ctg=17&page="
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
	article_info['URL'] = article_url
	article_info['title'] = extract_title(article)
	article_info['datetime'] = extract_datetime(article)
	article_info['content'] = extract_content(article)
	article_info['provider'] = extract_provider(article)

	article_info['section'] = extract_section(article)
	article_info['author'] = extract_author(article)

	return article_info

def extract_title(article) :
	article_soup = BeautifulSoup(article)
	title_div = article_soup.find('div', 'title')
	title = title_div.h3.get_text()
	return title

def extract_section(article) :
	import getServiceCodeName

	article_soup = BeautifulSoup(article)
	script_text = article_soup.head.get_text()
	ctg_code_pos = script_text.find('sServiceCode')
	ctg_code = script_text[ctg_code_pos+16:ctg_code_pos+20]

	large_code = ctg_code[0:2]
	small_code = ctg_code[2:4]

	large_ctg = getServiceCodeName.CTG_CODE[large_code]['k']
	return large_ctg

def extract_datetime(article) :
	article_soup = BeautifulSoup(article)
	title_div = article_soup.find('div', 'title')
	date = title_div.find('span', 'date').get_text()
	datetime = re.search('[0-9\.]+\s[0-9:]+', date).group(0) + ':00'
	return datetime

def extract_author(article) :
	article_soup = BeautifulSoup(article)
	article_soup = remove_photo(article_soup)

	content_div = article_soup.find('div', 'article_content');
	jname = get_journalist_name(content_div)
	jemail = get_email(content_div)
	return jname + ' / ' + jemail

def extract_content(article) :
	article_soup = BeautifulSoup(article)
	article_soup = remove_photo(article_soup)

	content_div = article_soup.find('div', 'article_content');
	content = content_div.get_text().strip()
	return content

def extract_provider(article) :
	article_soup = BeautifulSoup(article)
	title_div = article_soup.find('div', 'title')
	provider = title_div.find('em', 'provide').get_text()[1:-1]

	return provider

def get_journalist_name(content_div) :
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

def get_email(content_div) :
	email_list = re.findall('[-0-9a-zA-Z][-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[-0-9a-zA-Z.+_]+', content_div.get_text().strip())

	if (len(email_list) != 0) :
		return ','.join(email_list)

	return 'None'

def remove_photo(article_soup) :
	# remove photo div
	photo_divs = article_soup.find_all('div', 'html_photo')
	photo_divs += article_soup.find_all('div', 'html_photo_center')
	if(photo_divs is not None):
		for photo_div in photo_divs :
			photo_div.clear()
	return article_soup

def print_article_info(article_info) :
	print article_info['URL'].encode('utf-8')
	# print article_info['title'].encode('utf-8')
	# print article_info['datetime'].encode('utf-8')
	# print article_info['content'].encode('utf-8')
	print article_info['provider'].encode('utf-8')

	# Section text is saved in python file -> no need to encode
	# print article_info['section']
	print article_info['author'].encode('utf-8')
	print ''


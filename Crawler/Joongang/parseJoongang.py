#!/usr/bin/python
# -*- coding: utf-8 -*-

import sys
import os.path
parent_dir = os.path.abspath(os.path.join(sys.path[0], os.pardir))
sys.path.append(parent_dir)

import re
import urllib
from bs4 import BeautifulSoup

import articleList
import getServiceCodeName

def parse_joongang(page_num) :
	PRE_URL = articleList.get_url('joongang')
	URL = PRE_URL + str(page_num)

	html_doc = urllib.urlopen(URL).read()
	soup = BeautifulSoup(html_doc)

	news_list = soup.find('div', 'bd').ul

	for news in news_list.find_all('li') :
		# NON-TEXT CHILD
		article_url = news.dl.dt.a['href']
		news_info = parse_joongang_article(article_url)
		print_news_info(news_info)

def parse_joongang_article(article_url) :
	article = urllib.urlopen(article_url).read()
	article_soup = BeautifulSoup(article)

	# remove photo div
	[s.extract() for s in article_soup('div', 'html_photo')]

	news_info = dict()

	news_info['URL'] = article_url

	title_div = article_soup.find('div', 'title')
	content_div = article_soup.find('div', 'article_content');

	news_info['title'] = title_div.h3.get_text()
	news_info['datetime'] = title_div.find('span', 'date').get_text()
	news_info['content'] = content_div.get_text().strip()
	# news_info['provider'] = title_div.find('em', 'provide').get_text()[1:-1]

	news_info['section'] = get_joongang_section(article_soup)
	news_info['author'] = get_journalist_name(content_div)
	news_info['email'] = get_email(content_div)

	return news_info

def get_joongang_section(article_soup) :
	script_text = article_soup.head.get_text()
	ctg_code_pos = script_text.find('sServiceCode')
	ctg_code = script_text[ctg_code_pos+16:ctg_code_pos+20]

	large_code = ctg_code[0:2]
	small_code = ctg_code[2:4]

	large_ctg = getServiceCodeName.CTG_CODE[large_code]['k']
	if (small_code == '00') :
		return large_ctg
	else :
		small_ctg = getServiceCodeName.CTG_CODE[large_code]['s'+small_code]['k']
		return large_ctg + ' > ' + small_ctg

def get_journalist_name(content_div) :
	news_text = content_div.get_text()
	result = re.findall(u'[가-힣]+\s?[(인턴)(선임)]*?[^(연)(학생)]기자[^(간담)(회견)(들)(와)(가)(에게)(동차)(재)]+?', news_text)

	if (len(result) != 0) :
		i = 0
		for name in result :
			result[i] = name[:-1]
	# 		if (name.find("뉴시스") == 0) :
	# 			result[i] = name[12:]
			i += 1
		return ','.join(result)

	return 'None'

def get_email(content_div) :
	email_list = re.findall('[-0-9a-zA-Z][-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[-0-9a-zA-Z.+_]+', content_div.get_text().strip())

	if (len(email_list) != 0) :
		return ','.join(email_list)

	return 'None'

def print_news_info(news_info) :
	# print news_info['URL'].encode('utf-8')
	# print news_info['title'].encode('utf-8')
	# print news_info['datetime'].encode('utf-8')
	# print news_info['content'].encode('utf-8')
	# print news_info['provider']

	# Section text is saved in python file -> no need to encode
	# print news_info['section']
	print news_info['author'].encode('utf-8')
	# print news_info['email'].encode('utf-8')
	print ''

for i in range(1, 10) :
	parse_joongang(i)
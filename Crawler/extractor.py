#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: extractor.py

# MySQL Connector
import DB_connector as db

import re
from datetime import datetime

def main() :
	result = _get_raw_data()

	con = db.connect_dev()

	for row in result :
		# article table
		article = _make_article_info(con, row)
		query = db.make_insert_query('article', article)
		db.do_insert(con, query)

		# author table
		expected_author_string = row[5]
		author_list = _make_author_list(con, expected_author_string)

		for author in author_list :
			if not _is_author_exits(author['email']) :
				query = db.make_insert_query('author', author)
				db.do_insert(con, query)
			# Get author_id
			query = "SELECT id FROM author WHERE email=\'" + author['email'] + "\'"
			author_id = db.do_select(con, query)
			author_id = author_id[0][0]
			print author_id, author['name'], author['email'], author['press_id']

		# article_author table
			article_author = {'article_URL': article['URL'], 'author_id': author_id}
			query = db.make_insert_query('article_author', article_author)
			db.do_insert(con, query)


def _get_raw_data() :
	con_r = db.connect_raw()
	query = 'SELECT * FROM article'
	result = db.do_select(con_r, query)
	con_r.close()
	return result

def _make_article_info(con, row) :
	article = {}
	article['URL'] = row[0].encode('utf-8')
	article['title'] = row[1].encode('utf-8')
	article['content'] = __clip_content(row[2]).encode('utf-8')
	article['section_id'] = __get_section_id(con, row[3])
	article['datetime'] = str(row[4]).encode('utf-8')

	return article

def __get_section_id(con, section_name) :
	section_keywords = re.findall(u"[가-힣]+", section_name)
	kwd_num = len(section_keywords)

	# 일치하는 키워드가 전혀 없을 때
	if kwd_num == 0 :
		return 0
	kwd_idx = kwd_num - 1

	while True :
		last_keyword = section_keywords[kwd_idx]
		# 일반: 분류할 수 없음
		if last_keyword == u'일반' :
			kwd_idx -= 1
			continue

		# 키워드 탐색
		query = "SELECT id FROM section WHERE name LIKE \'%" + last_keyword + "%\'"
		result = db.do_select(con, query)

		# 일치 키워드를 찾았을 때, 반환
		if len(result) > 0 :
			return result[0][0]
		# 찾지 못했을 때, 상위 키워드로 이동
		kwd_idx -= 1
		# 일치하는 키워드가 전혀 없을 때
		if kwd_idx < 0 :
			return 0

def _make_author_list(con, expected_string) :
	author_list = []
	
	# get email list
	email_list = __extract_emails(expected_string)

	for email in email_list :
		if _is_author_exits(email) :
			author = __get_if_email_exits(con, email)
		else :
			author = __extract_author(con, email)
		if author is not None :
			author_list.append(author)
	
	return author_list

def __extract_emails(string) :
	email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"	
	emails = re.findall(email_pattern, string)
	return emails

def __extract_author(con, email) :
	"""
	id, name, email, press_id, added_date
	"""
	author = {}

	con_r = db.connect_raw()
	query = "SELECT URL, author_info FROM article WHERE author_info like \'%" + email + "%\'"
	result = db.do_select(con_r, query)
	url = result[0][0].split('/')[2]

	# SUM ALL POSSIBLE NAME
	possible_words = {}
	for row in result :
		author_info = row[1]
		possible_words_in_article = __extract_name(author_info)
		for k, v in possible_words_in_article.items() :
			possible_words[k] = possible_words.get(k, 0) + v

	max_value = 0
	name = ""
	for key, value in possible_words.items() :
		if (value > max_value) :
			if key == u'' :
				continue
			name = key
			max_value = value

	if max_value < 3 :
		return None

	author['name'] = name.encode('utf-8')
	author['email'] = email.encode('utf-8')
	author['press_id'] = __get_press_id_from(url)
	author['added_date'] = str(__get_today()).encode('utf-8')
	
	return author		
	# print max_value, name.encode('utf-8'), email

def __get_if_email_exits(con, email) :
	query = 'SELECT * from author WHERE email = \'' + email + '\''
	result = db.do_select(con, query)
	if len(result) == 0 :
		return None
	else :
		author = {}
		for row in result :
			author['id'] = row[0]
			author['name'] = row[1]
			author['email'] = row[2]
			author['press_id'] = row[3]
			author['added_date'] = row[4]
		return author

def __clip_content(content) :
	return content[:100]

def __extract_name(content) :
	keywords = {}
	words = re.findall(u"[가-힣]+", content)
	words = list(set(words))
	# init
	for word in words :
		keywords[word] = 0
	# add
	for word in words :
		keywords[word] += 1
	# check
	for word in words :
		if word == u'기자' :
			del keywords[word]
		if word.find(u'@') > 0 :
			del keywords[word]
		if len(word) > 4 :
			del keywords[word]
	return keywords

def __get_press_id_from(url) :
	con_d = db.connect_dev()
	query = 'SELECT id FROM press WHERE domain=\'' + url + '\''
	result = db.do_select(con_d, query)
	con_d.close()
	return result[0][0]

def __get_today() :
	i = datetime.now()
	return i.strftime('%Y-%m-%d %H:%M:%S')

def _is_author_exits(email) :
	con_d = db.connect_dev()
	query = 'SELECT id FROM author WHERE email=\'' + email + '\''
	result = db.do_select(con_d, query)
	con_d.close()
	
	if len(result) == 0 :
		return False
	return True

if __name__ == "__main__":
	main()

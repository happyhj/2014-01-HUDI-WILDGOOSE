#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: extractor.py

# MySQL Connector
import DB_connector as db

import re

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

		####### THIS IS PSUDO_CODE #######
		# if author_info not in DB :
		# 	query = db.make_insert_query('author', author_info)
		# 	db.do_insert(con, query)
		# article_author table
		# article_author = {'article_URL': article['URL'], 'author_id': author_info['id']}
		# query = db.make_insert_query('article_author', article_author)
		# db.do_insert(con, query)
		####### THIS IS PSUDO_CODE #######

def _get_raw_data() :
	con_r = db.connect_raw()
	query = 'SELECT * FROM article'
	result = db.do_select(con_r, query)
	con_r.close()
	return result

def _make_article_info(con, row) :
	article = {}
	article['URL'] = row[0]
	article['title'] = row[1]
	article['content'] = __clip_content(row[2])
	article['section_id'] = __get_section_id(con, row[3])
	article['datetime'] = row[4]

	return article

def __get_section_id(con, section_name) :
	return 3

def _make_author_list(con, expected_string) :
	author_list = []
	
	# get email list
	email_list = __extract_emails(expected_string)

	for email in email_list :
		author = __get_if_email_exits(con, email)
		if (author == None) :
			author = __extract_author(con, email)
		
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
			
	print max_value, name.encode('utf-8'), email
	
	# GET INSERTED DATA's ID
	# query = "SELECT last_insert_id()"
	# result = db.do_select(con, query)

def __get_if_email_exits(con, email) :
	query = 'SELECT * from author WHERE email = \'' + email + '\''
	result = db.do_select(con, query)
	if len(result) == 0 :
		return None
	else :
		return result[0]

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


# if __name__ == "__main__":
# 	main()
email_list = ['3code@hankyung.com', 'abc@donga.com', 'abcd@hani.co.kr', 'ace@hankyung.com', 'acirfa@joongang.co.kr', 'adche@joongang.co.kr', 'adonis55@joongang.co.kr', 'africasun@joongang.co.kr', 'agatha77@hankyung.com', 'ahnjk@hankyung.com', 'ahnyinhay@hotmail.com', 'ahs@hankyung.com', 'aimhigh@donga.com', 'americano@joongang.co.kr', 'anaki@hani.co.kr', 'ansesi@joongang.co.kr', 'anzy@joongang.co.kr', 'apple@hankyung.com', 'appletree@hani.co.kr', 'argos@hankyung.com', 'askme@joongang.co.kr', 'azul@joongang.co.kr', 'bae2150@donga.com', 'balgun@donga.com', 'baltika7@donga.com', 'bebop@hankyung.com', 'beh@donga.com', 'being@joongang.co.kr', 'beje@hankyung.com', 'beneath@joongang.co.kr', 'benoist@joongang.co.kr', 'bepop@hankyung.com', 'best@donga.com', 'bestemployerskorea@aonhewitt.com', 'bhchoi@lgeri.com', 'bim@joongang.co.kr', 'bk11@hankyung.com', 'black0419@hankyung.com', 'blast@joongang.co.kr', 'bluedot@donga.com', 'bong@hani.co.kr', 'bong@koreadaily.com', 'bong9@hani.co.kr', 'bonggari@joongang.co.kr', 'bonhong@donga.com', 'bono@hankyung.com', 'boriam@donga.com', 'bradkim@joongang.co.kr', 'bright@donga.com', 'bsism@donga.com', 'byungjoo@joongang.co.kr', 'cano@hani.co.kr', 'catalunia@hani.co.kr', 'ccandori@hani.co.kr', 'ccat@hankyung.com', 'cdkang@hankyung.com', 'ceo@mson.co.kr', 'ceo@roadtest.kr', 'ceoseo@hankyung.com', 'chan@hani.co.kr', 'chang@donga.com', 'charisma@hani.co.kr', 'chazz@joongang.co.kr', 'chdck@joongang.co.kr', 'che@hani.co.kr', 'chihiro@hankyung.com', 'cho@hani.co.kr', 'choi.yun-mee@shinyoung.com', 'choigo@joongang.co.kr', 'choiji@joongang.co.kr', 'choissie@joongang.co.kr', 'chomg@joongang.co.kr', 'chsan@hankyung.com', 'chung@hani.co.kr', 'chyun3344@daum.net', 'cjhoon@hani.co.kr', 'cjs@donga.com', 'ckhaa@joongang.co.kr', 'clartee@naver.com', 'click@hankyung.com', 'cmjang@hankyung.com', 'comedy9@donga.com', 'comeon@hankyung.com', 'confetti@donga.com', 'cooly@hani.co.kr', 'cosmos@hankyung.com', 'crystal@donga.com', 'csw@hani.co.kr', 'cym2060@joongang.co.kr', 'dadad@hankyung.com', 'daedan@joongang.co.kr', 'daeha@hani.co.kr', 'daepun@hankyung.com', 'dalsarang@donga.com', 'dandy@hani.co.kr', 'dbpark@hri.co.kr', 'ddak@hankyung.com', 'demian@donga.com', 'destinybr@hankyung.com', 'dew@donga.com']
for email in email_list :
	__extract_author(None, email)
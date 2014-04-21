#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: crawler.py

# ARTICLE PARSERs
import donga_article_parser as donga
import hani_article_parser as hani
import joongang_article_parser as joongang
import hankyung_article_parser as hankyung
# and so on

# MySQL Connector
import DB_connector as db

# Python Libraries
import sys
import time
from contextlib import closing

# NEEDS MANUAL HANDLING
press_dict = {
	"donga" : donga, 
	"hani" : hani, 
	"joongang" : joongang, 
	"hankyung" : hankyung
}

def main(argv) :
	press_name = argv[1]
	start_page_index = int(argv[2])
	end_page_index = int(argv[3])
	print press_name
	
	press = press_dict[press_name]
	
	con = db.connect_raw()

	for i in range(start_page_index, end_page_index+1) :
		# get 10-20 url and insert
		url_list = press.get_article_urls_with_pagenum(i)

		print "page: " + str(i)

		for url in url_list :
			print url
			try :
				article = press.parse_article_with_url(url)
			except :
				print 'retry parsing!'
				article = press.parse_article_with_url(url)
			
			query = db.make_insert_query("article", article)
			result = db.do_insert(con, query)
			time.sleep(1.5)
		time.sleep(5)
	con.close()

if __name__ == "__main__" :
	main(sys.argv)

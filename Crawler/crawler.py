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
from contextlib import closing

#press_list = [donga, hani, joongang, hankyung]
press_dict = {
	"donga" : donga, 
	"hani" : hani, 
	"joongang" : joongang, 
	"hankyung" : hankyung
}

# NEEDS MANUAL HANDLING
def main(argv) :
    #press_index = int(argv[1])-1
    press_name = argv[1]
    start_page_index = int(argv[2])
    end_page_index = int(argv[3])
    print press_name
    
    press = press_dict[press_name]
#    press_name = press.get_press_name()
#    print press_name
    
    con = db.connect()
    with closing(con.cursor()) as cur :
        for i in range(start_page_index, end_page_index+1) :
            # get 10-20 url and insert
            url_list = press.get_article_urls_with_pagenum(i)
            print "page: " + str(i)

            for url in url_list :
                article = press.parse_article_with_url(url)
                query = db.make_insert_query("article", article)
                cur.execute(query)
                con.commit()

                author_info = article['author_info']
                do_something_with_author_info(author_info)
    con.close()

def do_something_with_author_info(author_info) :
    return None

if __name__ == "__main__":
    main(sys.argv)
#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: crawler.py
'''
import joongang_article_parser as joongang
import hankyung_article_parser as hankyung
'''
import donga_article_parser as donga
import hani_article_parser as hani
import joongang_article_parser as joongang
import hankyung_article_parser as hankyung

import sys
# and so on

import DB_connector as db
from contextlib import closing

#press_list = [joongang, hankyung, donga]
press_list = [donga, hani, joongang, hankyung]

# NEEDS MANUAL HANDLING
def main(argv) :
    press_index = int(argv[1])-1
    start_page_index = int(argv[2])
    end_page_index = int(argv[3])

    print press_index
    press = press_list[press_index]
    press_id = press.get_press_id()
    con = db.connect()

    with closing(con.cursor()) as cur :
        for i in range(start_page_index, end_page_index+1) :
            # get 10-20 url and insert
            url_list = press.get_article_urls_with_pagenum(i)

            for url in url_list :
                article = press.parse_article_with_url(url)
                author_info = article['author_info']
                query = db.make_insert_query("article", article)
                #print query
                cur.execute(query)
                con.commit()

    con.close()
    
if __name__ == "__main__":
    main(sys.argv)
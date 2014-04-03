#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: pressname_article_parser.py

import urllib
import re
from bs4 import BeautifulSoup

"""
NEVER USE GLOBAL VARIABLE
"""

"""
public methods
crawler use these methods
"""

def get_press_name() :
    return "PRESS NAME"

def get_article_urls_with_pagenum(page_num) :

    LIST_URL = ""

    url = LIST_URL + str(page_num)
    html_doc = get_html_doc(url)
    # soup = BeautifulSoup(html_doc)

    article_urls = []

    # PARSING CODE HERE

    return article_urls

def parse_article_with_url(url) :
    html_doc = _get_html_doc(url)
    article = {}

    article["url"] = url.encode('utf-8')
    article["section"] = _extract_section(html_doc)
    article["title"] = _extract_title(html_doc)
    article["datetime"] = _extract_datetime(html_doc)
    article["contents"] = _extract_contents(html_doc)
    article["author_info"] = _extract_author_info(html_doc)
    article["is_email_exist"] = _is_email_exist(article["author_info"])

    return article

"""
private methods
starts with _
"""

def _get_html_doc(url) :
    net_obj = urllib.urlopen(url)
    html_doc = net_obj.read()
    net_obj.close()
    return html_doc

def _extract_section(html_doc):
    # CODE HERE
    return "section".encode('utf-8') # SHOULD BE UTF-8

def _extract_title(html_doc):
    # CODE HERE
    return "title".encode('utf-8') # SHOULD BE UTF-8

def _extract_datetime(html_doc):
    # CODE HERE
    return "yyyy-mm-dd hh:mm:ss"

def _extract_contents(html_doc):
    # CODE HERE
    return "contents".encode('utf-8') # SHOULD BE UTF-8

def _extract_author_info(html_doc):
    # CODE HERE
    return "author info have author@email.com and other text" .encode('utf-8')# SHOULD BE UTF-8

def _is_email_exist(author_info) :
    # CODE HERE
    return 1 #=true or 0=false

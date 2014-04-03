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

    # PARSING CODES HERE

    return article_urls

def parse_article_with_url(url) :
    html_doc = _get_html_doc(url)
    article = {}

    article["url"] = url
    article["section"] = _extract_section(html_doc)
    article["title"] = _extract_title(html_doc)
    article["datetime"] = _extract_datetime(html_doc)
    article["contents"] = _extract_contents(html_doc)
    article["author_info"] = _extract_author_info(html_doc)

    return article

"""
private methods
"""

def _get_html_doc(url) :
    net_obj = urllib.urlopen(url)
    html_doc = net_obj.read()
    net_obj.close()
    return html_doc

def _extract_section(html_doc):
    # CODES HERE
    return "section" # SHOULD BE UTF-8

def _extract_title(html_doc):
    # CODES HERE
    return "title" # SHOULD BE UTF-8

def _extract_datetime(html_doc):
    # CODES HERE
    return "yyyy-mm-dd hh:mm" # SHOULD BE UTF-8

def _extract_contents(html_doc):
    # CODES HERE
    return "contents" # SHOULD BE UTF-8

def _extract_author_info(html_doc):
    # CODES HERE
    return "author info have author@email.com and other text" # SHOULD BE UTF-8
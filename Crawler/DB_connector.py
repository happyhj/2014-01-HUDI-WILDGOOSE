#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: DB_connector.py

import mysql.connector
import re


def connect() :
    config = {  "host":"10.73.45.134",
#    config = {  "host":"127.0.0.1",
                "user":"root",
                "password":"wildgoose",
                "database":"wildgoose",
                "charset" :'utf8'}

    con = mysql.connector.connect(**config)
    return con

def make_insert_query(table_name, data) :
    # ORDERING DATA
    query_column = []
    query_value = []

    for key in data.keys() :
        value = data[key]
        if type(value) is str :
            value = handle_apostrophe(value)
        query_column.append(key)
        query_value.append(value)

    column_count = len(query_column)

    # MAKE QUERY
    try :
        QUERY = "INSERT INTO " + table_name + " ("
        for i in range(0, column_count) :
            QUERY += query_column[i] + ', '
        QUERY = QUERY[:-2] + ") "

        QUERY += "VALUES ("
        for i in range(0, column_count) :
            QUERY += "'" + str(query_value[i]) + "', "
        QUERY = QUERY[:-2] + ") ON DUPLICATE KEY UPDATE "

        for i in range(0, column_count) :
            QUERY += query_column[i] + "='" + str(query_value[i]) + "', "
        QUERY = QUERY[:-2]

    except UnicodeError :
        # print QUERY
        print "Unicode Error at " + str(data)
        return None

    return QUERY

def make_insert_into_author_table_query(author_info) :
	emails = _extract_emails(author_info)
	QUERY = "INSERT INTO author VALUES "
	for email in emails :
		QUERY += "('"+email+"'),"
	return QUERY[:-1]	
	
def make_insert_into_article_author_table_query(url, author_info) :
	emails = _extract_emails(author_info)
	QUERY = "INSERT INTO article_author VALUES "
	for email in emails :
		QUERY += "('"+email+"','"+url+"'),"
	return QUERY[:-1]
	
def _extract_emails (paragraph) :
	email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"	
	emails = re.findall(email_pattern, paragraph)
	return emails	

def handle_apostrophe(string) :
    splited = string.split("'")
    edited = "\\'".join(splited)
    return edited
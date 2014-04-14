#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: DB_connector.py

import mysql.connector
import re
from contextlib import closing


def connect_dev() :
    config = {  'host':'125.209.198.141',
                'user':'python',
                'password':'wildgoose',
                'database':'wildgoose_dev',
                'charset' :'utf8'}

    con = mysql.connector.connect(**config)
    return con

def connect_raw() :
    config = {  'host':'125.209.198.141',
                'user':'python',
                'password': 'wildgoose',
                'database': 'wildgoose_raw',
                'charset' : 'utf8'}

    con = mysql.connector.connect(**config)
    return con

# return is query worked
def do_insert(con, query) :
    with closing(con.cursor()) as cur :
        try :
            cur.execute(query)
            con.commit()
            return True
        except mysql.connector.Error as err :
            print ("Insert Error -  {}".format(err))
            return False

# return Selected rows
def do_select(con, query) :
    with closing(con.cursor()) as cur :
        try :
            cur.execute(query)
            result = cur.fetchall()
        except mysql.connector.Error as err :
            print ("Select Error -  {}".format(err))

    return result

def make_insert_query(table_name, data) :
    # ORDERING DATA
    key_list = data.keys()
    value_list = []

    for key in key_list :
        value = data[key]
        if type(value) is str :
            value = handle_apostrophe(value)
        value_list.append(value)

    key_len = len(key_list)

    # MAKE QUERY
    try :
        QUERY = "INSERT INTO " + table_name + " ("
        for key in key_list :
            QUERY += key + ', '
        QUERY = QUERY[:-2] + ") "

        QUERY += "VALUES ("
        for value in value_list :
            QUERY += "'" + str(value) + "', "
        QUERY = QUERY[:-2] + ") ON DUPLICATE KEY UPDATE "

        for i in range(0, key_len) :
            QUERY += key_list[i] + "='" + str(value_list[i]) + "', "
        QUERY = QUERY[:-2]

    except UnicodeError as err :
        # print QUERY
        print "Unicode Error at " + err.object
        return None

    return QUERY

### 지금은 Case별로 Insert Query를 만드는게 효과적이지만, 
### 다양해지고 나면 범용 함수로 관리하는게 나을지도 모르므로
### 미리 만들어둠.
def make_list_insert_query(table_name, data_list, const_data=None) :
    key_list = data_list[0].keys()
    key_len = len(key_list)
    if (const_data is not None) :
        const_key_list = const_data.keys()
        const_len = len(const_data)

    try :
        QUERY = "INSERT INTO " + table_name + " ("
        for key in key_list :
            QUERY += key + ', '
        if (const_data is not None) :
            for ckey in const_key_list :
                QUERY += ckey + ", "
        QUERY = QUERY[:-2] + ") "
    
        QUERY += "VALUES "
        for data in data_list :
            QUERY += "("
            for key in key_list :
                value = data[key]
                if type(value) is str :
                    value = handle_apostrophe(value)
                
                QUERY += "'" + str(value) + "', "
            if (const_data is not None) :
                for ckey in const_key_list :
                    value = const_data[ckey]
                    if type(value) is str :
                        value = handle_apostrophe(value)
                    QUERY += "'" + str(value) + "', "
            QUERY = QUERY[:-2] + "), "
        QUERY = QUERY[:-2]

    except UnicodeError as err :
        # print QUERY
        print "Unicode Error at " + err.object
        return None

    return QUERY
	
def make_insert_into_article_author_table_query(url, author_info) :
	emails = _extract_emails(author_info)
	QUERY = "INSERT INTO article_author VALUES "
	for email in emails :
		QUERY += "('"+email+"','"+url+"'),"
	return QUERY[:-1]

def handle_apostrophe(string) :
    splited = string.split("'")
    edited = "\\'".join(splited)
    return edited

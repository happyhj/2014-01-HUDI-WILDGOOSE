#!/usr/bin/python
# -*- coding: utf-8 -*-

import mysql.connector
from mysql.connector import errorcode

import urllib
import threading
import datetime
import articleParser
import newsSQL
from bs4 import BeautifulSoup
from time import sleep

def getHtmlDoc ( url, dateName, date, pageName, page ) :
	newUrl = url + dateName + date + pageName + str ( page )

	u = urllib.urlopen ( newUrl )
	htmlDoc = u.read ( )
	u.close ( )

	return htmlDoc

def searchArticleUrl ( htmlDoc ) :	
	soup = BeautifulSoup ( htmlDoc )
	temp = soup.find ( id = "newslist_ty1" )
	tags = temp.findAll ( "h3" )

	urls = []

	for tag in tags :
		urls.append ( tag.a["href"] )

	return urls




#main
print "Start\n\n"

hanKyungUrl = "http://www.hankyung.com/news/app/newslist_all.php?tab=&iscts=&popup=1"
dateName = "&date="
date = "2014-03-06"
pageName = "&page="

htmlDoc = getHtmlDoc ( hanKyungUrl, dateName, date, pageName, 21 )
urls = searchArticleUrl ( htmlDoc )

try :
	cnx = mysql.connector.connect ( host = "10.73.45.134", user = "root", password = "wildgoose", database = "Wildgoose" )
	cursor = cnx.cursor ( )

	query = ( "INSERT INTO Articles ( URL, title, section, content, author, datetime ) VALUES ( %s, %s, %s, %s, %s, %s )" )

	# f = open ("article.txt", "a")
	ap = articleParser.ArticleParser ( )
	i = 1
	for url in urls :
		print str ( i )
		data = ap.get ( url )
		if data :
			## handle exception
			if data == "TypeError" :
				print "TypeError: " + url

			## insert query
			# cursor.execute ( query, ( data["url"], data["title"], data["section"], data["content"], data["author"], data["datetime"] ) )

			## print data
			# for key in data :
			# print "// " + key + ": " + data [ 'author' ]
			print "// " + data [ 'author' ]
		# cnx.commit ( )
		# print data
		i = i + 1
		# sleep ( 1 )

	cursor.close ( )
	cnx.close ( )

except mysql.connector.Error as err:

	if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
		print "Something is wrong with your user name or password"
	elif err.errno == errorcode.ER_BAD_DB_ERROR:
		print "Database does not exists"
	else:
		print err
else:
	cursor.close ( )
	cnx.close ( )


# f = open ("articleURL.txt", "w")
# f = open ("articleURL.txt", "a")

# for page in range ( 67 ) :
# 	print page+1
# 	saveArticleURL ( f, date, page+1 )
	

# f.close()

print "\n\nEnd"
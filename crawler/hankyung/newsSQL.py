#!/usr/bin/python
# -*- coding: utf-8 -*-

import mysql.connector
from mysql.connector import errorcode

class NewsSQL :

	def __init__ (self, config) :
		# 객체 생성시 _conn()을 실행하여 sql연결 유지
		self._conn(config)

	def _conn (self, config) :
		try :
			self.cnx = mysql.connector.connect ( **config )
			self.cursor = self.cnx.cursor()
			print "-sql connection success"

		except mysql.connector.Error as err:
			if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
				print "Something is wrong with your user name or password"
			elif err.errno == errorcode.ER_BAD_DB_ERROR:
				print "Database does not exists"
			else:
				print err


	def close ( self ) :
		try :
			self.cursor.close ( )
			self.cnx.close ( )

		except mysql.connector.Error as err:
			print err

	def insert (self, query, attribute) :
		try :
			self.cursor.execute(query, (attribute))
			self.cnx.commit()

		except mysql.connector.Error as err:
			print err




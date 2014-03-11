import mysql.connector
from mysql.connector import errorcode


class NewsSQL :

	# def conn ( self, **config ) :
	# def conn ( self, hostName, userName, passwd, dbName ) :
	def conn ( self ) :
		# try:
		# cnx = mysql.connector.connect ( host = hostName, user = userName, password = passwd, database = dbName )
		# "10.73.45.134", "root", "wildgoose", "Wildgoose"
		cnx = mysql.connector.connect ( host = "10.73.45.134", user = "root", password = "wildgoose", database = "Wildgoose" )
		# self.cnx = mysql.connector.connect ( **config )
		self.cursor = self.cnx.cursor ( Buffered = True )

		print "-sql connection success"
		# except mysql.connector.Error as err:
		# 	if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
		# 		return "Something is wrong with your user name or password"
		# 	elif err.errno == errorcode.ER_BAD_DB_ERROR:
		# 		return "Database does not exists"
		# 	else:
		# 		return err
		# else:
		# 	self.cursor.close ( )
		# 	self.cnx.close ( )

	def close ( self ) :
		# try :
		self.cursor.close ( )
		self.cnx.close ( )
		# except mysql.connector.Error as err:
		# 	print err

	def insert ( self, query, data ) :
		# print "in insert"
		self.cursor.execute ( query, data )
		self.cnx.commit ( )
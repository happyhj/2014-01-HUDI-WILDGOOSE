"""
import mysql.connector

# MySQL Connector
import DB_connector as db
from contextlib import closing

def main() :
	con = db.connect()
	with closing(con.cursor()) as cur :
		query = db.select_article_by_press("joins")
		try : 
			cur.execute(query)
			result = cur.fetchall()
		except :
			print query
			print "Select Error"
			return None
	con.close()

	keywords = {}
	for row in result :
		# row is tuple. as table-column order
		content = row[2]
		article_keywords = extract_keyword(content)
		for k, v in article_keywords.items() :
			keywords[k] = keywords.get(k, 0) + v

	return keywords
"""

"""
def extract_keyword(content) :
	keywords = {}
	words = content.split(' ')
	for word in words :
		keywords[word] = 0
	for word in words :
		keywords[word] += 1
	return keywords
"""
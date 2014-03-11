#!/usr/bin/pythond
# -*- coding: utf-8 -*-

import urllib
from bs4 import BeautifulSoup
# from time import sleep

class NewsCrawler :

	def __init__(self, sleepTime) :
		self.sleepTime = sleepTime`

	def get (self, url, typeName) :
		try : 
			if typeName == "doc" :
				return self._getHTMLDoc(url)
			elif typeName == "soup" :
				return self._getSoup(url)
		except :
			if typeName is None :
				return "type parameter is none"
			else :
				return "error"

	def _getHTMLDoc ( self, url ) :
		# self.url = url
		u = urllib.urlopen ( url )
		doc = u.read ( )
		u.close ( )		

		return doc

	def _getSoup (self, url) :
		doc = self._getHTMLDoc(url)
		return BeautifulSoup ( doc )
		
# -*- coding: utf-8 -*-
"""
Created on Thu Feb 23 11:23:07 2017

@author: Naren Suri
"""

class Node:
 
    def __init__ (self, parent, value=None,iscategory=None,wikiId=None,inLinks=None,outLinks=None,ispage=None):
        self.parent = parent
        self.value = value
        self.childList = []
        self.childPageList = []
        self.articleWikiId=wikiId
        #self.totalInLinks = inLinks
        #self.totalOutLinks=outLinks
        self.isPage=ispage
        self.isCategory = iscategory
        
        
        
        if  parent is None:
            self.birthOrder = 0
        else:
            self.birthOrder = len(parent.childList)
            parent.childList.append (self)
        
    def nChildren (self):
        return len(self.childList)

    def nthChild (self, n):
        return self.childList[n]

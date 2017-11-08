# -*- coding: utf-8 -*-
"""
Created on Fri Feb 24 00:20:47 2017

@author: Naren Suri
"""

import string
class Node:
    """ Generic n-ary tree node object
        Children are additive; no provision for deleting them.
        The birth order of children is recorded: 0 for the first
        child added, 1 for the second, and so on.
            GenericTree(parent, value=None)    Constructor
            parent         			If this is the root node, None, otherwise the parent's GenericTree object.
            childList      			List of children, zero or more GenericTree objects.
            value          			Value passed to constructor; can be any type.
            birthOrder     			If this is the root node, 0, otherwise the index of this child in the parent's .childList
            nChildren()    			Returns the number of self's children.
            nthChild(n)    			Returns the nth child; raises IndexError if n is not a valid child number.
            fullPath():    			Returns path to self as a list of child numbers.
            nodeId():     			Returns path to self as a NodeId.
    """
    def __init__ (self, parent, value=None,iscategory=None,wikiId=None,inLinks=None,outLinks=None,ispage=None):
        self.parent = parent
        self.value = value
        self.childList = []
        self.articleWikiId=wikiId
        self.totalInLinks = inLinks
        self.totalOutLinks=outLinks
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

    def fullPath (self):
        result = []
        parent = self.parent
        kid = self
        while  parent:
            result.insert (0, kid.birthOrder)
            parent, kid = parent.parent, parent
        return result

    def nodeId (self):
        fullPath = self.fullPath()
        return NodeId (fullPath)

class NodeId:
    def __init__ (self, path):
        self.path = path

    def __str__ (self):
        L = map (str, self.path)
        return string.join (L, "/")

    def find (self, node):
        return self.__reFind (node, 0)

    def __reFind (self, node, i):
        if  i >= len(self.path):
            return node.value  # We're there!
        else:
            childNo = self.path[i]
        try:
            child = node.nthChild (childNo)
        except IndexError:
            return None
        return self.__reFind (child, i + 1)

    def isOnPath (self, node):
        if  len(self.nodePath) > len(self.path):
            return 0  # Node is deeper than self.path

        for  i in range(len(self.nodePath)):
            if  self.nodePath[i] != self.path[i]:
                return 0  # Node is a different route than self.path
        return 1
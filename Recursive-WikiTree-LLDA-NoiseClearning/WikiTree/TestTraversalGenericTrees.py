# -*- coding: utf-8 -*-
"""
Created on Thu Feb 23 22:33:53 2017

@author: Naren Suri
"""

# Written for python version 2
from __future__ import print_function
import time
import os
from unidecode import unidecode
import sys 
#import collections
import string
#import importlib
from DataNode import Node



if sys.version[0] == '2':
    reload(sys)
    sys.setdefaultencoding("utf-8")
    
stack =[]    
    
def NodeCreation():
    #(self, parent, value=None,iscategory=None,wikiId=None,inLinks=None,outLinks=None,ispage=None):    
    root = Node(None,1,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    N2 = Node(root,2,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    N3 = Node(root,3,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    
    
    
    N6 = Node(N2,6,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    N7 = Node(N2,7,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    N8= Node(N2,8,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    
    N18 = Node(N6,18,iscategory=None,wikiId=10,inLinks=10,outLinks=10,ispage=1)
    N19 = Node(N6,19,iscategory=None,wikiId=10,inLinks=10,outLinks=10,ispage=1)
    N20 = Node(N6,20,iscategory=None,wikiId=10,inLinks=10,outLinks=10,ispage=1)
    
    N13 = Node(N7,13,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    N14 = Node(N8,14,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    
    N10= Node(N3,10,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
    N15 = Node(N10,15,iscategory=None,wikiId=10,inLinks=10,outLinks=10,ispage=1)
    
    return root
    

def traversalOfGeneratedTree(CurrentNode):
    print("Tree Traversal")
       
    if CurrentNode.isPage==1:
        stack.append(CurrentNode.value)
        return 1
    
    if CurrentNode.isCategory==1: 
        
        stack.append(CurrentNode.value)
        if len(CurrentNode.childList)>0:
            for Current in CurrentNode.childList:            
                toPrint  = traversalOfGeneratedTree(Current)
                if toPrint :
                    print(stack)
                stack.pop()
            #stack.pop()
        else:
            #print(stack)  # Not printing since the end is not a leaf / article
            #stack.pop()
            return 0
    
    
    
rootval = NodeCreation()
traversalOfGeneratedTree(rootval)    
    
    
    
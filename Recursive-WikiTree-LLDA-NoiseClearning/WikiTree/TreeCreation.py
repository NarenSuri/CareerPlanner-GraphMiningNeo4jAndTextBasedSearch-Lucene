# -*- coding: utf-8 -*-
"""
Created on Thu Feb 23 11:26:03 2017

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
import datetime
import codecs

import numpy.random as np


if sys.version[0] == '2':
    #reload(sys)
    #sys.setdefaultencoding("utf-8")
    print("This code is running on the correct version of python it supports.. 2.7")
    
    
#importlib.reload(sys)
#import sys  
#sys.setdefaultencoding('utf8')

# Create the first root node and send it to the every File call
# since i know what a root node is supposed to be, i use it
root = Node(None,"applied sciences",iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None) 
leafObjects={}
leafsWithValue = {}
pagesCouldntAssign=0
stack=[]
#levels = [1,2] # the level value and the leaf value will be saved
#fo = open("results.txt", "rw+")
Level1={}
levelVal = 15
minLengthOfCategories = 0 # be super careful with this parameter. Think and change it.
size = 55000
Level2={}
Level3={}
WikiSourceFilesdirectory = None
st = datetime.datetime.fromtimestamp(time.time()).strftime('%Y_%m_%d_%H_%M_%S')

def ProcessForTreeCreation(file_name,write_directory,replace_punctuation):
    global root
    global leafObjects
    global leafsWithValue
    with codecs.open(file_name, 'r', 'utf-8') as inf:
        #file_sample=open(file_name)
        line=inf.readline()    
        
        while line!="":
            # we dont need counts of occurances for Wiki data, as each subcategory or page is definitely going to be representing only one case.
            # Check for existance bbefore processing, because proessing and then checking in dictionary is costly in time
            key_words_path=unidecode(line).lower().strip().split("=>")
            
            for i in range(len(key_words_path)):
                #print i
                key_words_path[i] = key_words_path[i].translate(replace_punctuation)    
            
            
            
            
            #level1.add(key_words[2].translate(replace_punctuation))
            #level2.add(key_words[3].translate(replace_punctuation))
            #level3.add(key_words[4].translate(replace_punctuation))
    
            returnVal = insertToTree(key_words_path) 
            if returnVal == 0:
                print ("There is a problem in inserting node")
                break
            #else:
               # print ("Sucessfullly inserted record" + str(key_words_path))
            line=inf.readline()
            
        # Two important things i need at this stage are root node to track the tree created
        # the dictionary created for the leafs
        # now create the reverse mapping of the hash or dict 
        for key, value in leafObjects.iteritems():
            if leafsWithValue.has_key(value):
                leafsWithValue[value].append(key)
            else:
                leafsWithValue[value]= [key]
            

        
        
def processTheArticleLinkedWithCategoryFile():
    global pagesCouldntAssign
    global root
    #print ("Processing the Article File linked with categories")
    replace_punctuation = string.maketrans(string.punctuation, ' '*len(string.punctuation))
    #WikiSourceFilesdirectory = "D:/sem2/ir-project/finalData/wiki-dta/Applied_sciences/Applied_sciences/Applied_sciences/test/"
    ArticleCategorySourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/"    

    for filename in os.listdir(ArticleCategorySourceFilesdirectory):
        if filename.endswith(".txt") or filename.endswith(".TXT"): 
            #print(os.path.join(directory, filename))
            print ("Found File Name   :  "+filename)
            time1 = time.time()           
            
            ProcessArticleCategoryLinking(ArticleCategorySourceFilesdirectory+filename,replace_punctuation)
            print ("time taken for this particular file =",time.time()-time1)
        else:
            print ("Found File Name thats not either of the formats specified in the code")
    
    print ("Finished every thing, but couldnt find or assign these many articles :  " + str(pagesCouldntAssign))
    
    
def write_report(r, filename):
    print("Writing to the file")
    with codecs.open(filename, 'a', 'utf-8') as input_file:
        #input_file=open(filename, "a")
        for k, v in r.items():
            if len(set(v)) >= minLengthOfCategories:
                categories=""
                for categori in list(set(v)):
                    categories = categories+","+categori
                line = '{}, {}'.format(k, categories)
                print(line, file=input_file) 
            else:
                continue
        input_file.close()
        print("Writing finished")    



    
    

def traversalOfGeneratedTree(CurrentNode):
    #print("Tree Traversal")
    global levelVal  
    if CurrentNode.isPage==1:
        stack.append(CurrentNode.value)
        return 1
    
    if CurrentNode.isCategory==1: 
        
        stack.append(CurrentNode.value)
        if len(CurrentNode.childList)>0:
            for Current in CurrentNode.childList:            
                toPrint  = traversalOfGeneratedTree(Current)
                
                
                if toPrint :
                    #print(stack)
                    #for k in levels:
                        #fo.seek(0, 2)
                        #fo.write( str(stack[k]) + "," + str(stack[-1]))
                        # write levels data to dictinoary
                    
                    if len(stack)-1 >=levelVal: 
                        
                        if Level1.has_key(stack[-1]): 
                            #if not stack[levelVal] in Level1[stack[-1]]:                                
                            Level1[stack[-1]].append(stack[levelVal])  
                            #else:
                                #continue
                        else:
                            Level1[stack[-1]]=[stack[levelVal]]
                            
                    else:
                        
                        print ("Some Nodes are Missed because they dont have the level you asked for")
                        
                        
                stack.pop()
            #stack.pop()
        else:
            #print(stack)  # Not printing since the end is not a leaf / article
            #stack.pop()
            return 0
            

            

def ProcessArticleCategoryLinking(file_name,replace_punctuation):
    global root
    global leafsWithValue
    global pagesCouldntAssign
    with codecs.open(file_name, 'r', 'utf-8') as inf:
        #file_sample=open(file_name)
        line=""
        line=inf.readline()
        while line!="":
            for i in range(5):
                if line!="":
                    # we dont need counts of occurances for Wiki data, as each subcategory or page is definitely going to be representing only one case.
                    # Check for existance bbefore processing, because proessing and then checking in dictionary is costly in time
                    line = line.strip('\n')
                    if i==0:
                        wikiId = int(line)
                    
                    elif i==1:
                        value= unidecode(line).lower().translate(replace_punctuation)
                    
                    elif i==2:
                        inLinks = int(line)
                   
                    elif i==3:
                        categoriesList=unidecode(line).lower().strip().split("|")
                        for i in range(len(categoriesList)):
                            #print i
                            categoriesList[i] = categoriesList[i].translate(replace_punctuation)
                    
                    elif i==4: 
                        outLinksList=unidecode(line).lower().strip().split("|")
                        outLinks = len(outLinksList)
                    
                    elif i==5:
                        pass
                      
                    else:
                        print ("got some wrong number in case selection")
                else:
                    print ("Some problem in lines processing")
                line=inf.readline()     
            # now lets create the node and assign it to the relevant parents!!
            # first lets get the leaf category that is mapped and stored in the dictionary leafsWithValue    
            
            if inLinks >5:
                for category in categoriesList:        
                
                    if leafsWithValue.has_key(category):
                        NodeObj=None
                        NodeObj = leafsWithValue[category]
                        for i in range(len(NodeObj)):
                            Node(NodeObj[i],value,None,wikiId,inLinks,outLinks,1)
                    else:
                        #add missing categories to another dictionary
                        pagesCouldntAssign = pagesCouldntAssign+1
                
                
            line=inf.readline()
            
        
            
def insertToTree(key_words_path):
    global root
    #print ("Inserting in to the the Tree")
    #print (root.value)
    current = None
    if key_words_path[0]==root.value:
        current = root        
    else:
        print ("Root of the tree and the root node you passed are not same!!")
        return 0
    
    for i in range(len(key_words_path)-1):
        # check if first one is already there in one of the child nodes of the current node!!
        # if there continue; else create and add one
        if len(current.childList)>=1:
            flagMissed=0            
            for k in range(len(current.childList)):
                if key_words_path[i+1]==current.childList[k].value:
                    current = current.childList[k]
                    break
                            
                else:
                    flagMissed = flagMissed+1
                    
            if flagMissed == len(current.childList):
                newNodeCreated=None
                # create the new node and add to the childlist of the current node
                newNodeCreated = Node(current,key_words_path[i+1],iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
                
                if leafObjects.has_key(newNodeCreated):
                    continue
                else:
                    leafObjects[newNodeCreated]= key_words_path[i+1]
                    if leafObjects.has_key(current):
                        leafObjects.__delitem__(current)
                #current.childList.append(newNodeCreated)
                current = newNodeCreated 
                
        else:
            newNodeCreated= None
            # create the new node and add to the childlist of the current node
            newNodeCreated = Node(current,key_words_path[i+1],iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None)
            if leafObjects.has_key(newNodeCreated):
                continue
            else:
                leafObjects[newNodeCreated]= key_words_path[i+1]
                if leafObjects.has_key(current):
                    leafObjects.__delitem__(current)
            #current.childList.append(newNodeCreated)
            current = newNodeCreated   
    
    return 1
    
def write_RansomSample_report(r, input_file,key):     
    if len(set(r[key])) >= minLengthOfCategories:
        categories=""
        line=""
        for categori in list(set(r[key])):
            categories = categories+","+categori
            line = '{}, {}'.format(key, categories)
        print(line, file=input_file) 
    return input_file
    #print("Writing finished")  


    
def randomSampling(size,filename):
    print("Writing to the file")  
    with codecs.open(filename, 'a', 'utf-8') as input_file:
        #input_file=open(filename, "a") 
        KeysList = list(Level1.keys())
        if len(KeysList)<size:
            print("Returning all as you asked more than what is stored in the tree")
            size = len(KeysList)
        randomSampledKeys = list(set(np.random_integers(0,len(KeysList)-1,size)))
        #print(randomSampledKeys)
        print(len(KeysList))
        print(size)
        for randomKey in randomSampledKeys:
            input_file = write_RansomSample_report(Level1, input_file,KeysList[randomKey])
        input_file.close()
    
    
def startWikiWordsExtractions():
    global WikiSourceFilesdirectory
    replace_punctuation = string.maketrans(string.punctuation, ' '*len(string.punctuation))
    #WikiSourceFilesdirectory = "D:/sem2/ir-project/finalData/wiki-dta/Applied_sciences/Applied_sciences/Applied_sciences/test/"
    WikiSourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Procesing/"
    WikiKeywordsSplitsResult = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/"
    

    for filename in os.listdir(WikiSourceFilesdirectory):
        if filename.endswith(".txt") or filename.endswith(".TXT"): 
            #print(os.path.join(directory, filename))
            print ("Found File Name   :  "+filename)
            time1 = time.time()
            ProcessForTreeCreation(WikiSourceFilesdirectory+filename,WikiKeywordsSplitsResult,replace_punctuation)
            print ("time taken for this particular file tree creation is=",time.time()-time1)
        else:
            print ("Found File Name thats not either of the formats specified in the code")
          
    # once leafs objects and values map is created         
    processTheArticleLinkedWithCategoryFile()
    
    traversalOfGeneratedTree(root)
    print("Traversal Completed.......")
    write_report(Level1, WikiSourceFilesdirectory+str(st)+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal)+".csv")
    ##############################################################
        # Do random Sampling
    #samplingFileName = WikiSourceFilesdirectory+str(st)+"_RandomSampled_Result_Traversal_ForTopicModeling_Level"+str(levelVal)+"_size"+str(size)+".csv"
    #randomSampling(size,samplingFileName)
   ###############################################################
    

time1 = time.time()
print ("Started the Wiki Tree Creation In Memory" )
startWikiWordsExtractions()
print ("total time taken=",time.time()-time1)
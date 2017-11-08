# -*- coding: utf-8 -*-
"""
Created on Mon Apr 03 13:05:36 2017

@author: nsuri
"""

# Written for python version 2
from __future__ import print_function
from os import listdir
from os.path import isfile, join
import os

#if sys.version[0] == '2':
    #reload(sys)
    #sys.setdefaultencoding("utf-8")


AticlesPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/WikiPageData/articles/test/"
file_name= "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/articles/articles.txt"
renameFilesLocation = "C:/Users/nsuri/Desktop/IndependentStudy/Data/WikiPageData/articles/test/"


onlyfiles=[]
counter=0
printCounter = 0
fileNamesDictionary={}
dictionaryToSet=0
key=""
fileNamevalue=""

def startDataPreparation(output_file,articleName,onlyfiles):


    #articleName = articleName+".txt"
    findTheArticleAndGetProcessedContent(articleName+".txt",onlyfiles,output_file,articleName)
             

            
def findTheArticleAndGetProcessedContent(articleName,onlyfiles,output_file,pagename):
    foundFile=0
    for fileName in onlyfiles:
        if articleName.lower() == fileName.lower():
                foundFile=1
                break
        
    if foundFile==0:
        print("couldnt find the file :  " + str(pagename))
        lineTest = '{}'.format(str(pagename)) 
        #lineTest = ''.join([i if ord(i) < 128 else '' for i in lineTest]).replace("'","")         
        print(lineTest, file=output_file)

def reNamesFiles(renameFilesLocation):
    global AticlesPath
    for f in listdir(AticlesPath):
        if isfile(join(AticlesPath,f )) and f.lower() in fileNamesDictionary:
            
            os.rename(join(AticlesPath,f ),join(AticlesPath,str(fileNamesDictionary[f.lower()])+".txt"))
    
    

def ProcessArticleCategoryLinking(file_name):
    key=""
    global dictionaryToSet
    global AticlesPath
    for f in listdir(AticlesPath):
        if isfile(join(AticlesPath,f )):
            onlyfiles.append(f)
            
    file_sample=open(file_name)
    line=""
    line=file_sample.readline()
    Ccounter=0
    while line!="":
        #for i in range(5):
        if line!="":
            # we dont need counts of occurances for Wiki data, as each subcategory or page is definitely going to be representing only one case.
            # Check for existance bbefore processing, because proessing and then checking in dictionary is costly in time
            line = line.strip('\n')
            try:
                line = int(line)
                if dictionaryToSet==0 and Ccounter>2:
                    Ccounter=0
                    
                if Ccounter==0:
                    dictionaryToSet=1
                    key = line
                    Ccounter = Ccounter+1
                    line=file_sample.readline()
                    continue

                    #fileNamesDictionary[line]
                 
            except ValueError:
                   if Ccounter==1:
                       fileNamevalue = line
                       Ccounter = Ccounter+1
                       if key!="" and dictionaryToSet==1 and line not in fileNamesDictionary:                           
                           fileNamesDictionary[fileNamevalue.lower()+".txt"]= key
                           dictionaryToSet=0
                           key=None
                           fileNamevalue=None
                           line=file_sample.readline()
                           continue
                           
                         
                    
            try:       
                if Ccounter ==2:
                    line=file_sample.readline()
                    Ccounter = Ccounter+1
                    continue
                    
                else:
                    #print ("Some problem in lines processing")
                    line=file_sample.readline()
                    #line=file_sample.readline()
                            
                                     
                
                '''
                   if i==0:
                        if line!="":
                            pass
                        else:
                            line=file_sample.readline()
                        #wikiId = int(line)
                    
                    elif i==1:
                        if line!="":
                            fileNamevalue= line.lower()
                            startDataPreparation(output_file,fileNamevalue,onlyfiles)                        
                            pass
                        else:
                            line=file_sample.readline()                    
                       
                    
                    elif i==2:
                        if line!="":
                            pass
                        else:
                            line=file_sample.readline()
                   
                    elif i==3:
                        if line!="":
                            pass
                        else:
                            line=file_sample.readline()
                    
                    elif i==4: 
                        if line!="":
                            pass
                        else:
                            line=file_sample.readline()
                    
                    elif i==5:
                        if line!="":
                            pass
                        else:
                            line=file_sample.readline()
                
                    else:
                        print ("got some wrong number in case selection")
                '''       

                    
            except ValueError:
                print('An exception flew by!')
                print(ValueError)
        

                
                
                
def MappingWikiIdToWikiFileNamePreProcess(file_name):
    key=""
    global dictionaryToSet
    global AticlesPath
    for f in listdir(AticlesPath):
        if isfile(join(AticlesPath,f )):
            onlyfiles.append(f)
            
    file_sample=open(file_name)
    line=""
    line=file_sample.readline()
    Ccounter=0
    while line!="":
        #for i in range(5):
        if line!="":
            # we dont need counts of occurances for Wiki data, as each subcategory or page is definitely going to be representing only one case.
            # Check for existance bbefore processing, because proessing and then checking in dictionary is costly in time
            line = line.strip('\n')
            try:
                line = int(line)
                if dictionaryToSet==0 and Ccounter>2:
                    Ccounter=0
                    
                if Ccounter==0:
                    dictionaryToSet=1
                    key = line
                    Ccounter = Ccounter+1
                    line=file_sample.readline()
                    continue

                    #fileNamesDictionary[line]
                 
            except ValueError:
                   if Ccounter==1:
                       fileNamevalue = line
                       Ccounter = Ccounter+1
                       if key!="" and dictionaryToSet==1 and line not in fileNamesDictionary:                           
                           fileNamesDictionary[fileNamevalue.lower()+".txt"]= key
                           dictionaryToSet=0
                           key=None
                           fileNamevalue=None
                           line=file_sample.readline()
                           continue
                           
                         
                    
            try:       
                if Ccounter ==2:
                    line=file_sample.readline()
                    Ccounter = Ccounter+1
                    continue
                    
                else:
                    #print ("Some problem in lines processing")
                    line=file_sample.readline()
                    #line=file_sample.readline()
                            


                    
            except ValueError:
                print('An exception flew by!')
                print(ValueError)
                
    return fileNamesDictionary
                
                

try:
    print("Writing to the file")     
    ProcessArticleCategoryLinking(file_name)
    reNamesFiles(renameFilesLocation)
except ValueError:
    print('An exception flew by!')
    print(ValueError)
finally:
   print("Completed")

    

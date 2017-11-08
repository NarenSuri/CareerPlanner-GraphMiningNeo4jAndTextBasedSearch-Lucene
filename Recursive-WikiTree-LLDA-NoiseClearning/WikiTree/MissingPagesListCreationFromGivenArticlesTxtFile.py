# -*- coding: utf-8 -*-
"""
Created on Mon Mar 27 12:26:00 2017

@author: nsuri
"""
# Written for python version 2
from __future__ import print_function
from os import listdir
from os.path import isfile, join

import sys 


#if sys.version[0] == '2':
    #reload(sys)
    #sys.setdefaultencoding("utf-8")


AticlesPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/WikiPageData/articles/articles/"
outputFilename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/WikiMissingCategoryChildPages.txt"
file_name= "C:/Users/nsuri/Desktop/IndependentStudy/NewData/articles.txt"
onlyfiles=[]
counter=0
printCounter = 0

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


    

def ProcessArticleCategoryLinking(file_name):
    
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
                int(line)
                Ccounter = Ccounter+1
            except ValueError:
                   if Ccounter==1:
                       fileNamevalue = line
                    
                    
            if Ccounter ==2:
                fileNamevalue= fileNamevalue.lower()
                if fileNamevalue =="":
                    Ccounter =0 
                    #pass
                else:                    
                    startDataPreparation(output_file,fileNamevalue,onlyfiles)
                    Ccounter =0 
                                         
                                     
                
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
        else:
            print ("Some problem in lines processing")
        line=file_sample.readline()
        #line=file_sample.readline()

try:
    print("Writing to the file")    
    output_file=open(outputFilename, "a")        
    ProcessArticleCategoryLinking(file_name)
except ValueError:
    print('An exception flew by!')
finally:
    output_file.close()

    

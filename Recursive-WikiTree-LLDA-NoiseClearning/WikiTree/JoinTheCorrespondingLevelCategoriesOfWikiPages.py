# -*- coding: utf-8 -*-
"""
Created on Fri Apr 28 21:09:42 2017

@author: nsuri
"""

# join multiple level files into single file to join the pages and their categories at a given level

from __future__ import print_function
import os
#import importlib
import codecs
import csv

PageAndCategoryMappingDict = {}
##########################################################################
FilesPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/TreeTraversalResults/Level_9/"
ResultsPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/"
##########################################################################

def writeToTheLevelFinalFile(fileName):
    global PageAndCategoryMappingDict
    with open(fileName, 'wb') as outputFile:
        writer = csv.writer(outputFile)
        for DictKey,DictVlaueList in PageAndCategoryMappingDict.iteritems():
            DictValues=[DictKey]
            for val in set(DictVlaueList):
                DictValues.append(val)
            writer.writerow(DictValues)

    
    
def createSetDictionary(fileName):
    global PageAndCategoryMappingDict
    with codecs.open(fileName, 'r', 'utf-8') as csvFile:
        FileReader =csv.reader(csvFile, delimiter=',')
        for row in FileReader:
           if row[0]!=" " and row[0]!="":
               if PageAndCategoryMappingDict.has_key(row[0]):
                   for k in range(2,len(row)):
                       PageAndCategoryMappingDict[row[0]].append(row[k])
               else:
                   PageAndCategoryMappingDict[row[0]] = [row[2]]
                   for k in range(3,len(row)):
                       PageAndCategoryMappingDict[row[0]].append(row[k])                   
    
def startJoiningTheFiles():
    global FilesPath
    for filename in os.listdir(FilesPath):
        if filename.endswith(".csv") or filename.endswith(".CSV"): 
            #print(os.path.join(directory, filename))
            print ("Found File Name   :  "+filename) 
            
            createSetDictionary(FilesPath+filename)
    newFileName = filename.split("_")
    OutputFile="Final"
    for x in range(2,len(newFileName)):
        OutputFile = OutputFile+"_"+newFileName[x]
    writeToTheLevelFinalFile(ResultsPath+OutputFile)
    
    
startJoiningTheFiles()
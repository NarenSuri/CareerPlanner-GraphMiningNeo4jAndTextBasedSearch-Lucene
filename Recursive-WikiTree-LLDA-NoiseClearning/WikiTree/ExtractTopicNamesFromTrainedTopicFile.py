# -*- coding: utf-8 -*-
"""
Created on Mon May 01 00:03:22 2017

@author: nsuri
"""
import codecs
category=[]
# extracting the topics names from the trained model output file
file_name = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/v1-TopicModelingInputFiles/WikiTopicModel-llda-took-1hour.keys"
with codecs.open(file_name, 'r', 'utf-8') as inf:
    line=inf.readline()
    while line!="":
        category.append(line.split()[1])
        line=inf.readline()
        
# write to file
write_file_name = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/v1-TopicModelingInputFiles/CategoryList.txt"
with codecs.open(write_file_name,  'a', 'utf-8') as fileout:
    count=0
    for i in range(len(category)):
        count = count+1
        line = '{} {} \n'.format(str(count), category[i])
        fileout.write(line)
        #print("Hello")

            

    
    
    
    
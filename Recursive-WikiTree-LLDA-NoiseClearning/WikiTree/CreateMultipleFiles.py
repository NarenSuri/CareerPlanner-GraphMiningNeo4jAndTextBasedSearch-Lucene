# -*- coding: utf-8 -*-
"""
Created on Thu Apr 27 22:21:54 2017

@author: nsuri
"""
#filename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/articles.txt"
filename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/PrepareForTopicModelingInput/Final_Result_Traversal_ForTopicModeling_Level15.csv"
startNewFile=1
fileNumber=0
linesTosaveInEachFile=180000
#linesTosaveInEachFile=480000
#linesTosaveInEachFile=1440000
lineNumber=0

with open(filename) as fp:
    
    for line in fp:
        if startNewFile==1:
            fileNumber = fileNumber+1
            #f = open('C:/Users/nsuri/Desktop/IndependentStudy/Data/article/myfile'+str(fileNumber)+".txt", 'w')
            f = open('C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/PrepareForTopicModelingInput/myfile'+str(fileNumber)+".csv", 'w')
            startNewFile=0
            
        lineNumber= lineNumber+1
        if lineNumber%linesTosaveInEachFile==0:
            startNewFile=1
            f.write(line)
            f.close()
            lineNumber=0
        else:
            f.write(line)  # python will convert /n to os.linesep
f.close()# last split file to be closed
fp.close()  # you can omit in most cases as the destructor will call it
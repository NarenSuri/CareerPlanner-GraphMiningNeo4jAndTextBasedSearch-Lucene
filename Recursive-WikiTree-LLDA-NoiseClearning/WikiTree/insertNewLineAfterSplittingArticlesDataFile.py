# -*- coding: utf-8 -*-
"""
Created on Thu Apr 27 16:00:06 2017

@author: nsuri
"""

import fileinput
file_path = "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/SplitFiles/test/file01.txt"
counter =0
for line in fileinput.FileInput(file_path,inplace=1):
    counter = counter+1    
    if counter ==50000 :
        line=line.replace(line,line+"\n")
        counter=0
    print line,
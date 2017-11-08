# -*- coding: utf-8 -*-
"""
Created on Fri Mar 10 19:08:20 2017

@author: nsuri
"""

# creating a file to ask for the all pages belongs to these categorties

# get all last categories from the category files

# Written for python version 2
from __future__ import print_function
import time
import os
from unidecode import unidecode
import sys 
#import collections
import string
import numpy.random as np


if sys.version[0] == '2':
    reload(sys)
    sys.setdefaultencoding("utf-8")
    
WikiSourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Procesing/"
WikiKeywordsSplitsResult = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/NeedLeafPagesOfTheseLeafCategories.txt"

def ProcessForLeafCategoriesExtraction(file_name,write_directory,replace_punctuation,output_file_categories):
    
    file_sample=open(file_name)
     
    line=file_sample.readline()
    while line!="":
        # we dont need counts of occurances for Wiki data, as each subcategory or page is definitely going to be representing only one case.
        # Check for existance bbefore processing, because proessing and then checking in dictionary is costly in time
        key_words_path=(line).strip().split("=>")        
        line = '{}'.format(key_words_path[len(key_words_path)-1]) 
        #line = ''.join([i if ord(i) < 128 else '' for i in line]).replace("'","") 
        print(line, file=output_file_categories)
        line=file_sample.readline() 
   
    
def startLeafCategoriesExtractions(output_file_categories):
    global WikiSourceFilesdirectory
    replace_punctuation = string.maketrans(string.punctuation, ' '*len(string.punctuation))
    #WikiSourceFilesdirectory = "D:/sem2/ir-project/finalData/wiki-dta/Applied_sciences/Applied_sciences/Applied_sciences/test/"

    for filename in os.listdir(WikiSourceFilesdirectory):
        if filename.endswith(".txt") or filename.endswith(".TXT"): 
            #print(os.path.join(directory, filename))
            print ("Found File Name   :  "+filename)
            time1 = time.time()   
            
            
            ProcessForLeafCategoriesExtraction(WikiSourceFilesdirectory+filename,WikiKeywordsSplitsResult,replace_punctuation,output_file_categories)
            print ("time taken for this particular file =",time.time()-time1)
        else:
            print ("Found File Name thats not either of the formats specified in the code")

try: 
    print("Writing to the file")    
    output_file_categories=open(WikiKeywordsSplitsResult, "a")            
    startLeafCategoriesExtractions(output_file_categories)
except NameError:
    print('An exception flew by!')
    raise 
finally:
    output_file_categories.close()

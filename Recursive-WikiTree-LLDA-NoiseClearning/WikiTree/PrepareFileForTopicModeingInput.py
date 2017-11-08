# -*- coding: utf-8 -*-
"""
Created on Fri Mar 10 01:57:25 2017

@author: Naren Suri
"""

# Creating the files to test the topic modeling from the pages and the categories data extracted from the trees generated

# got a pages and the categoies related to it page. Now lets create a data format that can be fed into the python topic modeling code
# labled - lda imput data prepaation.

# load the pages and categories fle in to the pandas

from __future__ import print_function
import pandas as panda
from os import listdir
from os.path import isfile, join
from unidecode import unidecode
import string
from nltk.corpus import stopwords

# load the excel sheet
path ="C:/Users/nsuri/Desktop/IndependentStudy/wikiTreeResults/"
FileName = "2017_03_02_16_26_26_RandomSampled_Result_Traversal_ForTopicModeling_Level2_size55000.xlsx"
#FileName = "DataAnalyticsLearningProject_AnonymizedToShare_AnonymizedAndGeoCoded.xlsx"
excelsheet = panda.ExcelFile(path+FileName)
print(excelsheet.sheet_names)
print(excelsheet.sheet_names[0])
loadedDataDf = excelsheet.parse(excelsheet.sheet_names[0])

AticlesPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/WikiPageData/articles/articles/"
outputFilename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/TopicModelInput.txt"
missingFilename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/MissingPages.txt"
onlyfiles=[]


def findTheArticleAndGetProcessedContent(articleName,onlyfiles,notFound_files):
    foundFile=0
    result=""
    for fileName in onlyfiles:
        if articleName.lower() == fileName.lower():
            with open(join(AticlesPath, fileName), 'r',encoding="utf8") as myfile:
                data=myfile.read()
                foundFile=1
                data = (" ".join(data.split())).replace(":"," ")
                words_list = ((data.translate(str.maketrans('','',string.punctuation))).lower()).split()
                data=""
                for word in words_list:
                    if word not in stopwords.words('english'):
                        result = result + " "+ word
                return result
        
    if foundFile==0:
        print("couldnt find the file :  " + str(articleName))
        lineTest = '{}'.format(str(articleName)) 
        #lineTest = ''.join([i if ord(i) < 128 else '' for i in lineTest]).replace("'","")         
        print(lineTest, file=notFound_files)
        return ""

def writeToFile(labels,pageContent,output_file): 
    line = '{} {}'.format(labels, pageContent) 
    line = ''.join([i if ord(i) < 128 else '' for i in line]).replace("'","") 
    print(line, file=output_file)
    
    
    
    
def startDataPreparation(output_file,notFound_files):
    for f in listdir(AticlesPath):
        if isfile(join(AticlesPath,f )):
            onlyfiles.append(f)
    ## Now get the each record
    for row in loadedDataDf.itertuples():
        labels=[]
        isWrite=0
        pageContent=""
        #print row[loadedDataDf.columns.get_loc('PageName')+1]
        #print row
 
        for CurrentCol in list(range(len(row)-1)):
            if CurrentCol == 0:
                continue
            else:
                if CurrentCol == 1:
                    articleName = unidecode(str(row[CurrentCol]))
                    articleName = articleName+".txt"
                    pageContent = findTheArticleAndGetProcessedContent(articleName,onlyfiles,notFound_files)
                    
                    if pageContent is not "" and pageContent is not None and pageContent== pageContent :
                        isWrite=1
                        continue
                    else:
                        isWrite=0
                        break
                    
                else:
                    if row[CurrentCol] is not None and row[CurrentCol]== row[CurrentCol] :
                        labels.append(unidecode(str(row[CurrentCol])))
        if isWrite:
            writeToFile(labels,pageContent,output_file)
                
            

try: 
    print("Writing to the file")    
    output_file=open(outputFilename, "a")
    notFound_files=open(missingFilename, "a")            
    startDataPreparation(output_file,notFound_files)
except NameError:
    print('An exception flew by!')
    raise 
finally:
    output_file.close()
    notFound_files.close()


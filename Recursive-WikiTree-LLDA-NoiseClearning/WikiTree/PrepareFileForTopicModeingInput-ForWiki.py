# -*- coding: utf-8 -*-
"""
Created on Fri Mar 10 01:57:25 2017

@author: Naren Suri
"""

# Creating the files to test the topic modeling from the pages and the categories data extracted from the trees generated

# got a page and the categoies related to it page. Now lets create a data format that can be fed into the mallet topic modeling code
# labled - lda imput data prepaation.

# load the pages and categories fle in to the pandas

from __future__ import print_function
import pandas as panda
from os import listdir
from os.path import isfile, join
from unidecode import unidecode
import string
from nltk.corpus import stopwords
import codecs
from nltk.stem import WordNetLemmatizer
wordnet_lemmatizer = WordNetLemmatizer()

#import RenamingFilesWithWikiIdToWikiName as rfw
#########################################################################################################
# load the excel sheet
path ="C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/PrepareForTopicModelingInput/split-v1/"

FileName = "myfile1.xlsx"
#FileName = "Final_Result_Traversal_ForTopicModeling_Level15_test.xlsx"

#AticlesPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/WikiPageData/articles/test/"
AticlesPath = "C:/Users/nsuri/Desktop/IndependentStudy/Data/WikiPageData/articles/wiki_articles/"

file_nameForWikiIdMapping= "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/SplitFiles/long-v2/myfile2.txt"  ######### change this
#file_nameForWikiIdMapping= "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/articles/articles.txt"

outputFilename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/WikiTopicModelInput_1.txt"
missingFilename = "C:/Users/nsuri/Desktop/IndependentStudy/Data/ProcessWikiDataForTopicModeling/WikiMissingPages_1.txt"

########################################################################################################

excelsheet = panda.ExcelFile(path+FileName)
print(excelsheet.sheet_names)
print(excelsheet.sheet_names[0])
loadedDataDf = excelsheet.parse(excelsheet.sheet_names[0])
onlyfiles=[]


onlyfiles=[]
counter=0
countFindMatchedWikiIDFiles=0



fileNamesDictionary={}
dictionaryToSet=0
replace_punctuation = string.maketrans(string.punctuation, '-'*len(string.punctuation))


def findTheArticleAndGetProcessedContent(articleName,onlyfiles,notFound_files):
    global replace_punctuation
    foundFile=0
    result=""
    if articleName in onlyfiles:
        #if articleName.lower() == fileName.lower():
        with codecs.open(AticlesPath+"/"+ articleName, 'r',"utf-8") as myfile:
            data=myfile.read()
            foundFile=1
            data = unidecode((" ".join(data.split())).replace(":"," "))
            #data=  ''.join([i if ord(i) < 128 else ' ' for i in data])
            #data = data.encode('ascii')
            words_list = data.translate(replace_punctuation).lower().split()
            data=""
            for word in words_list:
                if word not in stopwords.words('english'):
                    result = result + " "+ wordnet_lemmatizer.lemmatize(word)
            return result
        
    if foundFile==0:
        #print("couldnt find the file :  " + str(articleName))
        lineTest = '{}'.format(str(articleName)) 
        ##lineTest = ''.join([i if ord(i) < 128 else '' for i in lineTest]).replace("'","")    #- we dont need this     
        #print(lineTest, file=notFound_files) # enable this if u want to save the results to a file
        return ""

def writeToFile(counter,labelsList,pageContent,output_file): 
    
    labels = " ".join(str(x).replace(" ","_") for x in labelsList)
    labels = ''.join([i if ord(i) < 128 else '' for i in labels]).replace("'","") 
    pageContent = ''.join([i if ord(i) < 128 else '' for i in pageContent]).replace("'","") 
    
    line = '{}\t{}\t{}'.format(counter,labels, pageContent) 
    print(line, file=output_file)
    
def MappingWikiIDAndFileNames():
    global fileNamesDictionary
    global file_nameForWikiIdMapping
    #fileNamesDictionary = rfw.MappingWikiIdToWikiFileNamePreProcess(file_nameForWikiIdMapping)
    fileNamesDictionary = MappingWikiIdToWikiFileNamePreProcess(file_nameForWikiIdMapping)
    
def mapToWikiId(articleName):
    # mapping to the wiki ID
    global fileNamesDictionary
    global countFindMatchedWikiIDFiles
    if fileNamesDictionary.has_key(articleName):
        return fileNamesDictionary[articleName]
    else:
        countFindMatchedWikiIDFiles = countFindMatchedWikiIDFiles+1        
    return "NoFileFound"
    
    
def startDataPreparation(output_file,notFound_files):
    global fileNamesDictionary
    global loadedDataDf
    global onlyfiles
    '''for f in listdir(AticlesPath):
        if isfile(join(AticlesPath,f )):
            onlyfiles.append(f)
    '''
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
                    missing= articleName
                    articleName = mapToWikiId(articleName)                    
                    if articleName=="NoFileFound":
                        #print("couldnt find the file to map to wiki id "+str(missing))
                        missing=""
                        break
                    else:
                        articleName = str(articleName)+".txt"
                        pageContent = findTheArticleAndGetProcessedContent(articleName.lower(),onlyfiles,notFound_files)
                    
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
            global counter
            counter = counter+1
            writeToFile(counter,labels,pageContent,output_file)
                


def MappingWikiIdToWikiFileNamePreProcess(file_name):
    
    key=""
    global dictionaryToSet
    global AticlesPath
    for f in listdir(AticlesPath):
        if isfile(join(AticlesPath,f )):
            onlyfiles.append(f)
            
    with codecs.open(file_name, 'r', 'utf-8') as file_sample:
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
                               fileNamevalue = str.strip(str.rstrip(unidecode(fileNamevalue).lower().translate(replace_punctuation)))
                               fileNamesDictionary[fileNamevalue.lower()]= key
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
    print("Working on it....")    
    output_file=open(outputFilename, "a")
    notFound_files=open(missingFilename, "a") 
    MappingWikiIDAndFileNames()           
    startDataPreparation(output_file,notFound_files)
except NameError:
    print('An exception flew by!')
    raise 
finally:
    print ("Total missing Wiki ID , page files names missing  :  " + str(countFindMatchedWikiIDFiles))
    output_file.close()
    notFound_files.close()


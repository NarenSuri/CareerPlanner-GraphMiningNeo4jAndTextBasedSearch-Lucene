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
import warnings
import numpy.random as np
lineNUmber=0


if sys.version[0] == '2':
    #reload(sys)
    #sys.setdefaultencoding("utf-8")
    print("This code is running on the correct version of python it supports.. 2.7")
    
##############################################################################################################   
RootNodeValue = "Applied sciences".lower()
#RootNodeValue = "r".lower()

#WikiSourceFilesdirectory = "D:/sem2/ir-project/finalData/wiki-dta/Applied_sciences/Applied_sciences/Applied_sciences/test/"


#ArticleCategorySourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/test/" 
ArticleCategorySourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/article/SplitFiles/ToProcess/"

#WikiSourceFilesdirectory = "D:/sem2/ir-project/finalData/wiki-dta/Applied_sciences/Applied_sciences/Applied_sciences/test/"

#WikiSourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Procesing/test/"
WikiSourceFilesdirectory = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Procesing/"


WikiKeywordsSplitsResult = "C:/Users/nsuri/Desktop/IndependentStudy/Data/Category/Processed_Data/"

#levels = [1,2] # the level value and the leaf value will be saved
#fo = open("results.txt", "rw+")
Level1={}
levelVal = [5,9,11,14,17,20,22,25]
inLinksLimit=0
#############################################################################################################

#importlib.reload(sys)
#import sys  
#sys.setdefaultencoding('utf8')

# Create the first root node and send it to the every File call
# since i know what a root node is supposed to be, i use it
#root = Node(None,"applied sciences",iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None) 
root = Node(None,RootNodeValue,iscategory=1,wikiId=None,inLinks=None,outLinks=None,ispage=None) 
missingCategories = {}
#missingCategories[RootNodeValue] = [root]
leafObjects={}
leafsWithValue = {}
pagesCouldntAssign=0
stack=[]
SplitFileArticleName=""

minLengthOfCategories = 0 # be super careful with this parameter. Think and change it.
size = 55000
Level2={}
Level3={}
Level4={}
Level5={}
Level6={}
Level7={}
Level8={}
DontHaveLevelsYouAskedFor =0


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
            
            #key_words_path=unidecode(line).lower().strip().split("=>")
            key_words_path=unidecode(line).lower().strip().split("=>")
            
            for i in range(len(key_words_path)):
                #print i
                key_words_path[i] = str.strip(str.rstrip(key_words_path[i].translate(replace_punctuation))).strip("\n")    
            
            
            
            
            #level1.add(key_words[2].translate(replace_punctuation))
            #level2.add(key_words[3].translate(replace_punctuation))
            #level3.add(key_words[4].translate(replace_punctuation))
    
            returnVal = insertToTree(key_words_path) 
            if returnVal == 0:
                print ("There is a problem in inserting node")
                #warnings.warn("There is a problem in inserting node", DeprecationWarning)
                break
            #else:
               # print ("Sucessfullly inserted record" + str(key_words_path))
            line=inf.readline()
            
        # Two important things i need at this stage are root node to track the tree created
        # the dictionary created for the leafs
        # now create the reverse mapping of the hash or dict 
        for key, value in leafObjects.iteritems():
            if leafsWithValue.has_key(value):
                if key in leafsWithValue[value]:
                    continue
                else:
                    leafsWithValue[value].append(key)
            else:
                leafsWithValue[value]= [key]
            

        
        
def processTheArticleLinkedWithCategoryFile():
    global pagesCouldntAssign
    global root
    global SplitFileArticleName
    global ArticleCategorySourceFilesdirectory
    #print ("Processing the Article File linked with categories")
    replace_punctuation = string.maketrans(string.punctuation, ' '*len(string.punctuation))


    for filename in os.listdir(ArticleCategorySourceFilesdirectory):
        if filename.endswith(".txt") or filename.endswith(".TXT"): 
            #print(os.path.join(directory, filename))
            print ("Found File Name   :  "+filename)
            #warnings.warn("Found File Name   :  "+filename, DeprecationWarning)
            time1 = time.time()           
            SplitFileArticleName = filename
            ProcessArticleCategoryLinking(ArticleCategorySourceFilesdirectory+filename,replace_punctuation)
            print ("time taken for this particular file =",time.time()-time1)
            #warnings.warn("time taken for this particular file =",time.time()-time1, DeprecationWarning)
        else:
            print ("Found File Name thats not either of the formats specified in the code")
            #warnings.warn("Found File Name thats not either of the formats specified in the code")
    print ("Finished every thing, but couldnt find or assign these many articles :  " + str(pagesCouldntAssign))
    #warnings.warn("Finished every thing, but couldnt find or assign these many articles :  " + str(pagesCouldntAssign))
    
def write_report(r, filename):
    print("Writing to the file")
    #warnings.warn("Writing to the file")
    with codecs.open(filename, 'a', 'utf-8') as input_file:
        #input_file=open(filename, "a")
        for k, v in r.items():
            if len(set(v)) >= minLengthOfCategories:
                categories=""
                for categori in list(set(v)):
                    categories = categories+","+categori
                line = '{}, {}'.format(str.strip(str.rstrip(k)), str.strip(str.rstrip(categories)))
                print(line, file=input_file) 
            else:
                continue
        input_file.close()
        print("Writing finished") 
        #warnings.warn("Writing finished")



    
    

def traversalOfGeneratedTree(CurrentNode):
    #print("Tree Traversal")
    global levelVal 
    global Level1
    global Level2
    global Level3
    global Level4
    global Level5
    global Level6
    global Level7
    global Level8
    global DontHaveLevelsYouAskedFor
    
    #if CurrentNode.value=="c8":
        #print("start debug here")
        
    if CurrentNode.isPage==1:
        stack.append(CurrentNode.value)
        return 1
    
    if CurrentNode.isCategory==1:         
        stack.append(CurrentNode.value)
    for Current in CurrentNode.childList:
        #toPrint=0
        toPrint  = traversalOfGeneratedTree(Current)                
        #'''
        if toPrint :      
                    # write levels data to dictinoary                    
                if len(stack)-1 > levelVal[0] and len(levelVal)>0: ### look at this later  change to > to get exactly at that level                        
                    if Level1.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level1[stack[-1]].append(stack[levelVal[0]])  
                        #else:
                        #continue
                    else:
                        Level1[stack[-1]]=[stack[levelVal[0]]]                          
                
                # write levels data to dictinoary                    
                if len(stack)-1 > levelVal[1] and len(levelVal)>1: ### look at this later  change to > to get exactly at that level                        
                    if Level2.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level2[stack[-1]].append(stack[levelVal[1]])  
                        #else:
                        #continue
                    else:
                        Level2[stack[-1]]=[stack[levelVal[1]]]  


                if len(stack)-1 > levelVal[2] and len(levelVal)>2: ### look at this later  change to > to get exactly at that level                        
                    if Level3.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level3[stack[-1]].append(stack[levelVal[2]])  
                        #else:
                        #continue
                    else:
                        Level3[stack[-1]]=[stack[levelVal[2]]]


                if len(stack)-1 > levelVal[3] and len(levelVal)>3: ### look at this later  change to > to get exactly at that level                        
                    if Level4.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level4[stack[-1]].append(stack[levelVal[3]])  
                        #else:
                        #continue
                    else:
                        Level4[stack[-1]]=[stack[levelVal[3]]]


                if len(stack)-1 > levelVal[4] and len(levelVal)>4: ### look at this later  change to > to get exactly at that level                        
                    if Level5.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level5[stack[-1]].append(stack[levelVal[4]])  
                        #else:
                        #continue
                    else:
                        Level5[stack[-1]]=[stack[levelVal[4]]]


                if len(stack)-1 > levelVal[5] and len(levelVal)>5: ### look at this later  change to > to get exactly at that level                        
                    if Level6.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level6[stack[-1]].append(stack[levelVal[5]])  
                        #else:
                        #continue
                    else:
                        Level6[stack[-1]]=[stack[levelVal[5]]]


                if len(stack)-1 > levelVal[6] and len(levelVal)>6: ### look at this later  change to > to get exactly at that level                        
                    if Level7.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level7[stack[-1]].append(stack[levelVal[6]])  
                        #else:
                        #continue
                    else:
                        Level7[stack[-1]]=[stack[levelVal[6]]]  
                        
                if len(stack)-1 > levelVal[7]  and len(levelVal)>7: ### look at this later  change to > to get exactly at that level                        
                    if Level8.has_key(stack[-1]): 
                        #if not stack[levelVal] in Level1[stack[-1]]:                                                            
                        Level8[stack[-1]].append(stack[levelVal[7]])  
                        #else:
                        #continue
                    else:
                        Level8[stack[-1]]=[stack[levelVal[7]]]                      

                     
                stack.pop()
                ## this ends the if print loop                    
            # for loop still works here 
    #after for loop
    stack.pop()
    return 0
            

def ProcessArticleCategoryLinking(file_name,replace_punctuation):
    global root
    global leafsWithValue
    global pagesCouldntAssign
    global inLinksLimit
    global lineNUmber
    try:
        with codecs.open(file_name, 'r', 'utf-8') as inf:
            #file_sample=open(file_name)
            line=""
            line=inf.readline()
            lineNUmber = lineNUmber+1
            while line!="":
                for i in range(5):
                    if line!="":
                        # we dont need counts of occurances for Wiki data, as each subcategory or page is definitely going to be representing only one case.
                        # Check for existance bbefore processing, because proessing and then checking in dictionary is costly in time
                        line = line.strip('\n')
                        if i==0:
                            wikiId = int(line)
                        
                        elif i==1:
                            value= str.strip(str.rstrip(unidecode(line).lower().translate(replace_punctuation)))
                        
                        elif i==2:
                            inLinks = int(line)
                       
                        elif i==3:
                            categoriesList=unidecode(line).lower().strip().split("|")
                            for i in range(len(categoriesList)):
                                #print i
                                categoriesList[i] = str.strip(str.rstrip(categoriesList[i].translate(replace_punctuation)))
                        
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
                    lineNUmber=lineNUmber+1
                # now lets create the node and assign it to the relevant parents!!
                # first lets get the leaf category that is mapped and stored in the dictionary leafsWithValue    
                
                if inLinks >= inLinksLimit:
                    for category in categoriesList:  
                        
                    
                        if leafsWithValue.has_key(category):
                            NodeObj=None
                            NodeObj = leafsWithValue[category]
                            for i in range(len(NodeObj)):
                                Node(NodeObj[i],value,None,wikiId,inLinks,outLinks,1)
                        else:
                            #add missing categories to another dictionary
                            if missingCategories.has_key(category):
                                missingCategories[category]. append(Node(None,value,None,wikiId,inLinks,outLinks,1))
                                
                            else:
                                #missingCategories[].append(category)
                                missingCategories[category] =  [Node(None,value,None,wikiId,inLinks,outLinks,1)]                          
                            
                            pagesCouldntAssign = pagesCouldntAssign+1
                    
                    
                line=inf.readline()
                lineNUmber=lineNUmber+1
                
    except(RuntimeError, TypeError, NameError):
        print("problem with line number " + str(lineNUmber))
        #warnings.warn("problem with line number " + str(lineNUmber))    
        
        
        
def TraverseForMissingCategories(currentNode):
     # Now find and insert the missing pages at leaf -1 category's. Traverse the tree and keep inseting the pages to the categories those not in the leaf's
    global missingCategories
    global missedPagesAtSecondParseOfTree
    # lets do the traversal by the DFS and check for missed pages assignment to categories. Find those categories and assign the pages with no parent to that category
    for Currentchild in currentNode.childList:
        if Currentchild.isPage == 1:
            return 1
        else:
            # check if its is already there in the dictionary
            if missingCategories.has_key(Currentchild.value):
                for pagesNode in missingCategories[Currentchild.value]:
                    pagesNode.parent = Currentchild
                    #Currentchild.childPageList.append(pagesNode) # difficult to recursively track if we use 2 lists - one for page and one for category
                    Currentchild.childList.append(pagesNode)
                missingCategories.pop(Currentchild.value)
                    
            else:
                pass
                
        TraverseForMissingCategories(Currentchild)
         
                
        

       
            
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
            isPresent=0
            for k in range(len(current.childList)):
                #if current.childList[k].value == "c2":
                    #print("Debug from here")
                if key_words_path[i+1]==current.childList[k].value:
                    current = current.childList[k]
                    isPresent=1
                    break
                            
                else:
                    flagMissed = flagMissed+1
            
            if isPresent==1:
                isPresent=0
                continue

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
    global WikiKeywordsSplitsResult
    global missingCategories
    replace_punctuation = string.maketrans(string.punctuation, ' '*len(string.punctuation))

    

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
    
    #############################################
    # Now find and insert the missing pages at leaf -1 category's. Traverse the tree and keep inseting the pages to the categories those not in the leaf's
    # before we do that lets free up the space of the  leafsWithValue dictionary which could be huge and hence free the ram
    global leafsWithValue
    leafsWithValue = []
    # Insert Missing Pages To The Categories between Root To Leaf Category
    print(" The total number of categories missed while searching the leaf categories of the tree : " + str(len(missingCategories)))    
    TraverseForMissingCategories(root)
    print(" The total number of categories missed while searching entire tree to insert are : " + str(len(missingCategories)))
    #############################################

    ## Now traverse the final tree to generate the results
    traversalOfGeneratedTree(root)
    
    st=""
    #print ("Some Nodes are Missed because they dont have the level you asked for  " +str(DontHaveLevelsYouAskedFor))
    print("Traversal Completed.......")
    print("Writing Location : " + WikiKeywordsSplitsResult )
    #for k in len(levelVal):
    write_report(Level1, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[0])+".csv")
    write_report(Level2, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[1])+".csv")
    write_report(Level3, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[2])+".csv")
    write_report(Level4, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[3])+".csv")
    write_report(Level5, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[4])+".csv")
    write_report(Level6, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[5])+".csv")
    write_report(Level7, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[6])+".csv")
    write_report(Level8, WikiKeywordsSplitsResult+str(st)+"_"+SplitFileArticleName+"_Result_Traversal_ForTopicModeling_Level"+str(levelVal[7])+".csv")
    
    

    ##############################################################
        # Do random Sampling
    #samplingFileName = WikiSourceFilesdirectory+str(st)+"_RandomSampled_Result_Traversal_ForTopicModeling_Level"+str(levelVal)+"_size"+str(size)+".csv"
    #randomSampling(size,samplingFileName)
   ###############################################################
    

time1 = time.time()
print ("Started the Wiki Tree Creation In Memory" )
startWikiWordsExtractions()
print ("total time taken=",time.time()-time1)
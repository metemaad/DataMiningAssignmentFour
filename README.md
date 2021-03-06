# DataMiningAssignmentFour
        README File for Decision Tree ID3

 What is it?
 -----------
 ID3 is a decision tree algorithm used extensively in data mining.  It
 mines classification rules from a database, returning these rules in
 if-then-else form.  The algorithm is greedy, growing a tree at
 each step by using the attribute which has the most information gain
 associated with it.

 Installation
 ------------
To compile:
javac main.java
to run:
java main

 Usage
 -----
 To execute this program, once compiled, type: java main
 At the prompt, enter the name of the database file to be mined.  This
 database will be analyzed for all fields, returning a list from
 which the user must choose.  The field chosen will be the field for which
 the rules are generated.  The results will be if-then-else statements
 classifying the target attribute in the Rule file.

 Assumptions
 -----------
 o  The database file is either space or comma delimited.  If it is comma
    delimited, there must be no spaces (e.g. "North America" is not allowed).
 o  The rows of data do not exceed 250 characters in length.
 o  The number of tuples does not exceed 250.
 o  The number of fields does not exceed 40.
 o  There are no missing data values.

 Overview of Program Code
 ------------------------
 This program consists of nine Java source program files  and a document folder to have more helpfull information.
 Moreover, there is a few comments for each function in the source code to underestand how it is working.
 for more information go to doc folder or go to the source files. by the way a very short description for a few important functions are available below.
 The following illustrates the function distribution and explain some of the key functions:

main.java:

ID3.Java:
Classifier.java:
ID3Node.java:
Attribute.java:
item.java:
FileHandler.java


 main.java:
 ---->main()
 ------>FileHandler.setFilename()
 ------>FileHandler.loadData()                              Load dataset from a file to memory
 ------>FileHandler.getAttributes()                         fetch attributes from the loaded dataset
 ------>uiAssignment4.selectTargetAttribute                 User interface to select target attribute
 ------>uiAssignment4.verboseness()                         user interface to select the verbosity of results. if you select 2, you can see details of results
 ------>id3.setVerbose()
 ------>id3.TestCVNFold()

 id3.TestCVNFold(): this function Divide dataset to N number of sub dataset. then for N times, it select one subset ad test and other sets as train. in
 each step, it trains the classifier based on the train dataset. then test it with test dataset. Finally, the average number of accuracy in each step shows as
 accuracy of the classifier.


 UIAssignment4.java:
 ---->getInputFile()
 ------>FileHandler.getFilePath
 ---->selectTargetAttribute()
 ---->verboseness()
 ---->getTarget()

 FileHandler.java:
 ---->setFilename
 ---->loadData
 ---->getAttributes
 ---->getDataset
 ---->GetItemsinoneLine
 ---->GetItemsValuesinoneLine
 ---->getFilePath
 ---->loadDataSet
 ------>getFilePath
 ------>Files.readAllLines
 ------>GetItemsinoneLine
 ------>GetItemsValuesinoneLine


 ID3.java:
 ---->setVerbose
 ---->ID3()
 ------>setVerbose
 ---->printTree
 ------>printTree                                                   this function print tree recersively. in each step it calls all the child of the node.
 ---->determineBestSplit                                            This function determines the best attribute to start the algorithm. it selects the attribute based on gain indicator.
 ------>ID3Node.setVerbose
 ------>ID3Node.setCandidate
 ------>ID3Node.setTarget
 ------>ID3Node.gain
 ---->determineBestSplit2                                           This function determines the best attribute to select in each step of the algorithm. it selects the attribute based on gain indicator.
 ------>ID3Node.setCandidate
 ------>ID3Node.setTarget
 ------>ID3Node.gain
 ---->ID3(ID3Node id3Node)
 ------>determineBestSplit();
 ------>determineBestSplit2(id3Node);
 ------>id3Node.setCandidate(candidate);
 ------>id3Node.gain();
 ---->FoldTrainAndTest
 ------>random
 ---->getTestDataset
 ---->getFoldTest
 ---->getFoldTrain
 ---->random
 ---->TestClassifier(Vector<Vector<item>> test)                     this tests the classifier based on the input dataset.
 ------>prediction                                                  this function predicts the target value based on a given evidence.
 ---->DivideTrainAndTest                                            this function divides the dataset to train and test part.
 ------>random
 ---->TestClassifier
 ------>getTestDataset
 ------>prediction
 ---->train
 ------>ID3Node.setVerbose(verbose);
 ------>ID3Node.setTarget(target);
 ------>ID3Node.ID3(_root);
 ------>printTree
 ---->prediction
 ------>ID3Node.classtypeofmajority()                               this function calculates the majority of labels when there the node is not pure. it calls itself if there is 50/50 situation
 ---->TestCVNFold
 ------>FoldTrainAndTest
 ------>train                                                       this function trains the classifier and prints the if then else rules.
 ------>TestClassifier

 Classifier.java:
 ---->void train(final Vector<Vector<item>> trainingData,final Vector<Attribute> attributes, final Attribute target);
 ---->String prediction(Vector<item> data);
 ---->void DivideTrainAndTest(Vector<Vector<item>> data, int percent);
 ---->double TestClassifier();
 ---->double TestCVNFold(Vector<Vector<item>> data, int Fold, Attribute target, Vector<Attribute> attributes);

This class is an interface for Classifiers.



 ID3Node.java:
 ---->ID3Node()
 ---->subSet
 ------>getAttributeName().
 ---->getRemovedAttribute
 ---->classtypeofmajority
 ------>classCount
 ------>classtypeofmajority()
 ---->pad
 ---->getDescription                                        this function provides a good description of a node to use in the IF then statements.
 ------>classtypeofmajority();
 ---->setCandidate
 ---->getTarget
 ---->setTarget
 ------>Info
 ---->gain()
 ------>subSet
 ------>Info
 ---->UpdateCounts                                              this function updates the cardinality of each item.
 ---->Info                                                      this function calculate Entropy
 ------>UpdateCounts
 ------>classCount
 ---->setVerbose
 ---->toString()

 Attribute.java:
 ---->addClass
 ---->classTotal
 ---->classCount
 ---->equals
 ---->toString

 item.java:
 ---->getAttributeName
 ---->getvalue
 ---->item
 ---->toString


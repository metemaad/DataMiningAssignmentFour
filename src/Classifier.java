import java.util.Vector;

/**
 * This interface has the functions are shared between different classifiers. it works same as a handler to use for any classifier.

 */
public interface Classifier {

    /**
     * this function is responsible for train a classifier.
     * @param trainingData this is the traing dataset
     * @param attributes this is the list of attributes in the dataset
     * @param target this is the target dataset
     */
    void train(final Vector<Vector<item>> trainingData,final Vector<Attribute> attributes, final Attribute target);

    /**
     * this function get new evidence and pridict the target attribute.
     * This is part of bounce part of the question.
     * @param data this is the evidence that the target value of it should be predicted.
     * @return pridicted value for target attribute.
     */
    String prediction(Vector<item> data);

    /**
     * this function divide the dataset into a test set and a training set. the percent indicates the training part.
     * in this regard, the function uses a random generator to randomly assign each item of the dataset to the training set and test set.
     * @param data Dataset in vector structure
     * @param percent Percent of Dataset that should be in the training set.
     */
    void DivideTrainAndTest(Vector<Vector<item>> data, int percent);

    /**
     * this function test the classifier on the test part of dataset
     * @return accuracy of the classifier by ACC=(True Positive+True Negative)/(Positive+Negative)
     */
    double TestClassifier();

    /**
     * this function is an N-fold cross validation. it folds the dataset into N folds and then for each subset trains the dataset in the way that the subset is the test and all other subsets
     * are training dataset. Then, the accuracy of each step calculates and the average of these numbers is the accuracy of the whole
     * @param data Dataset in vector structure
     * @param Fold the number of folds in the dataset folding
     * @param target the target attribute
     * @param attributes list of all attributes in the dataset
     * @return the accuracy of cross-validation test.
     */
    double TestCVNFold(Vector<Vector<item>> data, int Fold, Attribute target, Vector<Attribute> attributes);
}
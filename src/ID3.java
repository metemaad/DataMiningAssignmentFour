import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

/**
 * this class is an implementation of Classifier for Desicion Tree classifier
 * Created by Mohammad Etemad on 3/09/17.
 */

public class ID3 implements Classifier {

    private int verbose=0;
    /**
     * _root is the root of the DT
     */
    public ID3Node _root;
    /**
     * divided dataset into test and training sets
     */
    private Vector<Vector<Vector<item>>> DividedDataset=new Vector<>();
    /**
     * Folded dataset into N parts
     */
    private Vector<Vector<Vector<item>>> FoldedDataset=new Vector<>();

    public void setVerbose(int verbose) {
        this.verbose = verbose;
    }

    /**
     * class constractor
     */
    public ID3() {
        _root=new ID3Node();
        _root.setVerbose(verbose);

    }


    /**
     * this function print the tree information based on the requested results. This will recursively go over all the nodes in tree.
     * @param root Tree root Node
     * @param writer writer variable to write the results in a file
     */
    private static void printTree(ID3Node root,PrintWriter writer) {

        for (ID3Node idn: root.children) {
            String s=idn.getDescription();

            System.out.print(s);
            writer.println(s);
            printTree(idn, writer);
        }
    }

    /**
     * In order to select the root we must find the highest information gain in the attributes.
     * we go over all the attributes and calculate information gain for each attribute then we check that
     * which attribute has the most information gain and select it as the root of our Dtree
     * @return candidate node for the root of DT
     */
    private Attribute determineBestSplit() {
        Vector < Attribute > SearchAttributes = new Vector < > ();
        SearchAttributes.addAll(_root.attributes);
        SearchAttributes.remove(_root.getTarget());
        Attribute Candidate = new Attribute();
        double maxGain = 0;
        for (Attribute a1: SearchAttributes) {
            ID3Node root = new ID3Node();
            root.setVerbose(verbose);
            root.data =_root.data;
            root.attributes = _root.attributes;
            root.setCandidate(a1);
            root.parent = null;
            root.setTarget(_root.getTarget());
            root.gain();
            if (verbose>1) System.out.print("Gain[" + a1.AttributeName + "]=" + root.gain + "\n");
            if (maxGain < root.gain) {
                maxGain = root.gain;
                Candidate = a1;
            }
        }

        return Candidate;
    }

    /**
     * in each step we need to go over the rest of attributes and find information gain for all of them
     * then we find the highest information gain between them and select the node as the candidate node to expand
     * @param node a node of ID3
     * @return candidate attribute to expand in that node
     */
    private Attribute determineBestSplit2(ID3Node node){

        Vector < Attribute > SearchAttributes = new Vector < > ();
        SearchAttributes.addAll(node.attributes);
        SearchAttributes.removeAll(node.getRemovedAttribute());
        SearchAttributes.remove(node.getTarget());
        Attribute Candidate = new Attribute();
        double maxgain = 0;
        for (Attribute a1: SearchAttributes) {
            node.setCandidate(a1);
            node.setTarget(node.getTarget());
            node.gain();
            if (this.verbose>1) System.out.print("Gain[" + a1.AttributeName + "]=" + node.gain + "\n");
            if (maxgain < node.gain) {
                maxgain = node.gain;
                Candidate = a1;
            }
        }

        return Candidate;
    }

    /**
     * initializing the ID3 function
     * @param id3Node root node
     */
    private void ID3(ID3Node id3Node) {

        if (verbose>1) System.out.print("run id3 on " + id3Node.data.size() + "this: " + id3Node + " parent:" + id3Node.parent + "\n");

        Attribute candidate;

        if (id3Node.parent == null) {
            candidate = determineBestSplit();

        } else {
            candidate = determineBestSplit2(id3Node);
        }

        id3Node.setCandidate(candidate);
        id3Node.gain();

        for (ID3Node idnod: id3Node.children) {
            idnod.setTarget(id3Node.getTarget());

            if (!idnod.pure) {
                ID3(idnod);
            }
        }
    }

    /**
     * fold the dataset into N folds
     * @param data dataset
     * @param fold number of folds
     */
    public void FoldTrainAndTest(Vector<Vector<item>> data, int fold) {
        FoldedDataset=new Vector<>();
        Vector<Vector<item>> tmpfold=new Vector<>();
        int n=data.size()/fold;
        for (int j=0;j<fold-1;j++){
            tmpfold=new Vector<>();
            for (int i = 0; i<=n - 1; i++)
            {
                Vector<item> select = data.get(random(data.size()));
                tmpfold.add(select);
                data.remove(select);
            }
            FoldedDataset.add(j,tmpfold);
        }
        FoldedDataset.add(fold-1,data);
    }

    /**
     * this function returns the test dataset after division
     * @return test dataset
     */
    public Vector<Vector<item>> getTestDataset() {

        return DividedDataset.get(1);
    }

    /**
     * return the Nth part of dataset that has been folded
     * @param fold number of fold
     * @return dataset related to that number
     */
    public Vector<Vector<item>> getFoldTest(int fold) {

        return FoldedDataset.get(fold);
    }

    /**
     * combine all dataset folds except test fold (fold) and generate the Train dataset
     * @param fold number of test fold
     * @return traing test dataset for fold
     */
    public Vector<Vector<item>> getFoldTrain(int fold) {

        Vector<Vector<item>> ret=new Vector<>();
        int h=FoldedDataset.size()-1;
        for (int j=0;j<h;j++)
        {
            Vector<Vector<item>> tmp = FoldedDataset.get(j);
            ret.addAll(tmp);
        }




        ret.removeAll(FoldedDataset.get(fold));
        return ret;
    }

    /**
     * generate random number
     * @param n input number to be generated
     * @return random number
     */
    private int random(int n){ Random rand = new Random(); return rand.nextInt(n) ;}

    /**
     * test the classifier on test dataset
     * @param test test dataset
     * @return accuracy of classifier based on the test dataset
     */
    public double TestClassifier(Vector<Vector<item>> test)   {
        int good=0;
        int total=0;
        if (this.verbose>1) System.out.print("\n============================================================");
        if (this.verbose>1) System.out.print("\nTesting "+this.getTestDataset().size()+" cases.");
        for (int i=0;i<=test.size()-1;i++)
        {
            total++;
            Vector<item> test1 = test.get(i);
            int idxtarget=this._root.attributes.indexOf(this._root.getTarget());
            if (this.verbose>1) System.out.print("\n*  ["+total+"]   : "+  this.prediction(test1)+ "<=>"+test1.get(idxtarget).getvalue()+"\r\n");
            if (this.prediction(test1).equals(test1.get(idxtarget).getvalue())){good++;}
        }

        double accuracy = (100 * good) / total;
        if (this.verbose>1) System.out.print(accuracy);
        return accuracy;

    }

    /**
     * implemementation of DivideTrainAndTest of classifier interface
     * @param data Dataset in vector structure
     * @param percent Percent of Dataset that should be in the training set.
     */
    @Override
    public void DivideTrainAndTest(Vector<Vector<item>> data, int percent) {
        DividedDataset=new Vector<>();
        Vector<Vector<item>> train=new Vector<>();
        int n=data.size()*percent/100;
        for (int i = 0; i<=n - 1; i++)
        {
            Vector<item> select = data.get(random(data.size()));
            train.add(select);
            data.remove(select);
        }
        DividedDataset.add(0,train);
        DividedDataset.add(1,data);
    }

    /**
     * implemementation of TestClassifier of classifier interface
     * @return accuracy of classiffier
     */
    @Override
    public double TestClassifier() {
        int good=0;
        int total=0;
        if (this.verbose>1) System.out.print("\n============================================================");
        if (this.verbose>1) System.out.print("\nTesting "+this.getTestDataset().size()+" cases.");
        for (int i=0;i<=this.getTestDataset().size()-1;i++)
        {
            total++;
            Vector<item> test1 = this.getTestDataset().get(i);
            int idxtarget=this._root.attributes.indexOf(this._root.getTarget());
            if (this.verbose>1) System.out.print("\n*  ["+total+"]   : "+  this.prediction(test1)+ "<=>"+test1.get(idxtarget).getvalue()+"\r\n");
            if (this.prediction(test1).equals(test1.get(idxtarget).getvalue())){good++;}
        }

        double accuracy = (100 * good) / total;
        if (this.verbose>1) System.out.print(accuracy);
        return accuracy;

    }

    /**
     * implementation of train function in classifier interface
     * @param trainingData this is the traing dataset
     * @param attributes this is the list of attributes in the dataset
     * @param target this is the target dataset
     */
    @Override
    public void train(Vector<Vector<item>> trainingData, Vector<Attribute> attributes, Attribute target) {


        _root = new ID3Node();
        _root.setVerbose(verbose);
        _root.data = trainingData;
        _root.attributes = attributes;
        _root.setTarget(target);
        _root.parent = null;

        ID3(_root);

        System.out.print("\n\n\n==========================================\n\n");

        PrintWriter writer;
        try {
            writer = new PrintWriter("Rules", "UTF-8");
            writer.println("------------------------------------------------------------");
            printTree(_root,writer);
            writer.close();
        }catch (Exception s)
        {

        }
    }

    /**
     * impelementation of perediction function of classifier interface
     * @param data this is the evidence that the target value of it should be predicted.
     * @return pridicted value for target attribute
     */
    @Override
    public String prediction(Vector<item> data)  {

        System.out.print("\nPrediction : "+data.toString()+" ");
        ID3Node tmp = this._root;
        boolean find=false;
        int y=0;
        while((tmp.children.size()>0)&(y<15))
        {

            y++;int idx=_root.attributes.indexOf(tmp.candidate);
            item dt = data.get(idx);

            for (ID3Node cc:tmp.children) {
                if (dt.getvalue().equals(cc.classname))
                {
                    tmp=cc;
                    find=true;
                    break;
                }
            }
            if (!find)
            {
                break;
            }

        }


        String st=(find)?tmp.classtypeofmajority():"";
        System.out.print(" prdiction={"+st+"}");
        return st;
    }

    /**
     * implementation of TestCVNFold cross validation in classifier interface
     * @param data Dataset in vector structure
     * @param Fold the number of folds in the dataset folding
     * @param target the target attribute
     * @param attributes list of all attributes in the dataset
     * @return accuracy of classifier
     */
    @Override
    public double TestCVNFold(Vector<Vector<item>> data, int Fold, Attribute target, Vector<Attribute> attributes) {
        this.FoldTrainAndTest(data,Fold);

        double m=0;
        String St="";
        for(int i=0;i<=Fold-1;i++){




            this.train(getFoldTrain(i),attributes, target);


            double Accuracy = this.TestClassifier(getFoldTest(i));
            St+="\n Fold "+i+" Accuracy="+Accuracy+" ";
            m+=Accuracy;

        }
        System.out.print(St);
        return m/Fold;


    }

}
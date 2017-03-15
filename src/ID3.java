import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

/**
 *
 * Created by Mohammad Etemad on 3/09/17.
 */

public class ID3 implements Classifier {

    private int verbose=0;
    public ID3Node _root;
    private Vector<Vector<Vector<item>>> DividedDataset=new Vector<>();
    private Vector<Vector<Vector<item>>> FoldedDataset=new Vector<>();
    public void setVerbose(int verbose) {
        this.verbose = verbose;
    }
    public ID3() {
        _root=new ID3Node();
        _root.setVerbose(verbose);

    }



    private static void printTree(ID3Node root,PrintWriter writer) {

        for (ID3Node idn: root.children) {
            String s=idn.getDescription();

            System.out.print(s);
            writer.println(s);
            printTree(idn, writer);
        }
    }
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
    public Vector<Vector<item>> getTestDataset() {

        return DividedDataset.get(1);
    }
    public Vector<Vector<item>> getFoldTest(int fold) {

        return FoldedDataset.get(fold);
    }
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
    private int random(int n){ Random rand = new Random(); return rand.nextInt(n) ;}
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
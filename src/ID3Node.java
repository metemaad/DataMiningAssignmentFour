import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * This class is a data structure for the ID3 node in the Desicion tree.
 * This node can calculate information gain and entropy of the node as well.
 * It also be used to create the printing version of the tree.
 * Created by Mohammad Etemad on 3/9/17.
 */
public class ID3Node {

    /**
     * Description of node
     */
    public String Desc = "";
    /**
     * Information gain of the node
     */
    double gain;
    /**
     * class name of the attribute that the node expaned based on that
     */
    String classname;
    /**
     * if all of the records in the dataset are in the same class that dataset is pure.
     * Also it uses in case we need the node to be a leaf
     */
    boolean pure;
    /**
     * list of all attributes
     */
    Vector<Attribute> attributes = new Vector<>();
    /**
     * dataset of subset of dataset that the node will work on it.
     */
    Vector<Vector<item>> data;
    /**
     * list of all child of a node in DT
     */
    Vector<ID3Node> children = new Vector<>();
    /**
     * this points to the parent of the node in DT
     */
    ID3Node parent;
    /**
     * this is the selected attribute that the node will expands on that
     */
    Attribute candidate;
    /**
     * stores the entropy of the node
     */
    private double entropy;
    /**
     * store the destance of the node to the root
     */
    private int depth;
    /**
     * degree of the verbosity
     */
    private int verbose = 0;
    /**
     * target value in the leaf will be the value of that class for pure node.
     * if its not pure node the majority of the target values will be the returning value.
     * if it is the 50-50 situation, the parents value of target value will be used
     */
    private String targetValueInLeaf;
    /**
     * when we use an attribute in the way from the root to this node they will be listed here.
     * we will not use then again to expand
     */
    private Set<Attribute> removedAttribute;
    /**
     * target attribute
     */
    private Attribute target;

    /**
     * constructor of the node and initializing the values
     */
    ID3Node() {
        data = new Vector<>();
        targetValueInLeaf = "";
        depth = 0;
        pure = false;
        candidate = null;
        removedAttribute = new HashSet<>();
    }

    /**
     * devide the dataset based on an attribute to its classes
     * @param set the dataset to be divided
     * @param attribute the attribute that dataset will be divided on
     * @return the list of datasets that is generated
     *
     */
    private static Vector<subSetclass> subSet(Vector<Vector<item>> set, Attribute attribute) {


        Vector<subSetclass> res2 = new Vector<>();
        for (String classname : attribute.AttributeClasses) {


            subSetclass res = new subSetclass();
            for (Vector<item> i : set) {
                boolean chk = false;
                for (item tmp : i) {
                    if ((tmp.getAttributeName().equals(attribute.AttributeName)) & (tmp.getvalue().equals(classname))) {
                        chk = true;
                    }

                }
                if (chk) {
                    res.classname = classname;
                    res.attribute=attribute;
                    res.subSet.add(i);
                }
            }
            res2.add(res);
        }
        return res2;
    }

    /**
     *
     * @return returns removed attributes
     */
    public Set<Attribute> getRemovedAttribute() {
        return removedAttribute;
    }

    /**
     * claculate the class target value based on the cardinality of majority class
     * @return the target value for the node
     */
    public String classtypeofmajority() {

        target = UpdateCounts(target);
        if (verbose > 1) System.out.print(" \n* Majority Check . . * ");
        int maxpr = 0;
        String classname = "";
        for (String cls : target.AttributeClasses) {
            if (maxpr < (100 * target.classCount(cls) / target.total)) {
                maxpr = (100 * target.classCount(cls) / target.total);
                classname = cls;
            }
            if (verbose > 1)
                System.out.print(" " + cls + " : " + ((100 * target.classCount(cls) / target.total)) + "%  ");
        }
        if (verbose > 1) System.out.print(" *\n");
        if (maxpr == 50) {
            if (this.parent != null) {
                if (verbose > 1) System.out.print(" \n* Parent Majority Check . . * \n");
                return this.parent.classtypeofmajority();
            } else {
                return "vague!";

            }
        } else {
            return classname;
        }
    }

    /**
     * add padding spaces to the result if-then statements
     * @param j number of padding
     * @return string with that number of spaces
     */
    private String pad(int j) {

        String str = "";
        for (int i = 0; i <= j; i++) {
            str += " ";
        }
        return str;
    }

    /**
     * returns the description of the node
     * @return description of the node
     */
    public String getDescription() {
        String Desc = "";
        if (this.parent == null) {
        } else {
            if ((this.pure) | (this.children.size() == 0)) {
                if ((this.children.size() == 0)) {

                    String st = this.classtypeofmajority();

                    Desc += pad(this.depth - 1) + " if " + this.parent.candidate.AttributeName + " is " + this.classname + " then " + this.target.AttributeName + " is " + st + ".\n";

                } else {

                    Desc += pad(this.depth - 1) + " if " + this.parent.candidate.AttributeName + " is " + this.classname + " then " + this.target.AttributeName + " is " + this.targetValueInLeaf + ".\n";
                }
            } else {
                Desc += pad(this.depth - 1) + " If " + this.parent.candidate.AttributeName + " is " + this.classname + " then \n";

            }
        }
        this.Desc = Desc;
        return Desc;
    }

    /**
     * set the candidate attribute
     * @param candidate candidate attribute
     */
    void setCandidate(Attribute candidate) {
        this.candidate = candidate;

        if (this.parent == null) {
            this.removedAttribute = new HashSet<>();
            this.removedAttribute.add(candidate);

        } else {
            this.removedAttribute = new HashSet<>();
            this.removedAttribute.addAll(this.parent.removedAttribute);
            this.removedAttribute.add(candidate);
        }


    }

    /**
     * returns the target attribute
     * @return target attribute
     */
    Attribute getTarget() {return target; }
    void setTarget(Attribute targetAttribute) {
        target = targetAttribute;
        this.entropy = this.Info(target);

    }

    /**
     * calculate the Information gain for this node
     * this function generates child for the node based on candidate attribute classes
     */
    void gain() {

        if (verbose > 1)
            System.out.print("\n Calculating Gain for " + this.candidate.AttributeName + " on data size " + this.data.size() + "\n");

        this.children = new Vector<>();
        Vector<subSetclass> subsets = subSet(this.data, candidate);
        for (subSetclass subset : subsets) {
            ID3Node node1 = new ID3Node();
            node1.verbose = this.verbose;
            node1.parent = this;
            node1.classname = subset.classname;
            node1.attributes = attributes;
            node1.data = subset.subSet;
            node1.target = target;
            node1.entropy = node1.Info(target);
            //node1.setCandidate(null);
            node1.depth = this.depth + 1;
            if (verbose > 1) System.out.print("subset  " + subset.classname + " with " + subset.subSet.size() + "\n");
            //  System.out.print("InfoA["+a1.AttributeName+"]=" + node1.entropy+"\n");
            if (node1.data.size() > 0) this.children.add(node1);

        }

        double deltagain = 0;
        for (ID3Node node : this.children) {
            if (verbose > 1)
                System.out.print("-  " + node.classname + " : " + node.data.size() + "/" + this.data.size() + "\n");
            deltagain += (node.entropy * node.data.size()) / this.data.size();

        }
        this.gain = this.entropy - deltagain;
        if (verbose > 1) System.out.print(" -> Gain  " + this.gain + "\n");

    }
    private Attribute UpdateCounts(Attribute attribute) {

        int index = this.attributes.indexOf(attribute);
        int index2 = this.attributes.indexOf(target);

        Attribute newatt = new
                Attribute();
        newatt.AttributeName = attribute.AttributeName;
        item itemi2 = new item();
        for (Vector<item> items : this.data) {
            item itemi = items.get(index);
            itemi2 = items.get(index2);
            newatt.addClass(itemi.getvalue());
        }

        for (String classname : newatt.AttributeClasses) {
            if ((newatt.classCount(classname) == 0) | (this.data.size() == 0) | ((newatt.classCount(classname) == this.data.size()))) {
                this.pure = true;
                this.targetValueInLeaf = itemi2.getvalue();
                break;

            }

        }
        return newatt;


    }
    private double Info(Attribute target) {

        target = UpdateCounts(target);
        double entropy = 0;
        if (this.data.size() == 0) {
            return 0;
        }
        for (String cl : target.AttributeClasses) {
            int classCount = target.classCount(cl);
            int total = target.classTotal();
            double probability = classCount / (double) total;
            if (classCount > 0) {
                entropy += -probability * (Math.log(probability) / Math.log(2));
            }
        }
        return entropy;
    }
    public void setVerbose(int verbose) {
        this.verbose = verbose;
    }

    @Override
    public String toString() {
        return " [candidate="+this.candidate+"";
    }
}
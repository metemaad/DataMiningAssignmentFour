import java.util.Vector;

/**
 * This class stores a subset of our dataset. each dataset has some attributes. the assumption is that all information in the dataset is a categorical type of information.
 * therefore, each attribute has some classes. in DT we need to divide our dataset into different classes based on the selected attribute. and each or these subsets
 * can store in an instance of subSetclass
 *
 *
 */
public class subSetclass {
    /**
     * subSet has the same structure of our dataset and it is just a selection of dataset based on an attribute with values in a class.
     */
    public Vector<Vector<item>> subSet=new Vector<>();
    /**
     * class name that the dataset is devided based on that
     */
    public String classname="";
    /**
     * this is the attribute that dataset has been devided based on it.
     */
    public Attribute attribute;


}
/**
 * Created by Mohammad Etemad on 3/9/17.
 * item is a class to store an attribute and its value together.
 */
public class item {
    private String attribute;
    private String value;

    /**
     * this returns an attribute name of an instance
     * @return the attribute name of the instance.
     */
    public String getAttributeName() {
        return attribute;
    }
    public String getvalue() {
        return value;
    }

    /**
     * when we make a new instance we can add the attribute and value as parameter
     * @param attribute this defines the type of attribute.
     * @param value  This defines the value of the attribute.
     */
    public item(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;

    }

    /**
     * this function makes a empty instance of an item.
     */
    public item() {
        this.attribute = "";
        this.value = "";

    }
    @Override
    /**
     *  this function makes a meaningfull description of an intance.
     */
    public String toString() {
        return " ["+this.attribute+"="+this.value+"] ";
    }

}
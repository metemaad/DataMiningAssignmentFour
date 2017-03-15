import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

/**
 * This class responsible for creating the user interface.
 * it has functions for getting the file name and selecting the target attribute.
 *
 * Created by Mohammad Etemad on 3/10/17.
 */
public class UIAssignment4 {

    /**
     * this is the target attribute.
     */
    private Attribute _target;

    /**
     user interface for getting the dataset file name
     * @return dataset file name
     */
    public String getInputFile() {
        String filename = "";
        boolean Invalid = false;
        while (!Invalid) {
            Scanner scan2 = new Scanner(System.in);
            System.out
                    .print("What is the name of the file containing your data?");
            filename = scan2.next();
            String DBFileName = FileHandler.getFilePath(filename);
            Path path = Paths.get(DBFileName);
            if (Files.isReadable(path)) {
                Invalid = true;
            } else {
                System.out.print("\r\nCannot open file:" + DBFileName + "\r\n");
            }

        }
        String _filename = filename;
        return filename;
    }

    /**
     * user interface for getting the target attribute.
     * @param attributes list of all attributes than can be selected as a target attribue.
     * @return target attribute.
     */
    public Attribute selectTargetAttribute(Vector<Attribute> attributes) {
        int target = 0;
        boolean Invalid = false;
        while (!Invalid) {
            Scanner scan2 = new Scanner(System.in);

            System.out.print("Please choose an attribute (by number): \n");
            for (Attribute s: attributes) {
                System.out.print("\t" + attributes.indexOf(s) + "-" + s.AttributeName + " \n");

            }
            System.out.print("Attribute:");


            target = scan2.nextInt();

            try {
                Attribute a = attributes.get(target);
                System.out.print("\nTarget attribute is :" + a.AttributeName + "\n");
                Invalid = true;
            } catch (Exception e) {
                Invalid = false;
                System.out.print("\nPlease select a valid attribute.");

            }



        }
        _target=attributes.get(target);
        return _target;


    }

    /**
     * user interface for getting the degree of verbosity.
     * this function help user to track the process by looking at the intermediate results.
     * @return user choice of verbosness
     */
    public int verboseness() {


        int target = 0;

            Scanner scan2 = new Scanner(System.in);

            System.out.print("Please choose a verbosity level for results:(0=concise 2=verbose) \n");
            System.out.print("0=concise just print the normal results.) \n");
            System.out.print("2=verbose for getting a detailed results step by step.) \n");

            System.out.print("Verbosity:");

            try {

            target = scan2.nextInt();


            } catch (Exception e) {
                target=0;
                System.out.print("\nNo verbose.\n");

            }




        return target;


    }

    /**
     *
     * @return target attribute
     */
    public Attribute getTarget() {
        return _target;
    }
}
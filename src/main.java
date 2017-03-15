/**
 * Created by mohammad on 3/9/17.
 */
public class main {


    public static void main(String[] args) {

        UIAssignment4 uiAssignment4 = new UIAssignment4();
        FileHandler fl = new FileHandler();
        fl.setFilename(uiAssignment4.getInputFile());
        fl.loadData();
        fl.getAttributes();
        uiAssignment4.selectTargetAttribute(fl.getAttributes());
        int verbose = 0;//uiAssignment4.verboseness();
        ID3 id3 = new ID3();
        id3.setVerbose(verbose);
        int Fold = 8;
        double Accuracy = id3.TestCVNFold(fl.getDataset(), Fold,uiAssignment4.getTarget() ,fl.getAttributes());
        System.out.print("\n============================================================================\n");
        System.out.print("Accuracy of ID3 by CV fold=" + Fold + " : " + Accuracy + "%\n");
        System.out.print("============================================================================\n");
        System.out.print("If-then-else statements are ready in file :Rules\n");

    }
}
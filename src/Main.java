import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Program program = Program.createProgram();
        program.sourceFilesDirectory = "solved/smallest_818f8cf4_003/src/main"; //"grade001/src/main";//"digit003/src/main"; //"syl002/src/main";//
        program.sourceClassFilesDirectory = "solved/smallest_818f8cf4_003/bin"; //"grade001/bin";//"syl002/bin";//
        program.testFilesDirectory = "solved/smallest_818f8cf4_003/src/test"; //"grade001/src/test";//"syl002/src/test";//
        program.testClassFilesDirectory = "solved/smallest_818f8cf4_003/test"; //"grade001/test";//"syl002/test";//

        scanDirectory(new File(program.sourceFilesDirectory));
        System.out.println("END");
    }

    private static void scanDirectory(File folder) throws IOException {
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".java")) {
                System.out.println("Localizing Fault");
                FaultLocalizer faultLocalizer = FaultLocalizer.createFaultLocalizer();
                faultLocalizer.localizeFault();
                PatchEvaluator patchEvaluator = PatchEvaluator.createPatchEvaluator();
                patchEvaluator.prepareTestClasses();
                
                PatchGenerator patchGenerator = PatchGenerator.createPatchGenerator();
                patchGenerator.generatePatch(listOfFiles[i], System.nanoTime());
                break;
            }
            else if (listOfFiles[i].isDirectory()) {
                scanDirectory(new File(folder+"/"+listOfFiles[i].getName()));
            }
        }
    }
}

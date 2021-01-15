import info.debatty.java.stringsimilarity.Cosine;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteChangedAstToFile {

    Compiler compiler;
    PatchEvaluator patchEvaluator;
    Cosine cosine;

    private WriteChangedAstToFile(){
        compiler = Compiler.createCompiler();
        patchEvaluator = PatchEvaluator.createPatchEvaluator();
        cosine = CosineImpl.createCosine();
    }

    private static WriteChangedAstToFile writeChangedAstToFile;
    public static WriteChangedAstToFile createWriteChangedAstToFile(){
        if(writeChangedAstToFile == null)
            writeChangedAstToFile = new WriteChangedAstToFile();

        return writeChangedAstToFile;
    }

    int writeChangedAst(ASTRewrite astWriter, Document document, File file,int count,CandidatePatch candidatePatch) throws BadLocationException, IOException {
        boolean correctPatchFound;
        TextEdit edits = astWriter.rewriteAST(document, null);
        edits.apply(document);
        //System.out.println(document.get());
        (new File("mutants/" + file.getParent() + "/" + count + "/")).mkdirs();
        File mutantFile = new File("mutants/" + file.getParent() + "/" + count + "/" + file.getName());
        mutantFile.createNewFile();
        generateProgramVariant(mutantFile, document);
        if (this.compiler.compileProject(mutantFile.getAbsolutePath(), "output")) { //file.getAbsolutePath(),Program.sourceClassFilesDirectory
             correctPatchFound = this.patchEvaluator.evaluatePatch();

            if (correctPatchFound) {
                PatchGenerator.correctPatches++;
                //System.out.println("Cosine Score: "+cosine.similarity(candidatePatch.faultyNode.toString(),candidatePatch.fixingIngredient.toString()));
                System.out.println("Correct Patch Generated!");//+ " Elapsed Time: " +(System.nanoTime()-startingTime));
                System.out.println("File no " + count);
                System.out.println(candidatePatch.faultyNode);
                System.out.println(candidatePatch.fixingIngredient);
                System.out.println(candidatePatch.mutationOperation);
                (new File("correct_patch/" + file.getParent() + "/" + count + "/")).mkdirs();
                File correctFile = new File("correct_patch/" + file.getParent() + "/" + count + "/" + file.getName());
                correctFile.createNewFile();
                generateProgramVariant(correctFile,document);

            }
            else{
                deleteDirectory(new File("mutants/"+file.getParent()+"/"+count));

            }
        }
        return count;
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] contents = directoryToBeDeleted.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    void generateProgramVariant(File file, Document document) {
        try {
            FileWriter fileWrite = new FileWriter(file.getAbsolutePath());
            fileWrite.write(document.get());
            fileWrite.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

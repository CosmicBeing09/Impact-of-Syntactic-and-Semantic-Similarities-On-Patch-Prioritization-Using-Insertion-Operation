import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PatchGenerator {
    private static PatchGenerator patchGenerator;
    CompilationUnit compilationUnit, compilationUnit1;
    public static int correctPatches;
    ArrayList<CandidatePatch> candidatePatchesList = new ArrayList<CandidatePatch>();
    Document document;
    Document document2;
    File file;
    public int count;
    boolean correctPatchFound;
    long startingTime;
    IngredientCollector ingredientCollector;
    Compiler compiler;
    PatchEvaluator patchEvaluator;
    ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
    TargetMethodInvocationInsertionHandler targetMethodInvocationInsertionHandler;
    TargetBlockInsertionHandler targetBlockInsertionHandler;

    private PatchGenerator() {
        this.compiler = Compiler.createCompiler();
        this.patchEvaluator = PatchEvaluator.createPatchEvaluator();
        this.ingredientCollector = IngredientCollector.createIngredientCollector();
        this.targetMethodInvocationInsertionHandler = TargetMethodInvocationInsertionHandler.createTargetMethodInvocationInsertionHandler();
        this.targetBlockInsertionHandler = TargetBlockInsertionHandler.createIfStatementInsertionHandler();
    }

    public static PatchGenerator createPatchGenerator() {
        if (patchGenerator == null) {
            patchGenerator = new PatchGenerator();
        }

        return patchGenerator;
    }

    void generatePatch(File file, long startingTime) {
        Program program = Program.createProgram();
        this.startingTime = startingTime;
        this.init();
        //	file = new File("digit003/src/main/java/introclassJava/digits_6e464f2b_003_old.java"); // //D:/thesis/software repair/resources/20/capgen/CapGen/IntroClassJava/dataset/syllables/fcf701e8bed9c75a4cc52a990a577eb0204d7aadf138a4cad08726a847d66e77126f95f06f839ec9224b7e8a887b873fe0d4b6f4311b4e8bd2a36e5028d1feca/002/src/main/java/introclassJava/syllables_fcf701e8_002.java
        this.file = file;

        ASTParser parser = ASTParser.newParser(AST.JLS10);
        String fileContent = readFileToString(file.getAbsolutePath());
        this.document = new Document(fileContent);
        parser.setSource(document.get().toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setEnvironment(new String[]{program.sourceClassFilesDirectory}, new String[]{program.sourceFilesDirectory}, null, true);
        parser.setUnitName("file.java");

//        correctPatchFileNo.clear();
        this.compilationUnit = (CompilationUnit) parser.createAST(null);
        this.compilationUnit.accept(new VariableCollector());
        this.compilationUnit.accept(ingredientCollector);

        for (int i = 0; i < this.ingredientCollector.faultyNodes.size(); i++) {
            FaultyNode faultyNode = this.ingredientCollector.faultyNodes.get(i);
            this.generatePatchTemplate(faultyNode);
        }

        Collections.sort(this.candidatePatchesList);
//
        this.writeCandidatePatches();
////		System.out.println((long)15*60*1000000000);
        this.correctPatchFound = false;
        for (int i = 0; i < this.candidatePatchesList.size(); i++) { //candidatePatches.size()
            long currentTime = System.nanoTime();
//			System.out.println("Patch no: "+(i+1)+ " ");

            if ((currentTime - startingTime) >= (long) 20 * 60 * 1000000000) {
                System.out.println("time-up!!!!!!!!!!!!!!!!");
                break;
            }
            this.document = new Document(fileContent);
            this.document2 = new Document(fileContent);
            CompilationUnit compilationUnitCopy = (CompilationUnit) ASTNode.copySubtree(compilationUnit.getAST(), compilationUnit);


            ASTRewrite rewriter = ASTRewrite.create(compilationUnitCopy.getAST()); //compilationUnit.getAST();
            ASTRewrite rewriter2 = ASTRewrite.create(compilationUnitCopy.getAST());

            this.generateConcretePatch(rewriter, rewriter2, candidatePatchesList.get(i), i);

        }

//        File resfile = new File("D:\\PDF\\thesisPaper\\results\\insertOperation\\metricVar.csv");
//        FileWriter fileWrite = null;
//        WriteChangedAstToFile writeChangedAstToFile = WriteChangedAstToFile.createWriteChangedAstToFile();
//
//        try {
//            fileWrite = new FileWriter(resfile.getAbsolutePath(),true);
//            String str = Arrays.toString(writeChangedAstToFile.getListPair().getValue().toArray()).replace(",", " ");
//            String fileStr = Arrays.toString(writeChangedAstToFile.getListPair().getKey().toArray()).replace(",", " ");
//
//            List<List<String>> rows = Arrays.asList(
//                    Arrays.asList(file.getName(), str, Integer.toString(this.candidatePatchesList.size()), fileStr)
//            );
//
//            for (List<String> rowData : rows) {
//                fileWrite.append(String.join(",", rowData));
//                fileWrite.append("\n");
//            }
//
//            fileWrite.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // deleteIncorrectPatch(file);
        System.out.println(this.candidatePatchesList.size() + " Patches Generated");
        System.out.println(this.correctPatches + " Correct Patches Found");
    }

    void writeCandidatePatches() {
        File newfile = new File(this.file.getParent() + "Candidates11.csv");
        try {
            FileWriter fileWrite = new FileWriter(newfile.getAbsolutePath());
            for (int i = 0; i < this.candidatePatchesList.size(); i++) {
                fileWrite.write(this.candidatePatchesList.get(i).toString() + "\n");
            }

            fileWrite.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void init() {
        this.count = 0;
        this.correctPatches = 0;
        this.candidatePatchesList.clear();
        VariableCollector.variables.clear();
        this.ingredientCollector.faultyNodes.clear();
        this.ingredientCollector.fixingIngredients.clear();
    }

    public String readFileToString(String filePath) {
        StringBuilder fileData = new StringBuilder(100000);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            char[] buffer = new char[10];
            int numRead = 0;
            while ((numRead = reader.read(buffer)) != -1) {
                String readData = String.valueOf(buffer, 0, numRead);
                fileData.append(readData);
                buffer = new char[1024];
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return fileData.toString();
    }

    public void generatePatchTemplate(FaultyNode faultyNode) {
        InsertionHandler insertionHandler = InsertionHandler.createInsertionHandler();
        if (faultyNode.node instanceof ExpressionStatement) {
            ASTNode temp = faultyNode.node.getParent();

            try {
                while (!(temp instanceof Block) && temp != null) {
                    temp = temp.getParent();
                }

            } catch (Exception e) {
                // e.printStackTrace();
            }
            if (temp.getParent().getNodeType() == ASTNode.IF_STATEMENT) {
                insertionHandler.insertUnderIfStatement(faultyNode);
            } else if (temp.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
                insertionHandler.insertUnderMethodDeclaration(faultyNode);
            } else if (temp.getParent().getNodeType() == ASTNode.TRY_STATEMENT) {
                insertionHandler.insertUnderTryStatement(faultyNode);
            }

        } else if (faultyNode.node instanceof Expression) {

            ASTNode temp = faultyNode.node.getParent();
            try {
                while (!(temp instanceof MethodInvocation)) {
                    temp = temp.getParent();
                }
            } catch (Exception e) {
            }


            if (temp != null)
                insertionHandler.insertUnderMethodInvocation(faultyNode);
        }
    }


    private void generateConcretePatch(ASTRewrite rewriter, ASTRewrite rewriter2, CandidatePatch candidatePatch, int rank) {
        try {
            if (candidatePatch.mutationOperation.equals("replace")) {

                rewriter.replace(candidatePatch.faultyNode, candidatePatch.fixingIngredient, null);
            } else if (candidatePatch.mutationOperation.equals("insert")) {

                if (candidatePatch.faultyNode instanceof ExpressionStatement) {
                    ASTNode temp = candidatePatch.faultyNode.getParent();

                    try {
                        while (!(temp instanceof Block) && temp != null) {
                            temp = temp.getParent();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Block temp2 = (Block) temp;
                    if (temp2.statements() != null && temp2.statements().contains(candidatePatch.faultyNode)) {

                        try {
                            targetBlockInsertionHandler.insertAfter(temp, candidatePatch, rewriter, document, file, this.count, rank);
                            this.count += 1;
                            targetBlockInsertionHandler.insertBefore(temp, candidatePatch, rewriter2, document2, file, this.count, rank);
                            this.count += 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } else if (candidatePatch.faultyNode instanceof Expression && (candidatePatch.fixingIngredient.getNodeType() == ASTNode.BOOLEAN_LITERAL
                        || candidatePatch.fixingIngredient.getNodeType() == ASTNode.SIMPLE_NAME || candidatePatch.fixingIngredient.getNodeType() == ASTNode.QUALIFIED_NAME)) {

                    ASTNode temp = candidatePatch.faultyNode.getParent();
                    while (!(temp instanceof MethodInvocation)) {
                        temp = temp.getParent();
                    }

                    MethodInvocation temp2 = (MethodInvocation) temp;

                    if (temp2.arguments().contains(candidatePatch.faultyNode)) {

                        try {
                            targetMethodInvocationInsertionHandler.insertAfter(temp, candidatePatch, rewriter, document, file, this.count, rank);
                            this.count += 1;

                            targetMethodInvocationInsertionHandler.insertBefore(temp, candidatePatch, rewriter2, document2, file, this.count, rank);
                            this.count += 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            } else if (candidatePatch.mutationOperation.equals("delete")) {
                rewriter.remove(candidatePatch.fixingIngredient, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (StackOverflowError overflow) {
            //overflow.printStackTrace();
        }
    }


}

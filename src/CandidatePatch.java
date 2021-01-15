import org.eclipse.jdt.core.dom.ASTNode;

import java.io.Serializable;

public class CandidatePatch implements Serializable, Comparable <CandidatePatch>{

    ASTNode faultyNode;
    ASTNode fixingIngredient;
    double score;
    String mutationOperation;
    double tokenScore;
    double genealogyScore;
    double variableScore;
    double LCS;
    double cosineScore;

    @Override
    public int compareTo(CandidatePatch o) {
        return Double.compare(o.score, this.score);
    }

    @Override
    public String toString() {
        ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
        PatchGenerator patchGenerator = PatchGenerator.createPatchGenerator();
        String modifiedFaultyNode = faultyNode.toString().replaceAll("[\\t\\n\\r,]+"," ") + "," + modelExtractor.getNodeType(faultyNode);
//        System.out.println("Faulty Node: "+faultyNode.toString());
//        System.out.println("Modified Faulty Node: "+modifiedFaultyNode);
        String modifiedFixingIngredient = fixingIngredient.toString().replaceAll("[\\t\\n\\r,]+"," ") + "," + modelExtractor.getNodeType(fixingIngredient);
//        System.out.println("Fixint Ingred: "+fixingIngredient.toString());
//        System.out.println("Modified Faulty Node: "+modifiedFixingIngredient);
//        System.out.println("Candiate Patch: "+modifiedFaultyNode + ", line no: " + patchGenerator.compilationUnit.getLineNumber(faultyNode.getStartPosition()) + "," + modifiedFixingIngredient + ", line no: " + patchGenerator.compilationUnit.getLineNumber(fixingIngredient.getStartPosition()) + ", " +score + ", " + mutationOperation + ", " + genealogyScore+", "+variableScore+", "+LCS);
        return modifiedFaultyNode + ", line no: " + patchGenerator.compilationUnit.getLineNumber(faultyNode.getStartPosition()) + "," + modifiedFixingIngredient + ", line no: " + patchGenerator.compilationUnit.getLineNumber(fixingIngredient.getStartPosition()) + ", " +score + ", " + mutationOperation + ", " + genealogyScore+", "+variableScore+", "+LCS;
    }
}

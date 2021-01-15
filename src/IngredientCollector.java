import org.eclipse.jdt.core.dom.*;
import java.util.ArrayList;


public class IngredientCollector extends ASTVisitor {
    private static IngredientCollector ingredientCollector;
    //	Tokenizer tokenizer = new Tokenizer();
    ArrayList<FaultyNode> faultyNodes = new ArrayList<FaultyNode>();
    ArrayList<FixingIngredient> fixingIngredients = new ArrayList<FixingIngredient>();


    private IngredientCollector() {

    }

    public static IngredientCollector createIngredientCollector() {
        if(ingredientCollector == null){
            ingredientCollector = new IngredientCollector();
        }

        return ingredientCollector;
    }

    @Override
    public void preVisit(ASTNode node) {

        if(node instanceof Expression || node instanceof ExpressionStatement)
            this.collectFaultyNode(node);


        if(node.getNodeType()==ASTNode.EXPRESSION_STATEMENT ||
                 node.getNodeType()==ASTNode.IF_STATEMENT || node.getNodeType()==ASTNode.BOOLEAN_LITERAL
        || node.getNodeType()==ASTNode.SIMPLE_NAME || node.getNodeType()==ASTNode.QUALIFIED_NAME) {
            this.findFixingIngredients(node);
        }

    }

    private void collectFaultyNode(ASTNode node) {
        PatchGenerator patchGenerator = PatchGenerator.createPatchGenerator();
        FaultyNode faultyNode = new FaultyNode();
        faultyNode.node = node;
        faultyNode.startLine = patchGenerator.compilationUnit.getLineNumber(node.getStartPosition());
        faultyNode.endLine = patchGenerator.compilationUnit.getLineNumber(node.getStartPosition()+node.getLength());

        double total = 0;
        FaultLocalizer faultLocalizer = FaultLocalizer.createFaultLocalizer();
        for(int i=0;i<faultLocalizer.faultyLines.size();i++){
            FaultyLine faultLine = faultLocalizer.faultyLines.get(i);
            if(faultLine.lineNumber>=faultyNode.startLine && faultLine.lineNumber<=faultyNode.endLine){
                total = total + faultLine.suspiciousValue;
            }
        }
        faultyNode.suspiciousValue = (double)total/((double)faultyNode.endLine-faultyNode.startLine+1);
        if(faultyNode.suspiciousValue>0) {
//			System.out.println("SUSP " + node);
            ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
//			Tokenizer tokenizer = Tokenizer.createTokenizer();
            faultyNode.type = modelExtractor.getNodeType(node);
            if(!faultyNode.type.equals("") && !faultyNode.type.equals("BLOCK")){
                faultyNode.genealogy = modelExtractor.getGenealogyContext(node);
                faultyNode.variableAccessed = modelExtractor.getVariableContext(node);
//				faultyNode.tokens = tokenizer.getTokens(faultyNode.startLine, faultyNode.endLine);
                this.faultyNodes.add(faultyNode);
            }
        }
    }

    private void findFixingIngredients(ASTNode node) {
        PatchGenerator patchGenerator = PatchGenerator.createPatchGenerator();
        ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
//		Tokenizer tokenizer = Tokenizer.createTokenizer();
        FixingIngredient fixingIngredient = new FixingIngredient();
        fixingIngredient.node = node;
        fixingIngredient.startLine = patchGenerator.compilationUnit.getLineNumber(node.getStartPosition());
        fixingIngredient.endLine = patchGenerator.compilationUnit.getLineNumber(node.getStartPosition()+node.getLength());
        fixingIngredient.type = modelExtractor.getNodeType(node);
        fixingIngredient.genealogy = modelExtractor.getGenealogyContext(node);
        fixingIngredient.variableAccessed = modelExtractor.getVariableContext(node);
//		fixingIngredient.tokens = tokenizer.getTokens(fixingIngredient.startLine, fixingIngredient.endLine);
        this.fixingIngredients.add(fixingIngredient);
//		System.out.println("NODE :" +node.toString() + " "+fixingIngredient.startLine+ " "+fixingIngredient.endLine);
//		System.out.println(fixingIngredient.tokens);
        //				if(node instanceof Expression ) {
        //					exp++;
        //				}
        //				if( node instanceof Type) {
        //					type++;
        //				}
        //				if( node instanceof Statement ) {
        //					stmt++;
        //				}
//
    }
}

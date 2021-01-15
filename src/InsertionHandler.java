import org.eclipse.jdt.core.dom.ASTNode;

public class InsertionHandler {
    public static InsertionHandler insertionHandler;
    CompatibilityChecker compatibilityChecker;
    PatchListUpdater patchListUpdater;
    Tokenizer tokenizer;



    private InsertionHandler() {
        this.compatibilityChecker = CompatibilityChecker.createCompatibilityChecker();
        this.patchListUpdater = PatchListUpdater.createPatchListUpdater();
        this.tokenizer = Tokenizer.createTokenizer();
    }

    public static InsertionHandler createInsertionHandler() {
        if(insertionHandler == null){
            insertionHandler = new InsertionHandler();
        }

        return insertionHandler;
    }

    public void insertUnderMethodInvocation(FaultyNode faultyNode){
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for(int i=0;i<ingredientCollector.fixingIngredients.size();i++){
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

           if(fixingIngredient.node.getNodeType()== ASTNode.QUALIFIED_NAME || fixingIngredient.node.getNodeType()== ASTNode.SIMPLE_NAME || fixingIngredient.node.getNodeType()== ASTNode.BOOLEAN_LITERAL){


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);
                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString());
                //System.out.println(modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString()));
//                try {
//                    faultyNode.tokens = tokenizer.tokenize(faultyNode.node.toString());
//                    fixingIngredient.tokens = tokenizer.tokenize(fixingIngredient.node.toString());
//                    candidatePatch.tokenScore = modelExtractor.getTokenSimilarityScore(faultyNode.tokens, fixingIngredient.tokens);
//                   // System.out.println("Token score: " + candidatePatch.tokenScore);
//                }
//                catch (Exception e){e.printStackTrace();}
                double dependencyScore = 1;
                candidatePatch.mutationOperation = "insert";
                if(fixingIngredient.node.getNodeType()== ASTNode.SIMPLE_NAME)
//                     candidatePatch.score = faultyNode.suspiciousValue*0.0313*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                     candidatePatch.score = faultyNode.suspiciousValue*0.0313*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));
                else if(fixingIngredient.node.getNodeType()== ASTNode.QUALIFIED_NAME)
//                    candidatePatch.score = faultyNode.suspiciousValue*0.0075*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                    candidatePatch.score = faultyNode.suspiciousValue*0.0075*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));
                else if(fixingIngredient.node.getNodeType()== ASTNode.BOOLEAN_LITERAL)
//                    candidatePatch.score = faultyNode.suspiciousValue*0.0047*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                    candidatePatch.score = faultyNode.suspiciousValue*0.0047*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));

//                if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0 && candidatePatch.cosineScore>0) {
                if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0) {
                    this.patchListUpdater.updatePatchList(candidatePatch);

                }
            }
        }
    }

    public void insertUnderMethodDeclaration(FaultyNode faultyNode){
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for(int i=0;i<ingredientCollector.fixingIngredients.size();i++){
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if(fixingIngredient.node.getNodeType()== ASTNode.IF_STATEMENT || fixingIngredient.node.getNodeType()== ASTNode.EXPRESSION_STATEMENT){


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);
                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString());
               // System.out.println(modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString()));
//                try {
//                    faultyNode.tokens = tokenizer.tokenize(faultyNode.node.toString());
//                    fixingIngredient.tokens = tokenizer.tokenize(fixingIngredient.node.toString());
//                    candidatePatch.tokenScore = modelExtractor.getTokenSimilarityScore(faultyNode.tokens, fixingIngredient.tokens);
//                  //  System.out.println("Token score: " + candidatePatch.tokenScore);
//                }
//                catch (Exception e){e.printStackTrace();}
                double dependencyScore = 1;
                candidatePatch.mutationOperation = "insert";
                if(fixingIngredient.node.getNodeType()== ASTNode.IF_STATEMENT)
//                    candidatePatch.score = faultyNode.suspiciousValue*0.0063*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                    candidatePatch.score = faultyNode.suspiciousValue*0.0063*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));
                else if(fixingIngredient.node.getNodeType()== ASTNode.EXPRESSION_STATEMENT)
//                    candidatePatch.score = faultyNode.suspiciousValue*0.0815*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                    candidatePatch.score = faultyNode.suspiciousValue*0.0815*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));

//                 if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0 && candidatePatch.cosineScore>0) {
                if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0) {
                    this.patchListUpdater.updatePatchList(candidatePatch);

                }
            }
        }
    }

    public void insertUnderIfStatement(FaultyNode faultyNode){
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for(int i=0;i<ingredientCollector.fixingIngredients.size();i++){
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if(fixingIngredient.node.getNodeType()== ASTNode.EXPRESSION_STATEMENT){


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);
                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString());
               // System.out.println(modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString()));
//                try {
//                    faultyNode.tokens = tokenizer.tokenize(faultyNode.node.toString());
//                    fixingIngredient.tokens = tokenizer.tokenize(fixingIngredient.node.toString());
//                    candidatePatch.tokenScore = modelExtractor.getTokenSimilarityScore(faultyNode.tokens, fixingIngredient.tokens);
//                   // System.out.println("Token score: " + candidatePatch.tokenScore);
//                }
//                catch (Exception e){e.printStackTrace();}
                double dependencyScore = 1;
                candidatePatch.mutationOperation = "insert";
//               candidatePatch.score = faultyNode.suspiciousValue*0.0075*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                candidatePatch.score = faultyNode.suspiciousValue*0.0075*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));

//                 if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0 && candidatePatch.cosineScore>0) {
                if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0) {
                    this.patchListUpdater.updatePatchList(candidatePatch);

                }
            }
        }
    }

    public void insertUnderTryStatement(FaultyNode faultyNode){
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for(int i=0;i<ingredientCollector.fixingIngredients.size();i++){
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if(fixingIngredient.node.getNodeType()== ASTNode.IF_STATEMENT || fixingIngredient.node.getNodeType()== ASTNode.EXPRESSION_STATEMENT){


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);
                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString());
               // System.out.println(modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(),fixingIngredient.node.toString()));
//                try {
//                    faultyNode.tokens = tokenizer.tokenize(faultyNode.node.toString());
//                    fixingIngredient.tokens = tokenizer.tokenize(fixingIngredient.node.toString());
//                    candidatePatch.tokenScore = modelExtractor.getTokenSimilarityScore(faultyNode.tokens, fixingIngredient.tokens);
//                  //  System.out.println("Token score: " + candidatePatch.tokenScore);
//                }
//                catch (Exception e){e.printStackTrace();}
                double dependencyScore = 1;
                candidatePatch.mutationOperation = "insert";
                if(fixingIngredient.node.getNodeType()== ASTNode.IF_STATEMENT)
//                    candidatePatch.score = faultyNode.suspiciousValue*0.0063*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                      candidatePatch.score = faultyNode.suspiciousValue*0.0063*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));
                else if(fixingIngredient.node.getNodeType()== ASTNode.EXPRESSION_STATEMENT)
//                    candidatePatch.score = faultyNode.suspiciousValue*0.0141*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS*candidatePatch.cosineScore));
                    candidatePatch.score = faultyNode.suspiciousValue*0.0141*((0.4*candidatePatch.genealogyScore)+(0.6*candidatePatch.LCS));


//                 if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0 && candidatePatch.cosineScore>0) {
                if(candidatePatch.LCS>0 && candidatePatch.genealogyScore>0 ) {
                    this.patchListUpdater.updatePatchList(candidatePatch);

                }
            }
        }
    }
}

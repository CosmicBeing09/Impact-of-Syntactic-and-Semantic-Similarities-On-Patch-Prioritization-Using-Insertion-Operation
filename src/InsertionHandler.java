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
        if (insertionHandler == null) {
            insertionHandler = new InsertionHandler();
        }

        return insertionHandler;
    }

    public void insertUnderMethodInvocation(FaultyNode faultyNode) {
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for (int i = 0; i < ingredientCollector.fixingIngredients.size(); i++) {
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if (fixingIngredient.node.getNodeType() == ASTNode.QUALIFIED_NAME || fixingIngredient.node.getNodeType() == ASTNode.SIMPLE_NAME || fixingIngredient.node.getNodeType() == ASTNode.BOOLEAN_LITERAL) {


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);

                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaroWinklerDistance = modelExtractor.getJaroWinklerDistance(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaccardDistance = modelExtractor.getJaccardDistance(faultyNode.node.toString(), fixingIngredient.node.toString());

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
                if (fixingIngredient.node.getNodeType() == ASTNode.SIMPLE_NAME) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationSimpleNameFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationSimpleNameFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationSimpleNameFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);

                } else if (fixingIngredient.node.getNodeType() == ASTNode.QUALIFIED_NAME) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
                    //candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationQualifiedNameFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationQualifiedNameFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationQualifiedNameFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);

                } else if (fixingIngredient.node.getNodeType() == ASTNode.BOOLEAN_LITERAL) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationBooleanLiteralFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationBooleanLiteralFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodInvocationBooleanLiteralFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);
                }

//
//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaroWinklerDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaccardDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.cosineScore > 0 && !modelExtractor.isAntiPattern(fixingIngredient.node.toString())) {

                    this.patchListUpdater.updatePatchList(candidatePatch);
                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

            }
        }
    }

    public void insertUnderMethodDeclaration(FaultyNode faultyNode) {
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for (int i = 0; i < ingredientCollector.fixingIngredients.size(); i++) {
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if (fixingIngredient.node.getNodeType() == ASTNode.IF_STATEMENT || fixingIngredient.node.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);

                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaroWinklerDistance = modelExtractor.getJaroWinklerDistance(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaccardDistance = modelExtractor.getJaccardDistance(faultyNode.node.toString(), fixingIngredient.node.toString());

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
                if (fixingIngredient.node.getNodeType() == ASTNode.IF_STATEMENT) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodDeclarationIfFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodDeclarationIfFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodDeclarationIfFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);

                } else if (fixingIngredient.node.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodDeclarationExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodDeclarationExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.methodDeclarationExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);
                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaroWinklerDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaccardDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.cosineScore > 0 && !modelExtractor.isAntiPattern(fixingIngredient.node.toString())) {

                    this.patchListUpdater.updatePatchList(candidatePatch);
                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

            }
        }
    }

    public void insertUnderIfStatement(FaultyNode faultyNode) {
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for (int i = 0; i < ingredientCollector.fixingIngredients.size(); i++) {
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if (fixingIngredient.node.getNodeType() == ASTNode.IF_STATEMENT || fixingIngredient.node.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);

                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaroWinklerDistance = modelExtractor.getJaroWinklerDistance(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaccardDistance = modelExtractor.getJaccardDistance(faultyNode.node.toString(), fixingIngredient.node.toString());

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
                if (fixingIngredient.node.getNodeType() == ASTNode.IF_STATEMENT) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.ifIfFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.ifIfFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.ifIfFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);

                } else if (fixingIngredient.node.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.ifExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.ifExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                    candidatePatch.score = faultyNode.suspiciousValue * Frequency.ifExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);

                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaroWinklerDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaccardDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.cosineScore > 0 && !modelExtractor.isAntiPattern(fixingIngredient.node.toString())) {

                    this.patchListUpdater.updatePatchList(candidatePatch);
                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

            }
        }
    }

    public void insertUnderTryStatement(FaultyNode faultyNode) {
        IngredientCollector ingredientCollector = IngredientCollector.createIngredientCollector();
        for (int i = 0; i < ingredientCollector.fixingIngredients.size(); i++) {
            FixingIngredient fixingIngredient = ingredientCollector.fixingIngredients.get(i);

            if (fixingIngredient.node.getNodeType() == ASTNode.EXPRESSION_STATEMENT) {


                ModelExtractor modelExtractor = ModelExtractor.createModelExtractor();
                CandidatePatch candidatePatch = new CandidatePatch();
                candidatePatch.faultyNode = faultyNode.node;
                candidatePatch.fixingIngredient = fixingIngredient.node;

                candidatePatch.LCS = modelExtractor.getNormalizedLongestCommonSubsequence(candidatePatch.faultyNode.toString(), candidatePatch.fixingIngredient.toString());
                candidatePatch.genealogyScore = modelExtractor.getGenealogySimilarityScore(faultyNode.genealogy, fixingIngredient.genealogy);
                candidatePatch.variableScore = modelExtractor.getVariableSimilarityScore(faultyNode.variableAccessed, fixingIngredient.variableAccessed);

                candidatePatch.cosineScore = modelExtractor.getCosineSimilarityScore(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaroWinklerDistance = modelExtractor.getJaroWinklerDistance(faultyNode.node.toString(), fixingIngredient.node.toString());
                candidatePatch.jaccardDistance = modelExtractor.getJaccardDistance(faultyNode.node.toString(), fixingIngredient.node.toString());

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

                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore;
//                    candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance;
//                candidatePatch.score = candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance;

//                candidatePatch.score = faultyNode.suspiciousValue * Frequency.tryExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaroWinklerDistance);
//                candidatePatch.score = faultyNode.suspiciousValue * Frequency.tryExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.jaccardDistance);
//                candidatePatch.score = faultyNode.suspiciousValue * Frequency.tryExpressionFrequency * (candidatePatch.genealogyScore + candidatePatch.LCS + candidatePatch.cosineScore);

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaroWinklerDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.jaccardDistance > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0 && candidatePatch.cosineScore > 0 && !modelExtractor.isAntiPattern(fixingIngredient.node.toString())) {

                    this.patchListUpdater.updatePatchList(candidatePatch);
                }

//                if (candidatePatch.genealogyScore > 0 && candidatePatch.LCS > 0) {
//
//                    this.patchListUpdater.updatePatchList(candidatePatch);
//                }

            }
        }
    }
}

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import java.io.File;
import java.io.IOException;

public class TargetBlockInsertionHandler {

    private static TargetBlockInsertionHandler targetIfStatementInsertionHandler;
    WriteChangedAstToFile writeChangedAstToFile;

    private TargetBlockInsertionHandler() {
        writeChangedAstToFile = WriteChangedAstToFile.createWriteChangedAstToFile();
    }

    public static TargetBlockInsertionHandler createIfStatementInsertionHandler() {
        if (targetIfStatementInsertionHandler == null) ;
        targetIfStatementInsertionHandler = new TargetBlockInsertionHandler();

        return targetIfStatementInsertionHandler;
    }

    int insertBefore(ASTNode parent, CandidatePatch candidatePatch, ASTRewrite astRewrite, Document document, File file, int count, int rank) throws IOException, BadLocationException {
        try {
            ListRewrite listRewrite = astRewrite.getListRewrite(parent, Block.STATEMENTS_PROPERTY);
            listRewrite.insertBefore(candidatePatch.fixingIngredient, candidatePatch.faultyNode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writeChangedAstToFile.writeChangedAst(astRewrite, document, file, count, candidatePatch, rank);

    }

    int insertAfter(ASTNode parent, CandidatePatch candidatePatch, ASTRewrite astRewrite, Document document, File file, int count, int rank) throws IOException, BadLocationException {
        try {
            ListRewrite listRewrite = astRewrite.getListRewrite(parent, Block.STATEMENTS_PROPERTY);
            listRewrite.insertAfter(candidatePatch.fixingIngredient, candidatePatch.faultyNode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writeChangedAstToFile.writeChangedAst(astRewrite, document, file, count, candidatePatch, rank);
    }
}

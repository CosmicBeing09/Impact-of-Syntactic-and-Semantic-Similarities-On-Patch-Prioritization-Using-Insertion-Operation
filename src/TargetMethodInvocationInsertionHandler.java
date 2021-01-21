import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import java.io.File;
import java.io.IOException;

public class TargetMethodInvocationInsertionHandler {


    private static TargetMethodInvocationInsertionHandler targetMethodInvocationInsertionHandler;
    WriteChangedAstToFile writeChangedAstToFile;

    private TargetMethodInvocationInsertionHandler() {
        writeChangedAstToFile = WriteChangedAstToFile.createWriteChangedAstToFile();
    }

    public static TargetMethodInvocationInsertionHandler createTargetMethodInvocationInsertionHandler() {
        if (targetMethodInvocationInsertionHandler == null) ;
        targetMethodInvocationInsertionHandler = new TargetMethodInvocationInsertionHandler();

        return targetMethodInvocationInsertionHandler;
    }


    int insertBefore(ASTNode parent, CandidatePatch candidatePatch, ASTRewrite astRewrite, Document document, File file, int count, int rank) throws IOException, BadLocationException {
        try {
            ListRewrite listRewrite = astRewrite.getListRewrite(parent, MethodInvocation.ARGUMENTS_PROPERTY);
            listRewrite.insertBefore(candidatePatch.fixingIngredient, candidatePatch.faultyNode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writeChangedAstToFile.writeChangedAst(astRewrite, document, file, count, candidatePatch, rank);

    }

    int insertAfter(ASTNode parent, CandidatePatch candidatePatch, ASTRewrite astRewrite, Document document, File file, int count, int rank) throws IOException, BadLocationException {
        try {
            ListRewrite listRewrite = astRewrite.getListRewrite(parent, MethodInvocation.ARGUMENTS_PROPERTY);
            listRewrite.insertAfter(candidatePatch.fixingIngredient, candidatePatch.faultyNode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writeChangedAstToFile.writeChangedAst(astRewrite, document, file, count, candidatePatch, rank);
    }
}

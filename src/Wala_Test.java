import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.slicer.HeapStatement;
import com.ibm.wala.ipa.slicer.NormalReturnCaller;
import com.ibm.wala.ipa.slicer.NormalStatement;
import com.ibm.wala.ipa.slicer.ParamCallee;
import com.ibm.wala.ipa.slicer.ParamCaller;
import com.ibm.wala.ipa.slicer.SDG;
import com.ibm.wala.ipa.slicer.Slicer;
import com.ibm.wala.ipa.slicer.Slicer.ControlDependenceOptions;
import com.ibm.wala.ipa.slicer.Slicer.DataDependenceOptions;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.ipa.slicer.Statement.Kind;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.types.Descriptor;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphIntegrity;
import com.ibm.wala.util.graph.GraphIntegrity.UnsoundGraphException;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.io.CommandLine;
import com.ibm.wala.util.io.FileProvider;
import com.ibm.wala.util.strings.Atom;
import com.ibm.wala.viz.DotUtil;
import com.ibm.wala.viz.NodeDecorator;
import com.ibm.wala.viz.PDFViewUtil;
import org.eclipse.jdt.core.dom.ASTNode;

import static com.ibm.wala.ipa.slicer.SlicerUtil.findCallTo;


public class Wala_Test {
//    public static void main(String[] args) throws WalaException, IllegalArgumentException, CancelException, IOException {
//        Properties p = CommandLine.parse(args);
//        try {
//            String appJar = "C:\\Users\\ASUS\\Documents\\github\\Moumita-Apu-Thesis\\Test-Thesis-Code\\out\\artifacts\\Test_Thesis_Code_jar\\Test_Thesis_Code_jar.jar";
//            String mainClass = "C:\\Users\\ASUS\\Documents\\github\\Moumita-Apu-Thesis\\Test-Thesis-Code\\out\\artifacts\\Test_Thesis_Code_jar\\Test_Thesis_Code_jar.jar";
//
//            AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar, (new FileProvider())
//                    .getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
//
//            // build a class hierarchy, call graph, and system dependence graph
//            ClassHierarchy cha = ClassHierarchyFactory.make(scope);
//            Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha, mainClass);
//            AnalysisOptions options = CallGraphTestUtil.makeAnalysisOptions(scope, entrypoints);
//            CallGraphBuilder builder = Util.makeZeroCFABuilder(options, new AnalysisCache(), cha, scope);
//            CallGraph cg = builder.makeCallGraph(options, null);
//            SDG sdg = new SDG(cg, builder.getPointerAnalysis(), getDataDependenceOptions(p), getControlDependenceOptions(p));
//            Statement s = findCallTo(findMainMethod(cg), "println");
//            Collection<Statement> slice = Slicer.computeBackwardSlice(s, cg, builder.getPointerAnalysis(), getDataDependenceOptions(p), getControlDependenceOptions(p));
//        }catch(CallGraphBuilderCancelException e){
//            System.out.println("Failed");
//        }
//        }
//
//    public static CGNode findMainMethod(CallGraph cg) {
//        Descriptor d = Descriptor.findOrCreateUTF8("([Ljava/lang/String;)V");
//        Atom name = Atom.findOrCreateUnicodeAtom("main");
//        for (Iterator<? extends CGNode> it = cg.getSuccNodes(cg.getFakeRootNode()); it.hasNext();) {
//            CGNode n = it.next();
//            if (n.getMethod().getName().equals(name) && n.getMethod().getDescriptor().equals(d)) {
//                return n;
//            }
//        }
//        Assertions.UNREACHABLE("failed to find main() method");
//        return null;
//    }
//
//
//    public static DataDependenceOptions getDataDependenceOptions(Properties p) {
//        String d = p.getProperty("dd", "full");
//        for (DataDependenceOptions result : DataDependenceOptions.values()) {
//            if (d.equals(result.getName())) {
//                return result;
//            }
//        }
//        Assertions.UNREACHABLE("unknown data datapendence option: " + d);
//        return null;
//    }
//
//    public static ControlDependenceOptions getControlDependenceOptions(Properties p) {
//        String d = p.getProperty("cd", "full");
//        for (ControlDependenceOptions result : ControlDependenceOptions.values()) {
//            if (d.equals(result.getName())) {
//                return result;
//            }
//        }
//        Assertions.UNREACHABLE("unknown control datapendence option: " + d);
//        return null;
//    }
    }


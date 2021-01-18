import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

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


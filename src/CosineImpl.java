import info.debatty.java.stringsimilarity.Cosine;

public class CosineImpl {
    public static Cosine cosine;
    public static Cosine createCosine() {
        if(cosine == null){
            cosine = new Cosine();
        }

        return cosine;
    }
}

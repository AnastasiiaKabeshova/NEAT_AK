
import java.util.ArrayList;
import java.util.List;


public class GraphDate {
    /**
     * list errors for best organism in current generation
     */
    private List<List<Double>> errors = new ArrayList<List<Double>>();
    private final int numberIterations;
    
    public GraphDate(List<List<Double>> xerrors, int iter) {
        errors = xerrors;
        numberIterations = iter;
    }
    
    public double getData(int sample, int i) {
        return errors.get(sample).get(i);
    }
    
    public int getSize_inSample() {
        return errors.get(0).size();
    }

    Object getNiterations() {
        return numberIterations;
    }
}

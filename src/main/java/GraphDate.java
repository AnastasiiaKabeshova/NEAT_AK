
import java.util.ArrayList;
import java.util.List;


public class GraphDate {
    /**
     * list errors for best organism in current generation
     */
    private List<List<Double>> errors = new ArrayList<List<Double>>();
    
    public GraphDate(List<List<Double>> xerrors) {
        errors = xerrors;
    }
    
    public double getData(int sample, int i) {
        return errors.get(sample).get(i);
    }
    
    public int getSize() {
        return errors.get(0).size();
    }
}

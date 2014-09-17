
import java.util.ArrayList;
import java.util.List;


public final class GraphDate {
    /**
     * list errors for best organism in current generation
     */
    private List<List<Double>> errors = new ArrayList<>();
    private final int numberIterations;
    private int generationNumber;
    
    public GraphDate(List<List<Double>> xerrors, int iter, int generNumber) {
        errors = xerrors;
        numberIterations = iter;
        setGenerationNumber(generNumber);
    }
    
    public double getData(int sample, int i) {
        return errors.get(sample).get(i);
    }
    
    public List<List<Double>> getAllErrors () {
        return errors;
    }
    
    public int getSize_inSample() {
        return errors.get(0).size();
    }

    int getNiterations() {
        return numberIterations;
    }

    /**
     * @return the generationNumber
     */
    public int getGenerationNumber() {
        return generationNumber;
    }

    /**
     * @param generationNumber the generationNumber to set
     */
    public void setGenerationNumber(int generationNumber) {
        this.generationNumber = generationNumber;
    }
}

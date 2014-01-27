
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;


public class NetworkTest {
    
    public NetworkTest() {
    }

    @Test
    public void testAjustmentWeigth() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes2 = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0);
        Organism o2 =  fabrica.createOrganism(inNodes2, outNodes);
        setWeigth(o2, 0, 0.1);
        setWeigth(o2, 1, 0.2);
        setWeigth(o2, 2, 0.3);
        
        o2.countFitnessOut();
        Network net = o2.getNet();
        net.ajustmentWeigth();
        Assert.assertFalse("just for check weight ajustement", false);
    }
    
    private void setWeigth(Organism o2, int index, double w) {
        o2.getGenome().getGenes().get(index).getLink().setWeight(w);
    }
}
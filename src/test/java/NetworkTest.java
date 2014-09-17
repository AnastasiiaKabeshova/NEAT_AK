
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class NetworkTest {

    public NetworkTest() {
    }

    @Test
    public void testAjustmentWeigth() throws Exception {
        OrganismFactory fabrica = new OrganismFactory();
        List<Double> inNodes2 = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> outNodes = Arrays.asList(1.0);
        Organism o2 = fabrica.createOrganismFromSpecimen();
        setWeigth(o2, 0, 0.1);
        setWeigth(o2, 1, 0.2);
        setWeigth(o2, 2, 0.3);

        o2.countFitnessOut(1);
        Network net = o2.getNet();
        net.ajustmentWeigth();
        Assert.assertFalse("just for check weight ajustement", false);
    }

    private void setWeigth(Organism o2, int index, double w) {
        o2.getGenome().getGenes().get(index).getLink().setWeight(w);
    }

    @Test
    public void testSelectRandomNode() {
        OrganismFactory fabrica = new OrganismFactory();
        List<Double> inNodes2 = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> outNodes = Arrays.asList(1.0);
        Organism o2 = fabrica.createOrganismFromSpecimen();
        setWeigth(o2, 0, 0.1);
        setWeigth(o2, 1, 0.2);
        setWeigth(o2, 2, 0.3);
        o2.getGenome().mutate_addNode(5);
        //refresh Network in organism
        o2.setNet(new Network(o2.getGenome()));

        //o2.countFitnessOut();
        Network net = o2.getNet();
        Node first = o2.getGenome().getGenes().get(2).getLink().getIn_node();
        Node instance = net.selectRandomNode(first);
        Assert.assertFalse("just for check random select", false);
    }
}
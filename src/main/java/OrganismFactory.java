
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrganismFactory {

    private static int enableNodeID = 1;
    private static int enableLinkID = 1;
    private static int enableGenomeID = 0;
    private static int enableInnovationNumber = 1;

    private Organism specimen = new Organism();

    public static int nextEnabeledGenomeID() {
        return enableGenomeID++;
    }

    public static int nextEnabeledInnovetionNumber() {
        return enableInnovationNumber++;
    }

    public static void resetIndexes() {
        enableNodeID = 1;
        enableLinkID = 1;
        enableInnovationNumber = 1;
        enableGenomeID = 1;
    }

    public void setEnabeledInnovetionNumber(int num) {
        enableInnovationNumber = num;
    }

    public static int nextEnabledNodeID() {
        return enableNodeID++;
    }

    public static int nextEnabledLinkID() {
        return enableLinkID++;
    }

    public void createSpecimen(List<Double> in, List<Double> out) {
        List<Node> outputSpecimen = new ArrayList<>();
        Network net = new Network();
        for (int i = 0; i < out.size(); i++) {
            Node newOutNode = new Node(out.get(i), NodeType.OUTPUT, nextEnabledNodeID());
            net.addOutNode(newOutNode);
            outputSpecimen.add(newOutNode);
        }
        List<Gene> genes = new ArrayList<>();
        for (int j = 0; j < in.size(); j++) {
            Node newInNode = new Node(in.get(j), NodeType.INPUT, nextEnabledNodeID());
            net.addInNode(newInNode);
            for (int i = 0; i < outputSpecimen.size(); i++) {
                Link newLink = new Link(newInNode, outputSpecimen.get(i));  // newOutNode.addIncomingLink(newLink); - Link constructior do this
                genes.add(new Gene(newLink, nextEnabeledInnovetionNumber(), true));
            }
        }
        Genome newGenome = new Genome(nextEnabeledGenomeID(), genes);
        specimen = new Organism(newGenome);
    }

    public Organism createOrganismFromSpecimen() {
        return (createOrganism(specimen.getGenome().getGenes()));
    }

    public Organism createOrganism(List<Gene> genes) {
        List<Gene> copyGenes = new ArrayList<>();
        for (Iterator<Gene> it = genes.iterator(); it.hasNext();) {
            copyGenes.add(new Gene(it.next()));
        }
        Genome newGenome = new Genome(nextEnabeledGenomeID(), copyGenes);
        //refresh NET made in Organism constructor
        return (new Organism(newGenome));
    }

    public Organism dublicate(Organism oldOrganism) {
        Organism newOrganism = createOrganism(oldOrganism.getGenome().getGenes());
        return (newOrganism);
    }
}

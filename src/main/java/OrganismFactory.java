
import java.util.ArrayList;
import java.util.List;

public class OrganismFactory {

    private static int enableNodeID = 1;
    private static int enableLinkID = 1;
    private static int enableGenomeID = 1;
    private static int enableInnovationNumber = 1;

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

    public Organism createOrganism(List<Double> inNodes, List<Double> outNodes) {
        //create initial genome in -> out
        List<Gene> genes = new ArrayList<Gene>();
        List<Node> createdOutNodes = new ArrayList<Node>();
        Network net = new Network();
        for (int i = 0; i < outNodes.size(); i++) {
            Node newOutNode = new Node(outNodes.get(i), NodeType.OUTPUT);
            newOutNode.setNodeID(nextEnabledNodeID());
            createdOutNodes.add(newOutNode);
            net.addOutNode(newOutNode);
        }
        for (int j = 0; j < inNodes.size(); j++) {
            Node newInNode = new Node(inNodes.get(j), NodeType.INPUT);
            newInNode.setNodeID(nextEnabledNodeID());
            net.addInNode(newInNode);
            for (int i = 0; i < createdOutNodes.size(); i++) {
                Link newLink = new Link(newInNode, createdOutNodes.get(i));  // newOutNode.addIncomingLink(newLink); - Link constructior do this
                genes.add(new Gene(newLink, nextEnabeledInnovetionNumber(), true));
            }
        }
        Genome newGenome = new Genome(nextEnabeledGenomeID(), genes);
        return (new Organism(newGenome, net));
    }

    public Organism createOrganism(List<Gene> genes) {
        Genome newGenome = new Genome(nextEnabeledGenomeID(), genes);
        //refresh NET made in Organism constructor
        return (new Organism(newGenome));
    }

    public Organism dublicate(Organism oldOrganism) {
        Organism newOrganism = new Organism(oldOrganism.getGenome());
        newOrganism.getGenome().setGenome_id(nextEnabeledGenomeID());
        return (newOrganism);
    }
}
